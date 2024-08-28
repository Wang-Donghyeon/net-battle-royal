package io.github.anblusis.netBattleRoyal.game.event

import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.game.GameWeather
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import java.time.Duration

class ChangeWeather(
    private val game: Game,
    private val regions: List<Region>
): Runnable {
    override fun run() {
        regions.forEach { region ->
            region.gameWeather = GameWeather.values().filter { it != region.gameWeather }.random()
        }

        game.players.filter { DataManager.getMarmotte(it).region in regions }.forEach {
            val region = DataManager.getMarmotte(it).region!!
            it.showTitle(
                Title.title(
                    text(""),
                    text("날씨가 ${region.gameWeather.displayName} 상태로 변화했습니다.").color(NamedTextColor.GOLD),
                    Title.Times.times(
                        Duration.ofMillis(500),
                        Duration.ofSeconds(2),
                        Duration.ofMillis(500)
                    )
                )
            )
        }
    }
}