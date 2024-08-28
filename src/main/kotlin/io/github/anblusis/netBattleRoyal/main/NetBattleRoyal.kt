package io.github.anblusis.netBattleRoyal.main

import io.github.anblusis.netBattleRoyal.command.CommandManager
import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.data.DataManager.addMarmotte
import io.github.anblusis.netBattleRoyal.event.EventManager
import io.github.anblusis.netBattleRoyal.game.Game
import io.github.monun.kommand.kommand
import io.github.monun.tap.task.Ticker
import org.bukkit.plugin.java.JavaPlugin

class NetBattleRoyal : JavaPlugin() {
    companion object {
        lateinit var plugin: NetBattleRoyal
    }

    val debugChestData = mutableListOf<ChestData>()
    val games: MutableList<Game> = mutableListOf()
    val marmottes: MutableList<Marmotte> = mutableListOf()
    val ticker: Ticker = Ticker.precision()

    override fun onLoad() {
        plugin = this

    }

    override fun onEnable() {
        EventManager.register()
        DataManager.register()
        registerPlayer()
        registerRecipe()
        registerCommand()

        server.scheduler.runTaskTimer(this, ticker, 0L, 1L)
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

    private fun registerCommand() {
        kommand {
            CommandManager.register(this)
        }
    }

    override fun onDisable() {
        ticker.cancelAll()
        games.forEach { it.remove() }
        Recipe.values().forEach { it.removeFromServer() }
        server.scheduler.cancelTasks(this)
    }
}
