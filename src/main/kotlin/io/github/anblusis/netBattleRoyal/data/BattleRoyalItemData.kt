package io.github.anblusis.netBattleRoyal.data

import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class BattleRoyalItemData(val itemStack : ItemStack) {
    MAGIC_STICK(ItemStack(Material.STICK).apply {
        itemMeta = itemMeta.apply {
            displayName(
                text().color(NamedTextColor.DARK_PURPLE).content("마술봉").decoration(TextDecoration.ITALIC, false).build()
            )
        }
    }),
    MAGIC_SWORD(ItemStack(Material.DIAMOND_SWORD).apply {
        itemMeta = itemMeta.apply {
            displayName(
                text().color(NamedTextColor.AQUA).content("응애검").decoration(TextDecoration.ITALIC, false).build()
            )
        }
    })
}