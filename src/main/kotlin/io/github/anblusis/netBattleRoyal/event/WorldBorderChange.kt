package io.github.anblusis.netBattleRoyal.event

import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.papermc.paper.event.world.border.WorldBorderEvent
import org.bukkit.event.player.PlayerQuitEvent

fun worldBorderChange(listener: EventManager, event: WorldBorderEvent) : EventResult {
    plugin.games.filter { game -> game.world == event.world }.forEach { game ->
        game.maps.forEach {

        }
    }
    return EventResult.WORLD_BORDER_CHANGE
}
