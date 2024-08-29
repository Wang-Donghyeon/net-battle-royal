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
import org.bukkit.entity.Display
import org.bukkit.util.Vector
import org.joml.Matrix4f
import java.time.Duration
import kotlin.math.atan2
import kotlin.math.sqrt
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

        val radius = 2.5
        val height = 3

        world.spawn(spawnLocation.clone().apply {
            x -= (radius - 1) / 2
            y += height
            z -= (radius - 1) / 2
        }, BlockDisplay::class.java).apply {
            block = Material.WHITE_WOOL.createBlockData()

            val matrix = Matrix4f()
                .scale(radius.toFloat(), 1.25f, radius.toFloat()) // 열기구 모양의 반구 크기 조정

            setTransformationMatrix(matrix)
            displays.add(this)
        }

        val directions = arrayOf(
            Vector(1.0, 0.0, 1.0),
            Vector(-1.0, 0.0, -1.0),
            Vector(-1.0, 0.0, 1.0),
            Vector(1.0, 0.0, -1.0)
        )

        val cordScale = 0.3

        directions.forEach { direction ->
            val cordLocation = spawnLocation.clone().toCenterLocation().apply {
                x += (direction.x * 0.75) - (0.3 * cordScale / 2)
                y += 0.75
                z += (direction.z * 0.75) - (0.3 * cordScale / 2)
            }
            world.spawn(cordLocation, BlockDisplay::class.java).apply {
                block = Material.OAK_FENCE.createBlockData()

                val matrix = Matrix4f()
                    .scale(cordScale.toFloat(), (height - 0.75).toFloat(), cordScale.toFloat()) // 끈의 스케일 조정

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

        val beamDisplays = mutableListOf<BlockDisplay>()

        world.spawn(spawnLocation.clone().apply {
            x += 0.5 - (0.3 / 2) // 크기 (0.3f)의 2 나눈 값을 더함
            y = world.getHighestBlockAt(spawnLocation).y.toDouble()
            z += 0.5 - (0.3 / 2) // 크기 (0.3f)의 2 나눈 값을 더함
        }, BlockDisplay::class.java).apply {
            block = Material.WHITE_CONCRETE.createBlockData()
            setTransformationMatrix(Matrix4f().scale(0.3f, (world.maxHeight - world.minHeight).toFloat(), 0.3f))
            viewRange = 10f
            brightness = Display.Brightness(15, 0)
            game.entities.add(this)
            beamDisplays.add(this)
        }
        world.spawn(spawnLocation.clone().apply {
            x += 0.5 - (0.5 / 2) // 크기 (0.5f)의 2 나눈 값을 더함
            y = world.getHighestBlockAt(spawnLocation).y.toDouble()
            z += 0.5 - (0.5 / 2) // 크기 (0.5f)의 2 나눈 값을 더함
        }, BlockDisplay::class.java).apply {
            block = Material.WHITE_STAINED_GLASS.createBlockData()
            setTransformationMatrix(Matrix4f().scale(0.5f, (world.maxHeight - world.minHeight).toFloat(), 0.5f))
            viewRange = 10f
            brightness = Display.Brightness(15, 0)
            game.entities.add(this)
            beamDisplays.add(this)
        }

        displays.forEach {
            game.entities.add(it)
        }

        lateinit var belowBlock: Block
        lateinit var task: TickerTask
        var rotationAngle = 0f

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
                createChest(blockDisplay.location, beamDisplays)
                return@runTaskTimer
            }
            displays.forEach {
                it.teleport(it.location.add(0.0, -0.5, 0.0))
            }

            rotationAngle += 0.1f

            beamDisplays.forEach { beamDisplay ->
                beamDisplay.teleport(beamDisplay.location.apply {
                    y = world.getHighestBlockAt(spawnLocation).y.toDouble() + 0.01
                })
            }

        }, 0L, 1L)
    }

    private fun createChest(location: Location, beams: List<BlockDisplay>) {
        game.run {
            val chest = RoyalChest(this, ChestData(location.toBlockLocation(), ChestType.EPIC), chestTables.getValue(ChestType.EPIC), beams)
            chests.add(chest)
            chestRegionCount[chest.region] = (chestRegionCount[chest.region] ?: 0) + 1
        }
    }
}