package io.github.anblusis.netBattleRoyal.main

import io.github.anblusis.netBattleRoyal.command.CommandManager
import io.github.anblusis.netBattleRoyal.data.*
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
        registerRecipe()
        registerCommand()

        server.scheduler.runTaskTimer(this, ticker, 0L, 1L)
    }

    private fun registerRecipe() {
        CustomRecipe.values().forEach {
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
        val willRemovedGames = games.toList()
        willRemovedGames.forEach { it.remove() }
        CustomRecipe.values().forEach { it.removeFromServer() }
        server.scheduler.cancelTasks(this)
    }
}
