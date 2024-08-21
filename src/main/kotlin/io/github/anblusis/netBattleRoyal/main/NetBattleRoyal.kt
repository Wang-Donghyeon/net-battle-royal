package io.github.anblusis.netBattleRoyal.main

import io.github.anblusis.netBattleRoyal.command.CommandManager
import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.data.DataManager.addMarmotte
import io.github.anblusis.netBattleRoyal.event.EventManager
import io.github.anblusis.netBattleRoyal.game.Game
import io.github.monun.kommand.Kommand
import io.github.monun.tap.fake.FakeEntityServer
import io.github.monun.tap.task.Ticker
import io.github.monun.tap.task.TickerTask
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class NetBattleRoyal : JavaPlugin() {
    companion object {
        lateinit var plugin: NetBattleRoyal
    }

    val debugChestData = mutableListOf<ChestData>()
    val games: MutableList<Game> = mutableListOf()
    val marmottes: MutableList<Marmotte> = mutableListOf()
    val ticker: Ticker = Ticker.precision()

    lateinit var fakeEntityManager: FakeEntityServer

    override fun onLoad() {
        plugin = this
    }

    override fun onEnable() {
        EventManager.register()
        CommandManager.register()
        DataManager.register()
        registerPlayer()
        registerRecipe()

        fakeEntityManager = FakeEntityServer.create(this)
        ticker.runTaskTimer(fakeEntityManager::update, 0L, 1L)
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
        ticker.cancelAll()
        games.forEach { it.remove() }
        fakeEntityManager.clear()
        Recipe.values().forEach { it.removeFromServer() }
    }
}
