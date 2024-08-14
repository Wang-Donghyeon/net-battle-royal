package io.github.anblusis.netBattleRoyal.event

import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.plugin
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.pluginManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object EventManager : Listener {

    fun register() {
        pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    private fun onPlayerInteract(event: PlayerInteractEvent) { playerInteract(this, event) }
}
