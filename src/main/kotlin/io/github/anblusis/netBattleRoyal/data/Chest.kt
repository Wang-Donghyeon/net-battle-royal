package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.entity.TextDisplay
import org.bukkit.inventory.ItemStack
import kotlin.math.ln
import kotlin.math.min
import kotlin.random.Random

data class ChestData(val location: Location, var type: ChestType)

enum class ChestType(val rating: String, val color: TextColor, val material: Material) {
    NORMAL("평범", NamedTextColor.WHITE, Material.WHITE_STAINED_GLASS),
    RARE("희귀", NamedTextColor.GREEN, Material.GREEN_STAINED_GLASS),
    EPIC("에픽", NamedTextColor.DARK_PURPLE, Material.PURPLE_STAINED_GLASS)
}

data class RoyalChest(val game: Game, val chestData: ChestData, val table: ChestLootTable) {

    var isOpened: Boolean
    private val entity: TextDisplay
    private var openTick: Int = 0

    val location
        get() = chestData.location

    val region: Region?
        get() {
            val regions = game.regions.filter { game.isInRegion(it, location) }
            return regions.maxByOrNull { it.priority }
        }

    init {
        location.block.type = Material.CHEST
        location.block.blockData = (location.block.blockData as org.bukkit.block.data.type.Chest).apply {
            facing = this.faces.random()
        }


        entity = location.world.spawn(location.clone().add(0.5, 1.5, 0.5), TextDisplay::class.java).apply {
            text(text("${chestData.type.rating} 상자").color(chestData.type.color))
        }
        plugin.server.broadcast(text("엔티티 위치: ${entity.location}"))
        isOpened = false

        setContent()
    }

    private fun setContent() {
        val items = table.generateItems(region)
        (location.block.state as Chest).inventory.contents = items
    }

    fun open() {
        if (isOpened) return
        isOpened = true

        entity.text(text("열린 상자 (${openTick / 20}초 전)").color(NamedTextColor.GRAY))

        plugin.server.broadcast(text("상자가 열렸습니다. 엔티티 위치: ${entity.location}"))
    }

    fun update() {
        if (location.block.type != Material.CHEST) {
            remove()
            return
        }
        if (isOpened) {
            openTick++
            entity.text(text("열린 상자 (${openTick / 20}초 전)").color(NamedTextColor.GRAY))
        }
        entity.location.direction = entity.location.clone().subtract(game.players.minByOrNull { it.location.distance(location) }?.location ?: return).toVector()
    }

    fun remove() {
        if (location.block.type == Material.CHEST) {
            location.block.type = Material.AIR
        }
        game.chests.remove(this)
        entity.remove()
    }
}

data class ChestLootTable(val stacks: List<IntRange>, val loots: List<ChestItemData>) {
    fun generateItems(region: Region?): Array<ItemStack?> {
        val results = arrayOfNulls<ItemStack>(27)
        val availableSlots = (0 until 27).toMutableList()
        val lootQueue = mutableListOf<ItemStack>()
        val lootItems = mutableListOf<ItemStack>()

        stacks.forEach { range ->
            repeat(range.random()) {
                // 가중치 랜덤
                val loot = loots.filter { it.stackRange == range && (it.regions.isEmpty() || it.regions.contains(region?.name)) }
                    .minByOrNull { -ln(Random.nextDouble()) / it.weight }

                if (loot != null) {
                    val amount = loot.amount.random()
                    val item = loot.item.clone().apply {
                        this.amount = amount
                    }
                    if (amount > 1) {
                        lootQueue.add(item)
                    } else {
                        lootItems.add(item)
                    }
                }
            }
        }

        // 큐에서 아이템을 꺼내어 슬롯에 배치
        while (availableSlots.size - lootQueue.size > 0 && lootQueue.isNotEmpty()) {
            val itemA = lootQueue.removeAt(Random.nextInt(lootQueue.size))
            val splitCount = Random.nextInt(itemA.amount / 2)
            val itemB = splitItem(itemA, splitCount)

            for (item in listOf(itemA, itemB)) {
                if (item.amount > 1 && Random.nextFloat() < 0.5) {
                    lootQueue.add(item)
                } else {
                    lootItems.add(item)
                }
            }
        }

        for (item in lootItems) {
            if (availableSlots.isEmpty()) break
            val slot = availableSlots.removeAt(Random.nextInt(availableSlots.size))
            if (item.amount > 0) {
                results[slot] = item
            }
        }

        return results
    }

    private fun splitItem(item: ItemStack, count: Int): ItemStack {
        val splitCount = min(count, item.amount)
        val other = item.clone()
        other.amount = splitCount
        item.amount -= splitCount
        return other
    }
}

data class ChestItemData(val item: ItemStack, val amount: IntRange, val stackRange: IntRange, val weight: Double, val regions: List<String> = listOf())