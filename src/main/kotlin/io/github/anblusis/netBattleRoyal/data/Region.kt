package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.GameWeather
import org.bukkit.Location

data class Region(val name: String, val displayName: String, val center: Location, val width: Double, val height: Double, val priority: Int) {
    constructor(name: String, displayName: String, center: Location, radius: Double, priority: Int) : this(name, displayName, center, radius, radius, priority)

    internal var gameWeather = GameWeather.SUNNY
}