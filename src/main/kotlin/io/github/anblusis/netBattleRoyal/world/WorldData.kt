package io.github.anblusis.netBattleRoyal.world

import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.game.GameWeather
import org.bukkit.Location

internal interface WorldData {
    fun getCenter(): Location

    fun getWorldDefaultWeather(): GameWeather

    fun getWorldBorderSize(): Double

    fun getChestCount(): Int

    fun getChestLocations(): List<ChestData>

    fun getRegions(): List<Region>

    fun getChestTables(): HashMap<ChestType, ChestLootTable>

    fun getMapColors(): List<Byte>

    fun getCustomRecipes(): List<CustomRecipe>
}