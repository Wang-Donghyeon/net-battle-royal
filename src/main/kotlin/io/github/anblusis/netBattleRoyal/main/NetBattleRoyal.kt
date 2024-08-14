package io.github.anblusis.netBattleRoyal.main

import io.github.anblusis.netBattleRoyal.command.PluginCommand
import io.github.anblusis.netBattleRoyal.event.EventListener
import org.bukkit.plugin.java.JavaPlugin

class NetBattleRoyal : JavaPlugin() {
    lateinit var eventManager: EventListener
    lateinit var commandManager: PluginCommand

    override fun onEnable() {
        eventManager = EventListener(this)
        eventManager.startListener()

        commandManager = PluginCommand(this)
        commandManager.startCommands()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
