package io.github.anblusis.netBattleRoyal.data

import org.bukkit.Location

data class Region(val name: String, val displayName: String, val center: Location, val width: Double, val height: Double, val priority: Int)