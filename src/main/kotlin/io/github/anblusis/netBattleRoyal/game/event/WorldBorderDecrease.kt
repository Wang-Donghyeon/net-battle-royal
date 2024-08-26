package io.github.anblusis.netBattleRoyal.game.event

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.github.monun.tap.task.TickerTask
import net.kyori.adventure.title.Title
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import java.time.Duration

class WorldBorderDecrease(
    private val game: Game,
    private val tick: Int
): Runnable {
    override fun run() {
        val size = game.worldBorderSize
        val center = game.worldBorderCenter

        game.players.forEach {
            it.showTitle(
                Title.title(
                    text(""),
                    text("월드보더가 ${tick / 20}초에 걸쳐 변화합니다!").color(NamedTextColor.AQUA),
                    Title.Times.times(
                        Duration.ofMillis(5),
                        Duration.ofSeconds(2),
                        Duration.ofMillis(5)
                    )
                )
            )
            it.sendMessage(
                text("중심 위치: ")
                    .append(text("${center.blockX}, ${center.blockZ} ").color(NamedTextColor.GREEN)
                    .append(text("(${center.distance(it.location).toInt()} 블록 거리)"))))
            it.sendMessage(
                text("경계 크기: ")
                    .append(text("$size ").color(NamedTextColor.GREEN)
                        .append(text("(${game.worldBorderSize - size} 블록 감소)"))))
        }

        game.worldBorder.setSize(size, (tick * 20).toLong())
        val offSetLocation = center.subtract(game.worldBorderCenter).multiply(1.0 / tick)
        var moveTick = 0
        lateinit var task: TickerTask
        task = plugin.ticker.runTaskTimer({
            game.worldBorder.center = game.worldBorderCenter.add(offSetLocation)
            if (moveTick++ >= tick) task.cancel()
        }, 0L, 1L)
    }
}