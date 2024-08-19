package io.github.anblusis.netBattleRoyal.data

import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class BattleRoyalItemData(val item: ItemStack) {
    MAGIC_STICK(ItemStack(Material.STICK).apply {
        itemMeta = itemMeta.apply {
            displayName(
                text().color(NamedTextColor.DARK_PURPLE).content("debugStick")
                    .decoration(TextDecoration.ITALIC, false).build()
            )
        }
    }),
    MAGIC_SWORD(ItemStack(Material.DIAMOND_SWORD).apply {
        itemMeta = itemMeta.apply {
            displayName(
                text().color(NamedTextColor.AQUA).content("검").decoration(TextDecoration.ITALIC, false).build()
            )
        }
    }),
    BATTLE_ROYAL_MAP(ItemStack(Material.FILLED_MAP).apply {
        itemMeta = itemMeta.apply {
            displayName(
                text().color(NamedTextColor.GREEN).content("지도").decoration(TextDecoration.ITALIC, false).build()
            )
        }
    })
}