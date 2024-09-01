package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.WeatherType
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import javax.xml.crypto.Data
import kotlin.math.abs

data class Marmotte(val player: Player, val game: Game) {
    private val bossBar: BossBar

    val region: Region?
        get() {
            val regions = game.regions.filter { game.isInRegion(it, player.location) }
            return regions.maxByOrNull { it.priority }
        }

    init {
        player.sendMessage("게임에 참가했습니다.")
        bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID).apply { addPlayer(player) }
    }

    private fun updateBossBar() {
        val allChestCount = game.chestRegionCount[region] ?: 0
        val leftChestCount = game.chests.count { it.region == region && !it.isOpened }

        val tasks = game.tasks.filter { it.regions.isEmpty() || it.regions.contains(region) }
        val firstTask = tasks.minByOrNull { it.tick - it.priority * 1200 }

        if (firstTask != null) {
            bossBar.setTitle("${region?.displayName ?: "지역 없음"} ($leftChestCount / $allChestCount) | ${firstTask.displayName}")
            bossBar.progress = firstTask.tick.toDouble() / firstTask.maxTick
        } else {
            bossBar.setTitle("${region?.displayName ?: "지역 없음"} ($leftChestCount / $allChestCount)")
            bossBar.progress = 1.0
        }
    }

    fun update() {
        updateBossBar()
        updateWeather()
    }

    private fun updateWeather() {
        val weather = region?.gameWeather?.weather ?: game.worldDefaultWeather.weather
        if (weather != player.playerWeather) player.setPlayerWeather(weather)
    }

    fun remove() {
        player.sendMessage("게임을 나갔습니다.")
        bossBar.removeAll()
        game.marmottes.remove(this)
        DataManager.removeMarmotte(this)
    }
}