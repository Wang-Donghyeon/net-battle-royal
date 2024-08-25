package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import kotlin.math.abs

data class Marmotte(val player: Player) {
    var game: Game? = null
    private var bossBar: BossBar? = null

    val region: Region?
        get() {
            if (game == null) return null
            val regions = game!!.regions.filter { game!!.isInRegion(it, player.location) }
            return regions.maxByOrNull { it.priority }
        }

    private fun updateBossBar() {
        bossBar?.setTitle(region?.displayName ?: "지역 없음")

        val allChestCount = game!!.chestRegionCount[region] ?: 0
        if (allChestCount == 0) {
            bossBar?.progress = 0.0
            return
        }

        val leftChestCount = game!!.chests.count { it.region == region && !it.isOpened }

        bossBar?.progress = leftChestCount.toDouble() / allChestCount
    }

    fun update() {
        updateBossBar()
    }

    fun joinGame(game: Game) {
        player.sendMessage("게임에 참가했습니다.")
        this.game = game
        bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID).apply { addPlayer(player) }
    }

    fun leaveGame() {
        if (game == null) return
        player.sendMessage("게임을 나갔습니다.")
        bossBar!!.removeAll()
        game!!.players.remove(player)
        this.game = null
    }
}