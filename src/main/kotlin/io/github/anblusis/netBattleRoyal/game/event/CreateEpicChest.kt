package io.github.anblusis.netBattleRoyal.game.event

import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.github.monun.tap.task.TickerTask
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.BlockDisplay
import org.bukkit.util.Transformation
import org.joml.AxisAngle4f
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import java.time.Duration
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class CreateEpicChest(
    private val game: Game,
    private val region: Region
): Runnable {
    override fun run() {
        game.players.filter { DataManager.getMarmotte(it).region == region }.forEach {
            it.showTitle(
                Title.title(
                    text(""),
                    text("낙하").color(NamedTextColor.GOLD),
                    Title.Times.times(
                        Duration.ofMillis(500),
                        Duration.ofSeconds(2),
                        Duration.ofMillis(500)
                    )
                )
            )
        }

        val world = game.world
        val spawnLocation = region.center.clone().apply {
            x += (Random.nextDouble() - 0.5) * region.width
            y = world.maxHeight.toDouble()
            z += (Random.nextDouble() - 0.5) * region.height
        }.toBlockLocation()
        val displays = mutableListOf<BlockDisplay>()

        val blockDisplay = world.spawn(spawnLocation, BlockDisplay::class.java).apply {
            block = Material.CHEST.createBlockData()
            displays.add(this)
        }

        // 낙하산 천 부분 구현 (구의 일부분을 자른 형태)
        val radius = 2.5 // 구의 반지름
        val segments = 8 // 천의 곡선 부분을 표현하기 위한 분할 수
        val heightSegments = 4 // 천의 높이 방향으로의 분할 수
        val maxAngle = Math.PI / 4 // 구의 절단 각도 (구의 1/4 부분)

        for (ySegment in 0 until heightSegments) {
            val yAngle = maxAngle * ySegment / heightSegments
            val currentRadius = radius * cos(yAngle)
            val yOffset = radius * sin(yAngle) + 1.0 // 상자 위로 올리기

            for (i in 0 until segments) {
                val angle = Math.PI * 2 / segments * i
                val xOffset = currentRadius * cos(angle)
                val zOffset = currentRadius * sin(angle)

                world.spawn(spawnLocation.clone().apply {
                    x += xOffset
                    y += yOffset
                    z += zOffset
                }, BlockDisplay::class.java).apply {
                    block = Material.WHITE_WOOL.createBlockData()
                    // 회전을 적용하여 구의 일부처럼 보이도록 변환
                    val matrix = Matrix4f()
                        .rotateY(angle.toFloat())
                        .rotateX(-yAngle.toFloat())
                        .scale(1.5f, 0.1f, 1.5f)
                    setTransformationMatrix(matrix)
                    displays.add(this)
                }
            }
        }

        val cordLength = radius * sin(maxAngle) + 0.5
        // 끈 모형 구현 (참나무 목재로 동서남북 연결)
        val directions = arrayOf(
            Pair(1.0, 0.0),   // 동
            Pair(-1.0, 0.0),  // 서
            Pair(0.0, 1.0),   // 남
            Pair(0.0, -1.0)   // 북
        )

        directions.forEach { (dx, dz) ->
            world.spawn(spawnLocation.clone().apply {
                x += dx * 0.5
                y += 0.5
                z += dz * 0.5
            }, BlockDisplay::class.java).apply {
                block = Material.OAK_FENCE.createBlockData()

                val targetX = dx * radius
                val targetY = radius * sin(maxAngle) + 1.0
                val targetZ = dz * radius

                // 끈의 회전을 설정 (천의 끝과 상자 모서리를 연결)
                val matrix = Matrix4f()
                    .translate(0f, cordLength.toFloat(), 0f)
                    .rotateX(atan2(targetY, cordLength).toFloat())
                    .rotateZ(atan2(targetZ, targetX).toFloat())
                    .scale(0.1f, cordLength.toFloat(), 0.1f)

                setTransformationMatrix(matrix)
                displays.add(this)
            }
        }

        world.spawn(spawnLocation.clone().apply {
            x -= 0.25
            y -= 0.25
            z -= 0.25
        }, BlockDisplay::class.java).apply {
            block = Material.GLASS.createBlockData()
            setTransformationMatrix(Matrix4f().scale(1.5f))
            displays.add(this)
        }

        val endRodDisplay = world.spawn(spawnLocation.clone().apply {
            x -= 0.5
            y = world.getHighestBlockAt(spawnLocation).y.toDouble()
            z -= 0.5
        }, BlockDisplay::class.java).apply {
            block = Material.END_ROD.createBlockData()
            setTransformationMatrix(Matrix4f().scale(2f, (world.maxHeight - world.minHeight).toFloat(), 2f))
            game.entities.add(this)
        }

        displays.forEach {
            game.entities.add(it)
        }

        lateinit var belowBlock: Block
        lateinit var task: TickerTask
        var tick = 0

        task = plugin.ticker.runTaskTimer({
            belowBlock = blockDisplay.location.clone().subtract(0.0, 0.1, 0.0).block
            if (belowBlock.type.isSolid || belowBlock.location.y < 0) {
                displays.forEach {
                    game.entities.remove(it)
                    it.remove()
                }
                world.playSound(blockDisplay.location, Sound.BLOCK_GLASS_BREAK, 1f, 1f)
                world.spawnParticle(
                    Particle.BLOCK_CRACK,
                    blockDisplay.location.toCenterLocation(),
                    10,
                    0.75,
                    0.75,
                    0.75,
                    0.0,
                    Material.GLASS.createBlockData()
                )
                task.cancel()
                createChest(blockDisplay.location, endRodDisplay)
                return@runTaskTimer
            }
            displays.forEach {
                it.teleport(it.location.add(0.0, -0.5, 0.0))
            }
            endRodDisplay.teleport(endRodDisplay.location.apply {
                y = world.getHighestBlockAt(spawnLocation).y.toDouble()
            })

            tick ++

            // 만약 이 구문 정상작동 하면 틱 빼기
            if (endRodDisplay.interpolationDuration == 0) { 
                tick = 0 
                val scale = endRodDisplay.transformation.scale
                endRodDisplay.setTransformationMatrix(
                    Matrix4f().scale(scale).rotateY(Math.PI.toFloat() + 0.1f /* 보정 */)
                )
                endRodDisplay.interpolationDelay = 0
                endRodDisplay.interpolationDuration = 100
            }
        }, 0L, 1L)
    }

    private fun createChest(location: Location, beam: BlockDisplay) {
        game.run {
            val chest = RoyalChest(this, ChestData(location.toBlockLocation(), ChestType.EPIC), chestTables.getValue(ChestType.EPIC), beam)
            chests.add(chest)
            chestRegionCount[chest.region] = (chestRegionCount[chest.region] ?: 0) + 1
        }
    }
}