package io.github.anblusis.netBattleRoyal.game

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.WeatherType

enum class GameTime(val displayName: String, val time: Long) {
    MORNING("낮", 12000L),
    NIGHT("밤", 0L)
}

enum class GameWeather(val displayName: String, val weather: WeatherType, val color: TextColor) {
    SUNNY("맑음", WeatherType.CLEAR, NamedTextColor.WHITE),
    RAINY("비", WeatherType.DOWNFALL, NamedTextColor.BLUE)
}