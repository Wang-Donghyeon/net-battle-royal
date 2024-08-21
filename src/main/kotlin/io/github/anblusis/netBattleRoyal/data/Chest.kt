package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.Game
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.math.ln
import kotlin.random.Random

data class ChestData(val location: Location, var type: ChestType)

enum class ChestType(val rating: String) {
    NORMAL("평범"),
    RARE("희귀"),
    EPIC("에픽")
}

data class RoyalChest(val game: Game, val chestData: ChestData, val items: MutableList<ItemStack>) {
    constructor(game: Game, chestData: ChestData) : this(game, chestData, mutableListOf())

    private var isOpened = false

    val location
        get() = chestData.location

    fun generateItems() {
        items.clear()
    }

    fun update() {
        if (location.block.type != Material.CHEST) {
            remove()
            return
        }
    }

    fun remove() {
        game.chests.remove(this)
    }
}

data class ChestLootTable(val stacks: List<IntRange>, val roots: List<ChestItemData>) {
    fun generateItems(): List<ItemStack> {
        val items = arrayOfNulls<ItemStack>(27)
        val invenStat = Array(27) { it }

        stacks.forEach { range ->
            repeat(range.random()) { _ ->
                // 'minByOrNull { ~ }' 이 구문이 있으면 가중치 확률이 정확하게 동작한다는데,, 정확한 동작 방식은 잘 모르겠음
                val root = roots.filter { it.stackRange == range }.minByOrNull { -ln(Random.nextDouble()) / it.weight }
                if (root != null) {
                    val item = root.item
                    val amount = root.amount.random()
                    repeat(amount) { i ->
                        val index = invenStat.filter { items[i] == null || items[i]!!.isSimilar(item) }.random()
                        if (items[index] == null) {
                            items[index] = item
                        } else {
                            items[index].amount
                        }
                    }
                }
            }
        }
        return items
    }
}

data class ChestItemData(val item: ItemStack, val stackRange: IntRange, val weight: Double, val amount: IntRange)
