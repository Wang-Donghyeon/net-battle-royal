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
import org.joml.Matrix4f
import java.time.Duration
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
        }
        val blockDisplay = world.spawn(spawnLocation, BlockDisplay::class.java).apply {
            block = Material.CHEST.createBlockData()
        }
        val glassDisplay = world.spawn(spawnLocation, BlockDisplay::class.java).apply {
            block = Material.GLASS.createBlockData()
            setTransformationMatrix(Matrix4f().scale(1.5f))
        }

        lateinit var belowBlock: Block
        lateinit var task: TickerTask

        task = plugin.ticker.runTaskTimer({
            belowBlock = blockDisplay.location.clone().subtract(0.0, 0.1, 0.0).block
            if (belowBlock.type.isSolid || belowBlock.location.y < 0) {
                blockDisplay.remove()
                glassDisplay.remove()
                world.playSound(blockDisplay.location, Sound.BLOCK_GLASS_BREAK, 1f, 1f)
                world.spawnParticle(Particle.BLOCK_CRACK, blockDisplay.location.toCenterLocation(), 10, 0.75, 0.75, 0.75, 0.0, Material.GLASS.createBlockData())
                task.cancel()
                createChest(blockDisplay.location)
                return@runTaskTimer
            }
            blockDisplay.location.subtract(0.0, 0.5, 0.0)
            glassDisplay.location.subtract(0.0, 0.5, 0.0)
        }, 0L, 1L)
    }

    private fun createChest(location: Location) {
        game.run {
            val chest = RoyalChest(this, ChestData(location.toBlockLocation(), ChestType.EPIC), chestTables.getValue(ChestType.EPIC))
            chests.add(chest)
            chestRegionCount[chest.region] = (chestRegionCount[chest.region] ?: 0) + 1
        }
    }
}