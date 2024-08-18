package io.github.anblusis.netBattleRoyal.event

import io.github.anblusis.netBattleRoyal.data.BattleRoyalItemData
import io.github.anblusis.netBattleRoyal.data.ChestData
import io.github.anblusis.netBattleRoyal.data.ChestType
import io.github.anblusis.netBattleRoyal.data.EventResult
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

fun playerInteract(listener: EventManager, event: PlayerInteractEvent) : EventResult {
    if (event.item == null) return EventResult.FAIL
    if (!event.item!!.isSimilar(BattleRoyalItemData.MAGIC_STICK.itemStack)) return EventResult.FAIL

    when (event.action) {
        Action.RIGHT_CLICK_BLOCK -> {
            event.isCancelled = true

            val block = event.clickedBlock
            if (plugin.debugChestData.find { it.location == block!!.location } != null) {
                event.player.sendMessage("해당 위치엔 상자가 이미 존재합니다.")
                return EventResult.FAIL
            }
            plugin.debugChestData.add(ChestData(block!!.location, ChestType.NORMAL))
            event.player.sendMessage("블록 위치가 ${block.location.toVector()} 위치에 저장되었습니다!")
        }
        Action.LEFT_CLICK_BLOCK -> {
            event.isCancelled = true

            val block = event.clickedBlock
            val selecting = plugin.debugChestData.find { it.location == block!!.location }
            if (selecting == null) {
                event.player.sendMessage("해당 위치엔 상자가 존재하지 않습니다.")
                return EventResult.FAIL
            }
            selecting.type = when(selecting.type) {
                ChestType.NORMAL -> ChestType.RARE
                ChestType.RARE -> ChestType.EPIC
                ChestType.EPIC -> ChestType.NORMAL
            }
            event.player.sendMessage("해당 위치의 상자 유형을 ${selecting.type.rating}(으)로 설정했습니다!")
        }
        else -> return EventResult.FAIL
    }
    return EventResult.SET_CHEST_DATA
}
