package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.entity.Player

object DataManager {
    private val marmotteByPlayer: Map<Player, Marmotte>
        get() = plugin.marmottes.associateBy { it.player }

    fun getMarmotte(player: Player): Marmotte? {
        return marmotteByPlayer[player]
    }

    fun addMarmotte(player: Player, game: Game): Marmotte {
        val marmotte = Marmotte(player, game)
        plugin.marmottes.add(marmotte)
        return marmotte
    }

    fun removeMarmotte(marmotte: Marmotte) {
        plugin.marmottes.remove(marmotte)
    }
}
