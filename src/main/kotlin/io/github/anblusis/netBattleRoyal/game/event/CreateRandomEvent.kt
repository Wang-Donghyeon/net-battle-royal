package io.github.anblusis.netBattleRoyal.game.event

import io.github.anblusis.netBattleRoyal.data.Region
import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.game.GameTask
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Sound
import kotlin.random.Random

class CreateRandomEvent(
    private val game: Game,
    private val tick: Int,
    private val events: Array<RandomGameEvent>
): Runnable {
    override fun run() {
        val randomEvent = events.random()
        when(randomEvent) {
            RandomGameEvent.EPIC_CHEST -> {
                val regions = game.regions.filter { game.isInWorldBorder(it.center, true) }
                if (regions.isEmpty()) return
                val randomRegion = regions.random()
                game.players.forEach {
                    it.sendMessage(text("${tick / 20}초 후에 ").append(text(randomRegion.displayName).decorate(TextDecoration.BOLD)).append(text(" 지역에 상자가 떨어집니다.")).color(NamedTextColor.GOLD))
                    it.playSound(it.location, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 2.0f)
                }
                game.tasks.add(
                    GameTask(
                        game,
                        CreateEpicChest(game, randomRegion),
                        "에픽 상자",
                        tick,
                        2,
                        false,
                        listOf(randomRegion)
                    )
                )
            }
            RandomGameEvent.CHANGE_WEATHER -> {
                val regions = game.regions.filter { game.isInWorldBorder(it.center, true) }.shuffled()
                if (regions.isEmpty()) return
                val selectedRegions = mutableListOf<Region>()
                repeat(Random.nextInt(1, 5)) {
                    selectedRegions.add(regions[it])
                }
                game.players.forEach { player ->
                    player.sendMessage(text("${tick / 20}초 후에 ").append(text(selectedRegions.joinToString(", ") { it.displayName }).decorate(TextDecoration.BOLD)).append(text(" 지역의 날씨가 변합니다.")).color(NamedTextColor.GOLD))
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 2.0f)
                }
                game.tasks.add(
                    GameTask(
                        game,
                        ChangeWeather(game, selectedRegions),
                        "날씨 변경",
                        tick,
                        1,
                        false,
                        selectedRegions
                    )
                )
            }
        }
    }
}