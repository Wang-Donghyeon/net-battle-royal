package io.github.anblusis.netBattleRoyal.main

import io.github.anblusis.netBattleRoyal.event.EventListener
import org.bukkit.plugin.java.JavaPlugin

class NetBattleRoyal : JavaPlugin() {
    lateinit var eventManager: EventListener

    override fun onEnable() {
        eventManager = EventListener(this)
        eventManager.startListener()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
