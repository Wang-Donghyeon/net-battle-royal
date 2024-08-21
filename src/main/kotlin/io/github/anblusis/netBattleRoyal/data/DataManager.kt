package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.entity.Player

object DataManager {
    lateinit var marmotteByName: Map<Player, Marmotte>
        private set

    fun register() {
        marmotteByName = plugin.marmottes.associateBy { it.player }
    }

    fun getMarmotte(player: Player): Marmotte {
        return marmotteByName[player]!!
    }

    fun addMarmotte(player: Player) {
        plugin.marmottes.add(Marmotte(player))
        marmotteByName = plugin.marmottes.associateBy { it.player } as HashMap<Player, Marmotte>
    }

    fun removeMarmotte(player: Player) {
        plugin.marmottes.remove(Marmotte(player))
        marmotteByName = plugin.marmottes.associateBy { it.player } as HashMap<Player, Marmotte>
    }
}
