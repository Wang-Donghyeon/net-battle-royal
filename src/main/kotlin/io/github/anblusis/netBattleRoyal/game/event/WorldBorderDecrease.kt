package io.github.anblusis.netBattleRoyal.game.event

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.github.monun.tap.task.TickerTask
import net.kyori.adventure.title.Title
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.util.Vector
import java.time.Duration
import kotlin.random.Random

class WorldBorderDecrease(
    private val game: Game,
    private val tick: Int
): Runnable {
    override fun run() {
        val sizeDecrease = game.worldBorderSize * Random.nextDouble(0.25, 0.35)
        val size = game.worldBorderSize - sizeDecrease
        val randomVector = Vector(
            Random.nextDouble(-sizeDecrease / 2, sizeDecrease / 2),
            0.0,
            Random.nextDouble(-sizeDecrease / 2, sizeDecrease / 2)
        )
        val center = game.worldBorderCenter.add(randomVector)

        game.targetWorldBorderCenter = center.clone()
        game.targetWorldBorderSize = size

        game.players.forEach {
            it.showTitle(
                Title.title(
                    text(""),
                    text("월드보더가 ${tick / 20}초에 걸쳐 변화합니다.").color(NamedTextColor.AQUA),
                    Title.Times.times(
                        Duration.ofMillis(500),
                        Duration.ofSeconds(2),
                        Duration.ofMillis(500)
                    )
                )
            )
            it.sendMessage(
                text("중심 위치: ")
                    .append(text("${center.blockX}, ${center.blockZ} ").color(NamedTextColor.GREEN))
                        .append(text("(처음에서 ${center.distance(game.worldBorderCenter).toInt()} 블록 거리)"))
            )
            it.sendMessage(
                text("경계 크기: ")
                    .append(text("${size.toInt()} ").color(NamedTextColor.GREEN))
                        .append(text("(처음에서 ${sizeDecrease.toInt()} 블록 감소)"))
            )
        }

        game.worldBorder.setSize(size, (tick.toDouble() / 20).toLong())
        val offSetLocation = center.subtract(game.worldBorderCenter).multiply(1.0 / tick)
        var moveTick = 0
        lateinit var task: TickerTask
        task = plugin.ticker.runTaskTimer({
            game.worldBorder.center = game.worldBorderCenter.add(offSetLocation)
            if (moveTick++ >= tick) task.cancel()
        }, 0L, 1L)
    }
}