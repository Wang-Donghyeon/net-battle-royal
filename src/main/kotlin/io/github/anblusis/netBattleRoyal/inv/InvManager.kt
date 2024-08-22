package io.github.anblusis.netBattleRoyal.inv

import io.github.anblusis.netBattleRoyal.data.ChestType
import io.github.anblusis.netBattleRoyal.data.Recipe
import io.github.anblusis.netBattleRoyal.data.Region
import io.github.anblusis.netBattleRoyal.game.Game
import io.github.monun.invfx.InvFX
import io.github.monun.invfx.frame.InvFrame
import io.github.monun.invfx.openFrame
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object InvManager {
    private val recipeMenuItem = ItemStack(Material.CRAFTING_TABLE).apply {
        itemMeta = itemMeta.apply {
            displayName(text("조합법"))
        }
    }
    private val chestMenuItem = ItemStack(Material.CHEST).apply {
        itemMeta = itemMeta.apply {
            displayName(text("상자"))
        }
    }
    private val itemMenuItem = ItemStack(Material.ITEM_FRAME).apply {
        itemMeta = itemMeta.apply {
            displayName(text("아이템"))
        }
    }
    private val previousPageItem = ItemStack(Material.ARROW).apply {
        itemMeta = itemMeta.apply {
            displayName(text("←"))
        }
    }
    private val nextPageItem = ItemStack(Material.ARROW).apply {
        itemMeta = itemMeta.apply {
            displayName(text("→"))
        }
    }
    private val returnItem = ItemStack(Material.OAK_DOOR).apply {
        itemMeta = itemMeta.apply {
            displayName(text("돌아가기"))
        }
    }
    private val nothing = ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
        itemMeta = itemMeta.apply {
            displayName(text(""))
            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    }
    
    fun createMainInv(game: Game): InvFrame = InvFX.frame(1, text("BATTLE ROYALE")) {
        item(1, 0, recipeMenuItem)
        item(4, 0, chestMenuItem)
        item(7, 0, itemMenuItem)

        onClick { x, _, event ->
            when (x) {
                1 -> (event.whoClicked as Player).openFrame(createRecipeInv(game))
                4 -> (event.whoClicked as Player).openFrame(createChestMainInv(game))
                7 -> (event.whoClicked as Player).openFrame(createItemInv(game))
            }
        }
    }

    private fun createRecipeInv(game: Game): InvFrame = InvFX.frame(5, text("조합법")) {
        list(0, 0, 4, 1, true, { Recipe.values().toList() }) {
            transform { it.result }
            onClickItem { _, _, (item, itemStack), _ ->
                transform { it.result }
                itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1)
                itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                val shape = item.toMaterialShape().map { if (it != null) ItemStack(it) }
                this@frame.list(6, 0, 8, 2, true, { shape })
            }
        }.let { list ->
            slot(0, 2) {
                item = previousPageItem
                onClick { list.index-- }
            }
            slot(4, 2) {
                item = nextPageItem
                onClick { list.index++ }
            }
        }
        pane(5, 0, 5, 2) {
            repeat(height) {
                item(0, it, nothing)
            }
        }
        pane(0, 3, 8, 3) {
            repeat(width) {
                item(it, 0, nothing)
            }
        }
        slot(0, 4) {
            item = returnItem
            onClick { event -> (event.whoClicked as Player).openFrame(game.mainInv) }
        }
    }

    private fun createChestMainInv(game: Game): InvFrame = InvFX.frame(1, text("상자")) {
        list(2, 0, 6, 0, true, { game.regions }) {
            transform {
                ItemStack(Material.BOOK).apply {
                    itemMeta = itemMeta.apply {
                        displayName(text(it.displayName))
                    }
                }
            }
            onClickItem { _, _, (region, _), event ->
                (event.whoClicked as Player).openFrame(createChestRegionInv(game, region, ChestType.NORMAL))
            }
        }.let { list ->
            slot(2, 0) {
                item = previousPageItem
                onClick { list.index-- }
            }
            slot(8, 0) {
                item = nextPageItem
                onClick { list.index++ }
            }
        }
        item(1, 0, nothing)
        slot(0, 0) {
            item = returnItem
            onClick { event -> (event.whoClicked as Player).openFrame(game.mainInv) }
        }
    }

    private fun createChestRegionInv(game: Game, region: Region, type: ChestType): InvFrame = InvFX.frame(6, text(region.displayName)) {
        list(0, 0, 8, 2, true, { game.chestTables[type]!!.loots.filter { it.regions.isEmpty() || it.regions.contains(region) } }) {
            transform {
                it.item
            }
        }.let { list ->
            slot(0, 3) {
                item = previousPageItem
                onClick { list.index-- }
            }
            slot(8, 3) {
                item = nextPageItem
                onClick { list.index++ }
            }
        }
        pane(0, 4, 8, 4) {
            repeat(width) {
                item(it, 0, nothing)
            }
        }
        slot(0, 5) {
            item = returnItem
            onClick { event -> (event.whoClicked as Player).openFrame(createChestMainInv(game)) }
        }
        list(3, 5, 5, 5, true, { ChestType.values().toList() }) {
            transform {
                ItemStack(Material.CHEST).apply {
                    itemMeta = itemMeta.apply {
                        displayName(text(it.rating).color(it.color).append(text(" 상자")))
                        lore(listOf(text("클릭하여 아이템을 확인합니다.").decoration(TextDecoration.ITALIC, false)))
                    }
                }
            }
            onClickItem { _, _, (type, _), _ ->
                createChestRegionInv(game, region, type)
            }
        }.let { list ->
            slot(2, 5) {
                item = previousPageItem
                onClick { list.index-- }
            }
            slot(6, 5) {
                item = nextPageItem
                onClick { list.index++ }
            }
        }
    }

    private fun createItemInv(game: Game): InvFrame = InvFX.frame(6, text("아이템 목록")) {
        list(0, 0, 8, 3, true, {
            Recipe.values().map { it.result }.plus(game.chestTables.values.map { table -> table.loots.map { loot -> loot.item } }.flatten())
        }) {
            transform {
                val explain = mutableListOf<Component>()
                if (it in Recipe.values().map { recipe -> recipe.result }) {
                    explain.add(text("").decoration(TextDecoration.ITALIC, false))
                    explain.add(text("조합 아이템").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GOLD))
                } else {
                    explain.add(text("").decoration(TextDecoration.ITALIC, false))
                    explain.add(text("상자 아이템").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GOLD))

                    game.chestTables.forEach { (type, table) ->
                        val loot = table.loots.find { loot -> loot.item == it }
                        loot?.regions?.forEach { region ->
                            explain.add(text("-${region.displayName} ${type.rating} 상자").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY))
                        }
                    }
                }
                it.apply {
                    itemMeta = itemMeta.apply {
                        lore(lore()?.plus(explain) ?: explain)
                    }
                }
            }
        }.let { list ->
            slot(0, 3) {
                item = previousPageItem
                onClick { list.index-- }
            }
            slot(8, 3) {
                item = nextPageItem
                onClick { list.index++ }
            }
        }
        pane(1, 4, 8, 4) {
            repeat(width) {
                item(it, 0, nothing)
            }
        }
        slot(0, 5) {
            item = returnItem
            onClick { event -> (event.whoClicked as Player).openFrame(game.mainInv) }
        }
    }
}