package io.github.anblusis.netBattleRoyal.game
import io.github.anblusis.netBattleRoyal.data.ChestData
import io.github.anblusis.netBattleRoyal.data.DataManager
import io.github.anblusis.netBattleRoyal.data.Marmotte
import io.github.anblusis.netBattleRoyal.data.Region
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.entity.Player
import org.bukkit.map.MapRenderer

class Game(
    val map: String,
    val mode: Int,
    val players: MutableList<Player>
) {
    lateinit var chests: MutableList<ChestData>
    lateinit var regions: MutableList<Region>

    init {
        registerChests(map)
        registerRegions(map)
        registerPlayers(players)
    }

    private fun registerChests(map: String) {
        if (map == "school") {
            chests = mutableListOf()
        }
    }

    private fun registerRegions(map: String) {
        if (map == "school") {
            regions = mutableListOf()
        }
    }

    private fun registerPlayers(players: MutableList<Player>) {
        players.forEach { player ->
            DataManager.getMarmotte(player).joinGame(this)
        }
    }
}
