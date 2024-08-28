package io.github.anblusis.netBattleRoyal.game

import org.bukkit.WeatherType

enum class GameTime(val displayName: String, val time: Long) {
    MORNING("낮", 12000L),
    NIGHT("밤", 0L)
}

enum class GameWeather(val displayName: String, val weather: WeatherType) {
    SUNNY("맑음", WeatherType.CLEAR),
    RAINY("비", WeatherType.DOWNFALL)
}