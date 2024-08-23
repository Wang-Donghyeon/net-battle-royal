package io.github.anblusis.netBattleRoyal.world

import io.github.anblusis.netBattleRoyal.data.ChestData
import io.github.anblusis.netBattleRoyal.data.ChestLootTable
import io.github.anblusis.netBattleRoyal.data.ChestType
import io.github.anblusis.netBattleRoyal.data.Region
import org.bukkit.Location
import java.awt.image.BufferedImage

internal interface WorldData {
    fun getCenter(): Location
    val center: Location
        get() = getCenter()

    fun getWorldBorderSize(): Double
    val worldBorderSize: Double
        get() = getWorldBorderSize()

    fun getChestCount(): Int
    val chestCount: Int
        get() = getChestCount()

    fun getChestLocations(): List<ChestData>
    val chestLocations: List<ChestData>
        get() = getChestLocations()

    fun getRegions(): List<Region>
    val regions: List<Region>
        get() = getRegions()

    fun getChestTables(): HashMap<ChestType, ChestLootTable>
    val chestTables: HashMap<ChestType, ChestLootTable>
        get() = getChestTables()

    fun getMapImage(): BufferedImage
    val mapImage: BufferedImage
        get() = getMapImage()
}