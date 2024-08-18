package io.github.anblusis.netBattleRoyal.event

import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.pluginManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object EventManager : Listener {

    fun register() {
        pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) { playerJoin(this, event) }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) { playerQuit(this, event) }

    @EventHandler
    private fun onPlayerInteract(event: PlayerInteractEvent) { playerInteract(this, event) }
}
