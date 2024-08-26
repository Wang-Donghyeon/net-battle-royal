package io.github.anblusis.netBattleRoyal.event

import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.game.GameState
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent
import javax.xml.crypto.Data

fun playerDamaged(listener: EventManager, event: EntityDamageEvent) : EventResult {
    DataManager.getMarmotte(event.entity as Player).game?.let {
        if (it.state == GameState.READYING) {
            event.isCancelled = true
            return EventResult.PLAYER_DAMAGED
        }
    }
    return EventResult.FAIL
}
