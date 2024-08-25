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
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object InvManager {
    private val recipeMenuItem = ItemStack(Material.CRAFTING_TABLE).apply {
        itemMeta = itemMeta.apply {
            displayName(text("조합법").decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false))
        }
    }
    private val chestMenuItem = ItemStack(Material.CHEST).apply {
        itemMeta = itemMeta.apply {
            displayName(text("상자").decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false))
        }
    }
    private val itemMenuItem = ItemStack(Material.ITEM_FRAME).apply {
        itemMeta = itemMeta.apply {
            displayName(text("아이템").decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false))
        }
    }
    private val previousPageItem = ItemStack(Material.ARROW).apply {
        itemMeta = itemMeta.apply {
            displayName(text("←").decoration(TextDecoration.ITALIC, false))
        }
    }
    private val nextPageItem = ItemStack(Material.ARROW).apply {
        itemMeta = itemMeta.apply {
            displayName(text("→").decoration(TextDecoration.ITALIC, false))
        }
    }
    private val upWheelItem = ItemStack(Material.CHAIN).apply {
        itemMeta = itemMeta.apply {
            displayName(text("↑").decoration(TextDecoration.ITALIC, false))
        }
    }
    private val downWheelItem = ItemStack(Material.CHAIN).apply {
        itemMeta = itemMeta.apply {
            displayName(text("↓").decoration(TextDecoration.ITALIC, false))
        }
    }
    private val returnItem = ItemStack(Material.BLACK_CANDLE).apply {
        itemMeta = itemMeta.apply {
            displayName(text("돌아가기").decoration(TextDecoration.ITALIC, false))
        }
    }
    private val nothing = ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
        itemMeta = itemMeta.apply {
            displayName(text(""))
            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    }
    
    fun createMainInv(game: Game): InvFrame = InvFX.frame(1, text("배틀로얄").decorate(TextDecoration.BOLD)) {
        item(1, 0, recipeMenuItem)
        item(4, 0, chestMenuItem)
        item(7, 0, itemMenuItem)

        onClick { x, _, event ->
            when (x) {
                1 -> {
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    (event.whoClicked as Player).openFrame(createRecipeInv(game))
                }
                4 -> {
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    (event.whoClicked as Player).openFrame(createChestMainInv(game))
                }
                7 -> {
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    (event.whoClicked as Player).openFrame(createItemInv(game))
                }
            }
        }
    }

    private fun createRecipeInv(game: Game, clickedRecipe: Recipe? = null): InvFrame = InvFX.frame(5, text("조합법").decorate(TextDecoration.BOLD)) {
        list(0, 0, if (clickedRecipe == null) 8 else 3, 3, true, { Recipe.values().toList() }) {
            transform {
                if (it == clickedRecipe) it.result.clone().apply {
                    addUnsafeEnchantment(Enchantment.DURABILITY, 1)
                    addItemFlags(ItemFlag.HIDE_ENCHANTS)
                } else it.result
            }
            onClickItem { _, _, (recipe, _), event ->
                (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                if (clickedRecipe == recipe) {
                    (event.whoClicked as Player).openFrame(createRecipeInv(game))
                } else {
                    (event.whoClicked as Player).openFrame(createRecipeInv(game, recipe))
                }
            }
        }.let { list ->
            slot(if (clickedRecipe == null) 3 else 0, 4) {
                item = previousPageItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    list.index--
                }
            }
            slot(if (clickedRecipe == null) 5 else 2, 4) {
                item = nextPageItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    list.index++
                }
            }
            slot(if (clickedRecipe == null) 4 else 1, 4) {
                item = returnItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    (event.whoClicked as Player).openFrame(game.mainInv)
                }
            }
        }

        if (clickedRecipe != null) {
            list(5, 1, 7, 3, true, { clickedRecipe.toMaterialShape() }) {
                transform { ItemStack(it ?: Material.AIR) }
            }

            pane(4, 0, 4, 4) {
                repeat(height) {
                    item(0, it, nothing)
                }
            }
            pane(5, 0, 7, 0) {
                repeat(width) {
                    item(it, 0, nothing)
                }
            }
            pane(5, 4, 7, 4) {
                repeat(width) {
                    item(it, 0, nothing)
                }
            }
            pane(8, 0, 8, 4) {
                repeat(height) {
                    item(0, it, nothing)
                }
            }
        }
    }

    private fun createChestMainInv(game: Game): InvFrame = InvFX.frame(3, text("상자").decorate(TextDecoration.BOLD)) {
        list(0, 0, 8, 1, true, { game.regions }) {
            transform {
                ItemStack(Material.BOOK).apply {
                    itemMeta = itemMeta.apply {
                        displayName(text(it.displayName).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false))
                    }
                }
            }
            onClickItem { _, _, (region, _), event ->
                (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                (event.whoClicked as Player).openFrame(createChestRegionInv(game, region, ChestType.NORMAL))
            }
        }.let { list ->
            slot(3, 2) {
                item = previousPageItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    list.index--
                }
            }
            slot(5, 2) {
                item = nextPageItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    list.index++
                }
            }
            slot(4, 2) {
                item = returnItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    (event.whoClicked as Player).openFrame(game.mainInv)
                }
            }
        }
    }

    private fun createChestRegionInv(game: Game, region: Region, type: ChestType): InvFrame = InvFX.frame(5, text(region.displayName).decorate(TextDecoration.BOLD)) {
        list(2, 0, 8, 3, true, { game.chestTables[type]!!.loots.filter { it.regions.isEmpty() || it.regions.contains(region.name) } }) {
            transform {
                it.item
            }
        }.let { list ->
            slot(4, 4) {
                item = previousPageItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    list.index--
                }
            }
            slot(6, 4) {
                item = nextPageItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    list.index++
                }
            }
            slot(5, 4) {
                item = returnItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    (event.whoClicked as Player).openFrame(createChestMainInv(game))
                }
            }
        }
        pane(1, 0, 1, 4) {
            repeat(height) {
                item(0, it, nothing)
            }
        }
        list(0, 1, 0, 3, true, { ChestType.values().toList() }) {
            transform {
                ItemStack(it.material).apply {
                    itemMeta = itemMeta.apply {
                        displayName(text(it.rating).color(it.color).append(text(" 상자")).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false))
                        lore(listOf(text("클릭하여 아이템 목록 확인").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)))
                    }
                    if (it == type) {
                        addUnsafeEnchantment(Enchantment.DURABILITY, 1)
                        addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    }
                }
            }
            onClickItem { _, _, (type, _), event ->
                (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                (event.whoClicked as Player).openFrame(createChestRegionInv(game, region, type))
            }
        }.let { list ->
            slot(0, 0) {
                item = upWheelItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    list.index--
                }
            }
            slot(0, 4) {
                item = downWheelItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    list.index++
                }
            }
        }
    }

    private fun createItemInv(game: Game): InvFrame = InvFX.frame(5, text("아이템 목록").decorate(TextDecoration.BOLD)) {
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
                        loot?.run {
                            if (regions.isEmpty()) {
                                explain.add(
                                    text(" - ${type.rating} 상자 (모든 지역)").decoration(
                                        TextDecoration.ITALIC,
                                        false
                                    ).color(NamedTextColor.GRAY)
                                )
                            } else {
                                val regions = regions.mapNotNull { name -> game.regions.find { region -> region.name == name } }
                                regions.forEach { region ->
                                    explain.add(
                                        text(" - ${type.rating} 상자 (${region.displayName})").decoration(
                                            TextDecoration.ITALIC,
                                            false
                                        ).color(NamedTextColor.GRAY)
                                    )
                                }
                            }
                        }
                    }
                }
                it.clone().apply {
                    itemMeta = itemMeta.apply {
                        lore(lore()?.plus(explain) ?: explain)
                    }
                }
            }
        }.let { list ->
            slot(3, 4) {
                item = previousPageItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    list.index--
                }
            }
            slot(5, 4) {
                item = nextPageItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    list.index++
                }
            }
            slot(4, 4) {
                item = returnItem
                onClick { event ->
                    (event.whoClicked as Player).playSound(event.whoClicked.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    (event.whoClicked as Player).openFrame(game.mainInv)
                }
            }
        }
    }
}