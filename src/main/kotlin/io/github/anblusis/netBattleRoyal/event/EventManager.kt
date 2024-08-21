package io.github.anblusis.netBattleRoyal.event

import io.github.anblusis.netBattleRoyal.data.EventResult
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.papermc.paper.event.world.border.WorldBorderEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object EventManager : Listener {

    fun register() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) { playerJoin(this, event) }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) { playerQuit(this, event) }

    @EventHandler
    private fun onPlayerInteract(event: PlayerInteractEvent) { playerInteract(this, event) }

    @EventHandler
    private fun onPlayerInventoryOpen(event: InventoryOpenEvent) {
        if (event.player !is Player) return
        if (event.inventory.type == InventoryType.CHEST) {
            playerOpenChest(this, event)
        }
    }

    @EventHandler
    private fun onWorldBorderChange(event: WorldBorderEvent) {
        worldBorderChange(this, event)
    }
}
