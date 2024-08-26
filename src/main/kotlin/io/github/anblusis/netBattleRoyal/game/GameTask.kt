package io.github.anblusis.netBattleRoyal.game

import io.github.anblusis.netBattleRoyal.data.Region

data class GameTask(val game: Game, val task: Runnable, val displayName: String, var tick: Int, val maxTick: Int, val priority: Int, val canRestart: Boolean, val regions: List<Region> = listOf()) {
    fun run() {
        task.run()
    }
}