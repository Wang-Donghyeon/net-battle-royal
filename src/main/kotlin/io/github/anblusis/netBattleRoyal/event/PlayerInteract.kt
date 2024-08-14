package io.github.anblusis.netBattleRoyal.event

import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

fun playerInteract(listener: EventListener, event: PlayerInteractEvent) : String? {
    if (event.action == Action.RIGHT_CLICK_BLOCK) {
        val block = event.clickedBlock
        if (block != null) {
            val chestData = mapOf(
                "location" to block.location,
                "type" to 1,
            )
            clickedBlocksData.add(chestData)
            event.player.sendMessage("블록 위치와 데이터가 저장되었습니다!")
        }
    }
}
