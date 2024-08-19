package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class ChestData(val location: Location, var type: ChestType)

enum class ChestType(val rating: String) {
    NORMAL("평범"),
    RARE("희귀"),
    EPIC("에픽")
}

data class RoyalChest(val game: Game, val chestData: ChestData, val items: MutableList<ItemStack>) {
    constructor(game: Game, chestData: ChestData) : this(game, chestData, mutableListOf())

    private var isOpened = false

    val location
        get() = chestData.location

    fun generateItems() {
        items.clear()
    }

    fun update() {
        if (location.block.type != Material.CHEST) {
            remove()
            return
        }
    }

    fun remove() {
        game.chests.remove(this)
    }
}


