package io.github.anblusis.netBattleRoyal.event

import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerQuitEvent

fun playerOpenChest(listener: EventManager, event: InventoryOpenEvent) : EventResult {
    val marmotte = DataManager.getMarmotte(event.player as Player)
    marmotte.game?.chests?.forEach {
        if (it.location == (event.inventory.holder as Chest).location) {
            it.open()
            return EventResult.OPEN_ROYALE_CHEST
        }
    }
    return EventResult.OPEN_CHEST
}
