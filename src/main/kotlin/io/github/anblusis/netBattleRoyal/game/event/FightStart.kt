package io.github.anblusis.netBattleRoyal.game.event

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.game.GameState
import io.github.anblusis.netBattleRoyal.game.GameTask
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.time.Duration

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
        game.players.forEach {
            it.showTitle(
                Title.title(
                    text(""),
                    text("무적이 해제됩니다.").color(NamedTextColor.AQUA),
                    Title.Times.times(
                        Duration.ofMillis(500),
                        Duration.ofSeconds(2),
                        Duration.ofMillis(500)
                    )
                )
            )
        }
        game.run {
            tasks.add(
                GameTask(this,
                    WorldBorderDecrease(this, 1200),
                    "월드보더 감소",
                    6000,
                    6000,
                    0,
                    true
                )
            )
            tasks.add(
                GameTask(
                    this,
                    CreateRandomEvent(this, 1200, RandomGameEvent.values()),
                    "무작위 사건 타이머",
                    7200,
                    7200,
                    -999,
                    true
                )
            )
        }
    }
}