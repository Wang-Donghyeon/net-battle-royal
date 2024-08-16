package io.github.anblusis.netBattleRoyal.main

import io.github.anblusis.netBattleRoyal.command.CommandManager
import io.github.anblusis.netBattleRoyal.event.EventManager
import io.github.anblusis.netBattleRoyal.game.Game
import org.bukkit.plugin.java.JavaPlugin

object NetBattleRoyal : JavaPlugin() {
    val games: MutableList<Game> = mutableListOf()

    val plugin
        get() = NetBattleRoyal

    val pluginManager
        get() = server.pluginManager

    override fun onEnable() {
        EventManager.register()
        CommandManager.register()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
