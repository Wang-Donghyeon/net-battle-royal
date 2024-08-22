package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.github.monun.tap.fake.FakeEntity
import io.github.monun.tap.fake.FakeEntityServer
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockState
import org.bukkit.block.Chest
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.ItemStack
import kotlin.math.ln
import kotlin.random.Random

data class ChestData(val location: Location, var type: ChestType)

enum class ChestType(val rating: String, val color: TextColor) {
    NORMAL("평범", NamedTextColor.WHITE),
    RARE("희귀", NamedTextColor.GREEN),
    EPIC("에픽", NamedTextColor.DARK_PURPLE)
}

data class RoyalChest(val game: Game, val chestData: ChestData, val table: ChestLootTable) {

    private var isOpened: Boolean
    private val fakeEntity: FakeEntity<ArmorStand>
    private lateinit var blockOpenState: BlockState

    val location
        get() = chestData.location

    private val region: Region?
        get() {
            val regions = game.regions.filter { game.isInRegion(it, location) }
            return regions.maxByOrNull { it.priority }
        }

    init {
        location.block.type = Material.CHEST
        (location.block.blockData as org.bukkit.block.data.type.Chest).apply {
            facing = this.faces.random()
        }

        fakeEntity = plugin.fakeEntityManager.spawnEntity(location.clone().add(0.5, -0.5, 0.5), ArmorStand::class.java).apply {
            updateMetadata {
                isInvisible = true
                isCustomNameVisible = true
                customName(text(chestData.type.rating).color(chestData.type.color).append(text(" 상자").color(NamedTextColor.WHITE)))
            }
        }
        isOpened = false

        setContent()
    }

    private fun setContent() {
        val items = table.generateItems(region)
        (location.block as Chest).inventory.contents = items
    }

    fun open() {
        if (isOpened) return
        isOpened = true

        fakeEntity.updateMetadata {
            customName(text("열린 상자").color(NamedTextColor.WHITE))
        }

        blockOpenState = location.block.state as Chest
    }

    fun update() {
        if (location.block.type != Material.CHEST) {
            remove()
            return
        }
        if (isOpened) blockOpenState.update(true, false)
    }

    fun remove() {
        game.chests.remove(this)
        fakeEntity.remove()
    }
}

data class ChestLootTable(val stacks: List<IntRange>, val loots: List<ChestItemData>) {
    fun generateItems(region: Region?): Array<ItemStack?> {
        val items = arrayOfNulls<ItemStack>(27)

        stacks.forEach { range ->
            repeat(range.random()) {
                // 'minByOrNull { ~ }' 이 구문이 있으면 가중치 확률이 정확하게 동작한다는데,, 정확한 동작 방식은 잘 모르겠음
                val loot = loots.filter { it.stackRange == range && (it.regions.isEmpty() || it.regions.contains(region)) }.minByOrNull { -ln(Random.nextDouble()) / it.weight }
                if (loot != null) {
                    val item = loot.item
                    val amount = loot.amount.random()
                    repeat(amount) {
                        val index = IntRange(0, 26).filter { items[it] == null || items[it]!!.isSimilar(item) }.random()
                        if (items[index] == null) items[index] = item
                        else items[index]!!.amount ++
                    }
                }
            }
        }

        return items
    }
}

data class ChestItemData(val item: ItemStack, val stackRange: IntRange, val weight: Double, val amount: IntRange, val regions: List<Region> = listOf())