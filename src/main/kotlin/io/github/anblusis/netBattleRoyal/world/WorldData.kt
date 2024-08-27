package io.github.anblusis.netBattleRoyal.world

import io.github.anblusis.netBattleRoyal.data.ChestData
import io.github.anblusis.netBattleRoyal.data.ChestLootTable
import io.github.anblusis.netBattleRoyal.data.ChestType
import io.github.anblusis.netBattleRoyal.data.Region
import org.bukkit.Location
import java.awt.image.BufferedImage

internal interface WorldData {
    fun getCenter(): Location

    fun getWorldBorderSize(): Double

    fun getChestCount(): Int

    fun getChestLocations(): List<ChestData>

    fun getRegions(): List<Region>

    fun getChestTables(): HashMap<ChestType, ChestLootTable>

    fun getMapImage(): BufferedImage

    fun getMapColors(): List<Byte>
}