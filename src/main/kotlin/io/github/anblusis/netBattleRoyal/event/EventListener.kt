package io.github.anblusis.netBattleRoyal.event

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin

class EventListener(
    internal val plugin: JavaPlugin
) : Listener {
    private val pluginManager
        get() = plugin.server.pluginManager

    fun startListener() {
        pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    private fun onPlayerInteract(event: PlayerInteractEvent) { playerInteract(this, event) }
}
