package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.math.abs

data class Marmotte(val player: Player) {
    var game: Game? = null

    val region: Region?
        get() {
            if (game == null) return null
            val regions = game!!.regions.filter { game!!.isInRegion(it, player.location) }
            return regions.maxByOrNull { it.priority }
        }

    fun joinGame(game: Game) {
        player.sendMessage("게임에 참가했습니다.")
        this.game = game
    }

    fun leaveGame() {
        player.sendMessage("게임을 나갔습니다.")
        this.game = null
    }
}