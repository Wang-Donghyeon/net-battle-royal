package io.github.anblusis.netBattleRoyal.event

import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.papermc.paper.event.world.border.WorldBorderBoundsChangeEvent
import io.papermc.paper.event.world.border.WorldBorderCenterChangeEvent
import io.papermc.paper.event.world.border.WorldBorderEvent
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object EventManager : Listener {

    fun register() {
        try {
            plugin.server.pluginManager.registerEvents(this, plugin)
        } catch (e: Exception) {
            plugin.logger.severe("Failed to register event listener: ${e.message}")
            e.printStackTrace()
        }
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
    private fun onWorldBorderBoundsChange(event: WorldBorderBoundsChangeEvent) {
        worldBorderChange(this, event)
    }

    @EventHandler
    private fun onWorldBorderCenterChange(event: WorldBorderCenterChangeEvent) {
        worldBorderChange(this, event)
    }
}