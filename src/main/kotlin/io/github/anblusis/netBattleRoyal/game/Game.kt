package io.github.anblusis.netBattleRoyal.game
import io.github.anblusis.netBattleRoyal.data.ChestData
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.plugin
import org.bukkit.entity.Player

class Game(
    val map: String,
    val mode: Int,
    val players: MutableList<Player>
) {
    lateinit var chests: MutableList<ChestData>

    init {
        setConfig()
    }

    fun setConfig() {
        chests = mutableListOf()
    }
}