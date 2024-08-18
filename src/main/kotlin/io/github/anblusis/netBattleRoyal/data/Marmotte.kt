package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.math.abs

data class Marmotte(val player: Player) {
    var game: Game? = null

    val region
        get() = getRegion()

    fun getRegion(): Region? {
        if (game == null) return null
        val regions = game!!.regions.filter { isInRegion(it) }
        return regions.maxByOrNull { it.priority }
    }

    fun joinGame(game: Game) {
        this.game = game
    }

    private fun isInRegion(region: Region): Boolean {
        val dx = abs(player.location.x - region.center.x)
        val dz = abs(player.location.z - region.center.z)
        return dx <= region.width / 2 && dz <= region.height / 2
    }
}