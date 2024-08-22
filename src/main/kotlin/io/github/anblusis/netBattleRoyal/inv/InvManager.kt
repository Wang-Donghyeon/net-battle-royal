package io.github.anblusis.netBattleRoyal.inv

import io.github.anblusis.netBattleRoyal.data.ChestType
import io.github.anblusis.netBattleRoyal.data.Recipe
import io.github.anblusis.netBattleRoyal.data.Region
import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.github.monun.invfx.InvFX
import io.github.monun.invfx.frame.InvFrame
import io.github.monun.invfx.openFrame
import net.kyori.adventure.text.Component.text
import org.bukkit.Material
import org.bukkit.entity.Player
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
    private val nothing = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
    
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

    private fun createRecipeInv(game: Game): InvFrame = InvFX.frame(3, text("조합법")) {
        list(0, 0, 4, 1, true, { Recipe.values().toList() }) {
            transform { it.result }
            onClickItem { _, _, (item, _), _ ->
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
                (event.whoClicked as Player).openFrame(createChestInv(game, region, null))
            }
        }.let { list ->
            slot(0, 0) {
                item = previousPageItem
                onClick { list.index-- }
            }
            slot(8, 0) {
                item = nextPageItem
                onClick { list.index++ }
            }
        }
        item(1, 0, nothing)
        item(7, 0, nothing)
    }
}