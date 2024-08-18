package io.github.anblusis.netBattleRoyal.event

import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.event.player.PlayerJoinEvent
import javax.xml.crypto.Data

fun playerJoin(listener: EventManager, event: PlayerJoinEvent) : EventResult {
    DataManager.addMarmotte(event.player)
    return EventResult.PLAYER_JOIN
}
