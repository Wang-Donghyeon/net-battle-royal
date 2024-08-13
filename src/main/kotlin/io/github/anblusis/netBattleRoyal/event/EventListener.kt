package io.github.anblusis.netBattleRoyal.event

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class EventListener(
    private val plugin: JavaPlugin
) : Listener {
    private val pluginManager = plugin.server.pluginManager

    public fun startListener() {
        pluginManager.registerEvents(this, plugin)
    }
}
