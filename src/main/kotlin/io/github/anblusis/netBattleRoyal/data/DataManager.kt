package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.data.BattleRoyalItemData
import io.github.anblusis.netBattleRoyal.data.ChestType
import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.github.monun.kommand.KommandArgument.Companion.dynamic
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import org.bukkit.Particle
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
