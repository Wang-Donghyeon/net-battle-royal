package io.github.anblusis.netBattleRoyal.data

import org.bukkit.Location

data class ChestData(val location: Location, var type: ChestType)

enum class ChestType(val rating: String) {
    NORMAL("평범"),
    RARE("희귀"),
    EPIC("에픽")
}

