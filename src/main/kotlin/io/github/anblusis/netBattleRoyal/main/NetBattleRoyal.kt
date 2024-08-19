package io.github.anblusis.netBattleRoyal.main

import io.github.anblusis.netBattleRoyal.command.CommandManager
import io.github.anblusis.netBattleRoyal.data.Marmotte
import io.github.anblusis.netBattleRoyal.data.ChestData
import io.github.anblusis.netBattleRoyal.data.DataManager
import io.github.anblusis.netBattleRoyal.data.DataManager.addMarmotte
import io.github.anblusis.netBattleRoyal.data.Recipe
import io.github.anblusis.netBattleRoyal.event.EventManager
import io.github.anblusis.netBattleRoyal.game.Game
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class NetBattleRoyal : JavaPlugin() {
    companion object {
        lateinit var plugin: NetBattleRoyal
        lateinit var pluginManager: PluginManager
    }

    val debugChestData = mutableListOf<ChestData>()
    val games: MutableList<Game> = mutableListOf()
    val marmottes: MutableList<Marmotte> = mutableListOf()

    override fun onLoad() {
        plugin = this
        pluginManager = server.pluginManager
    }

    override fun onEnable() {
        EventManager.register()
        CommandManager.register()
        DataManager.register()
        registerPlayer()
        registerRecipe()
    }

    private fun registerPlayer() {
        server.onlinePlayers.forEach {
            addMarmotte(it)
        }
    }

    private fun registerRecipe() {
        Recipe.values().forEach {
            it.addToServer()
        }
    }

    override fun onDisable() {

    }
}
