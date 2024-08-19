package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.MapCanvas
import org.bukkit.map.MapRenderer
import org.bukkit.map.MapView
import org.bukkit.map.MinecraftFont
import java.awt.Color

data class BattleRoyalMap(private val game: Game) {
    private val centerX = game.center.blockX
    private val centerZ = game.center.blockZ

    val item = BattleRoyalItemData.BATTLE_ROYAL_MAP.item

    init {
        val view = plugin.server.createMap(game.world)
        view.centerX = centerX
        view.centerZ = centerZ
        view.scale = MapView.Scale.FARTHEST
        view.isUnlimitedTracking = true

        view.addRenderer(WorldBorderRenderer())
        view.addRenderer(RegionRenderer())

        item.itemMeta = (item.itemMeta as MapMeta).apply {
            mapView = view
        }
    }

    inner class WorldBorderRenderer : MapRenderer() {
        override fun render(mapView: MapView, mapCanvas: MapCanvas, player: Player) {
            val size = game.worldBorderSize.toInt()
            val scale = mapView.scale.value
            for (x in -size..size) {
                for (z in -size..size) {
                    if (x == -size || x == size || z == -size || z == size) {
                        val relativeX = ((game.worldBorderCenter.blockX - centerX + x) / scale)
                        val relativeZ = ((game.worldBorderCenter.blockZ - centerZ + z) / scale)
                        if (relativeX in 0..127 && relativeZ in 0..127) {
                            mapCanvas.setPixelColor(relativeX, relativeZ, Color.RED)
                        }
                    }
                }
            }
        }
    }

    inner class RegionRenderer : MapRenderer() {
        override fun render(mapView: MapView, mapCanvas: MapCanvas, player: Player) {
            val scale = mapView.scale.value

            game.regions.forEach { region ->
                val relativeX = ((region.center.blockX - mapView.centerX) / scale) + 64
                val relativeZ = ((region.center.blockZ - mapView.centerZ) / scale) + 64
                if (relativeX in 0..127 && relativeZ in 0..127) {
                    mapCanvas.setPixelColor(relativeX, relativeZ, Color.BLUE)
                    mapCanvas.drawText(relativeX, relativeZ, MinecraftFont.Font, region.name)
                }
            }
        }
    }
}