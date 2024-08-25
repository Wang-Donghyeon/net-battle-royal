package io.github.anblusis.netBattleRoyal.event

import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.event.player.PlayerQuitEvent

fun playerQuit(listener: EventManager, event: PlayerQuitEvent) : EventResult {
    DataManager.getMarmotte(event.player).leaveGame()
    DataManager.removeMarmotte(event.player)
    return EventResult.PLAYER_QUIT
}
