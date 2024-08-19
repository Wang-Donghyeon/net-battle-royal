package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import net.kyori.adventure.text.Component.text
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.*
import java.awt.Color
import kotlin.math.pow
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class BattleRoyalMap(private val game: Game) {
    val item = BattleRoyalItemData.BATTLE_ROYAL_MAP.item

    init {
        val view = plugin.server.createMap(game.world)
        view.centerX = game.center.blockX
        view.centerZ = game.center.blockZ
        view.scale = MapView.Scale.NORMAL
        view.isUnlimitedTracking = true
        view.isTrackingPosition = true

        view.resetRenderers()

        renderFullMap(view)

        item.itemMeta = (item.itemMeta as MapMeta).apply {
            mapView = view
        }
        item.durability = view.id.toShort()
    }

    fun MapView.resetRenderers() {
        renderers.clear()
        addRenderer(WorldBorderRenderer)
        addRenderer(RegionRenderer)
    }

    private fun renderFullMap(mapView: MapView) {
        val world = mapView.world
        val centerX = mapView.centerX
        val centerZ = mapView.centerZ
        val scale = 1 shl mapView.scale.value.toInt()

        val mapSize = 128 * scale // 한 변의 길이(픽셀 수)
        val halfSize = mapSize / 2

        plugin.server.broadcast(text("지도 렌더링 시작"))
        // 지도의 좌측 상단 모서리 좌표 계산
        val startX = centerX - halfSize
        val startZ = centerZ - halfSize

        // 지도 전체를 커버하는 청크를 미리 로드하고, 해당 영역을 렌더링
        for (x in startX until startX + mapSize step 128) {
            for (z in startZ until startZ + mapSize step 128) {
                // 청크 강제 로드
                world!!.loadChunk(x shr 4, z shr 4)
                plugin.server.broadcast(text("청크 로드: $x, $z"))
                // 각 청크의 중간 지점에서 지도를 렌더링
                val blockX = x + 64
                val blockZ = z + 64
                mapView.centerX = blockX
                mapView.centerZ = blockZ

                mapView.renderers.forEach { renderer ->
                    renderer.render(mapView, object : MapCanvas {}, null)
                }
            }
        }

        // 지도의 중심을 원래 위치로 되돌림
        mapView.centerX = centerX
        mapView.centerZ = centerZ
        plugin.server.broadcast(text("지도 렌더링 완료"))
    }
}

object WorldBorderRenderer : MapRenderer() {
    override fun render(mapView: MapView, mapCanvas: MapCanvas, player: Player) {

        val game = DataManager.getMarmotte(player).game ?: return
        val centerX = game.center.blockX
        val centerZ = game.center.blockZ
        val size = (game.worldBorderSize / 2).toInt()
        val scale = (2.0).pow(mapView.scale.value.toDouble())

        for (x in -size..size) {
            for (z in -size..size) {
                if (x == -size || x == size || z == -size || z == size) {
                    val relativeX = 64 + ((game.worldBorderCenter.blockX - centerX + x) / scale).toInt()
                    val relativeZ = 64 + ((game.worldBorderCenter.blockZ - centerZ + z) / scale).toInt()
                    if (relativeX in 0..127 && relativeZ in 0..127) {
                        mapCanvas.setPixelColor(relativeX, relativeZ, Color.RED)
                    }
                }
            }
        }
    }
}

object RegionRenderer : MapRenderer() {
    override fun render(mapView: MapView, mapCanvas: MapCanvas, player: Player) {
        val game = DataManager.getMarmotte(player).game ?: return
        val scale = (2.0).pow(mapView.scale.value.toDouble())

        game.regions.forEach { region ->
            val relativeX = 64 + ((region.center.blockX - mapView.centerX) / scale).toInt()
            val relativeZ = 64 + ((region.center.blockZ - mapView.centerZ) / scale).toInt()
            if (relativeX in 0..127 && relativeZ in 0..127) {
                mapCanvas.setPixelColor(relativeX, relativeZ, Color.BLUE)
                mapCanvas.drawText(relativeX, relativeZ, MinecraftFont.Font, region.name)
            }
        }
    }
}

object CustomCanvas : MapCanvas {
    override fun getMapView(): MapView {
        return mapView
    }
    override fun setPixelColor(x: Int, y: Int, color: Color?) {
        if (x in 0..127 && y in 0..127) {
            pixels[x][y] = color
        }
    }

    override fun getPixelColor(x: Int, y: Int): Color? {
        return if (x in 0..127 && y in 0..127) {
            pixels[x][y]
        } else {
            null
        }
    }

    override fun getBasePixelColor(x: Int, y: Int): Color {
        return Color(0, 0, 0, 0) // 기본 색상 (투명)
    }

    override fun getCursors(): MapCursorCollection {
    }
}