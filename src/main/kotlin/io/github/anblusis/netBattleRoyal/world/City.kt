package io.github.anblusis.netBattleRoyal.world

import io.github.anblusis.netBattleRoyal.data.ChestData
import io.github.anblusis.netBattleRoyal.data.ChestLootTable
import io.github.anblusis.netBattleRoyal.data.ChestType
import io.github.anblusis.netBattleRoyal.data.Region
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.Location
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object City : WorldData {
    override fun getCenter(): Location =
        Location(plugin.server.getWorld("school"), -248.0, 0.0, 840.0)

    override fun getWorldBorderSize(): Double =
        380.0

    override fun getChestCount(): Int =
        100

    override fun getChestLocations(): List<ChestData> =
        listOf()

    override fun getRegions(): List<Region> =
        listOf(
            Region("school", "학교", Location(plugin.server.getWorld("school"), -248.0, 0.0, 840.0), 100.0, 100.0, 0)
        )

    override fun getChestTables(): HashMap<ChestType, ChestLootTable> =
        hashMapOf(
            ChestType.NORMAL to
                    ChestLootTable(
                        listOf(

                        ),
                        listOf(

                        )
                    ),
            ChestType.RARE to
                    ChestLootTable(
                        listOf(

                        ),
                        listOf(

                        )
                    ),
            ChestType.EPIC to
                    ChestLootTable(
                        listOf(

                        ),
                        listOf(

                        )
                    )
        )
    override fun getMapImage(): BufferedImage {
        val imageStream = javaClass.getResourceAsStream("/images/cityMap.png")
        return try {
            ImageIO.read(imageStream)
        } catch (e: IllegalArgumentException) {
            BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB)
        }
    }
}