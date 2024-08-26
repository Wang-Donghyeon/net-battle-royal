package io.github.anblusis.netBattleRoyal.game.event

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.game.GameState
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class FightStart(
    private val game: Game,
    private val tick: Int
): Runnable {
    init {
        game.players.forEach {
            it.addPotionEffect(PotionEffect(PotionEffectType.SPEED, tick, 1, false, false))
        }
    }

    override fun run() {
        game.state = GameState.PLAYING
    }
}