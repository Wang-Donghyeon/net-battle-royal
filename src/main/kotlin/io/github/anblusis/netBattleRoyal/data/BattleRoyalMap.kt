package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style.style
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.*
import org.bukkit.util.Vector
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class BattleRoyalMap(private val game: Game, private val hasRender: Boolean = true) {
    val item = BattleRoyalItemData.BATTLE_ROYAL_MAP.item
    private val view: MapView

    init {
        game.maps.add(this)

        view = plugin.server.createMap(game.world)
        view.centerX = game.center.blockX
        view.centerZ = game.center.blockZ
        view.scale = MapView.Scale.NORMAL
        view.isUnlimitedTracking = true
        view.isTrackingPosition = true

        if (hasRender) {
            view.apply {
                renderers.clear()
                addRenderer(ImageMapRenderer)
                addRenderer(WorldBorderRenderer)
                addRenderer(RegionRenderer)
            }
        }

        item.itemMeta = (item.itemMeta as MapMeta).apply {
            mapView = view
        }
    }
}

object ImageMapRenderer : MapRenderer() {
    override fun render(mapView: MapView, mapCanvas: MapCanvas, player: Player) {
        val game = DataManager.getMarmotte(player).game ?: return

        game.mapColors.forEachIndexed { index, color ->
            val x = index % 128
            val y = index / 128
            mapCanvas.setPixel(x, y, color)
        }
        // mapCanvas.drawImage(0, 0,  game.mapImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH))
    }
}

object WorldBorderRenderer : MapRenderer() {
    override fun render(mapView: MapView, mapCanvas: MapCanvas, player: Player) {

        val game = DataManager.getMarmotte(player).game ?: return
        val centerX = game.center.blockX
        val centerZ = game.center.blockZ
        val size = (game.worldBorderSize / 2).toInt()
        val scale = (2.0).pow(mapView.scale.value.toDouble())

        game.worldBorderDots.forEach { (pair, color) ->
            mapCanvas.setPixelColor(pair.first, pair.second, color)
        }
        game.worldBorderDots.clear()

        for (x in -size..size) {
            for (z in -size..size) {
                if (x == -size || x == size || z == -size || z == size) {
                    val relativeX = 64 + ((game.worldBorderCenter.blockX - centerX + x) / scale).toInt()
                    val relativeZ = 64 + ((game.worldBorderCenter.blockZ - centerZ + z) / scale).toInt()
                    if (relativeX in 0..127 && relativeZ in 0..127) {
                        if (!game.worldBorderDots.containsKey(Pair(relativeX, relativeZ))) {
                            game.worldBorderDots[Pair(relativeX, relativeZ)] = mapCanvas.getPixelColor(relativeX, relativeZ)
                            mapCanvas.setPixelColor(relativeX, relativeZ, Color.RED)
                        }
                    }
                }
            }
        }
    }
}

object RegionRenderer : MapRenderer() {
    override fun render(mapView: MapView, mapCanvas: MapCanvas, player: Player) {
        val game = DataManager.getMarmotte(player).game ?: return
        if (mapCanvas.cursors.size() < game.regions.size) {
            val scale = (2.0).pow(mapView.scale.value.toDouble())

            game.regions.forEach { region ->
                var relativeX = 64 + ((region.center.blockX - mapView.centerX) / scale).toInt()
                var relativeZ = 64 + ((region.center.blockZ - mapView.centerZ) / scale).toInt()
                if (relativeX in 0..127 && relativeZ in 0..127) {
                    relativeX = (relativeX * 2) - 128
                    // 6 빼는건 일종의 보정
                    relativeZ = max((relativeZ * 2) - 128 - 6, -128)
                    val cursor = MapCursor(
                        relativeX.toByte(),
                        relativeZ.toByte(),
                        0,
                        MapCursor.Type.BLUE_POINTER,
                        true,
                        text(region.displayName)
                    )

                    mapCanvas.cursors.addCursor(cursor)
                }
            }
        }
    }
}