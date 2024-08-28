package io.github.anblusis.netBattleRoyal.game

import io.github.anblusis.netBattleRoyal.data.Region

data class GameTask(val game: Game, val task: Runnable, val displayName: String, var tick: Int, val maxTick: Int, val priority: Int, val canRestart: Boolean, val regions: List<Region> = listOf()) {
    constructor(game: Game, task: Runnable, displayName: String, tick: Int, priority: Int, canRestart: Boolean, regions: List<Region> = listOf()): this(game, task, displayName, tick, tick, priority, canRestart, regions)

    fun run() {
        task.run()
    }
}