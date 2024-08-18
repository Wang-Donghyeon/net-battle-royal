package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

enum class Recipe(recipeName: String, val result: ItemStack, val shape: List<String>, val ingredients: Map<Char, Material>) {
    MAGIC_SWORD("magic_sword",
        BattleRoyalItemData.MAGIC_SWORD.itemStack,
        listOf(" A ", " B ", " C "),
        mapOf('A' to Material.DIAMOND, 'B' to Material.STICK, 'C' to Material.DIAMOND)
    );

    private val key = NamespacedKey(plugin, recipeName)

    private fun toBukkitRecipe(): ShapedRecipe {
        val recipe = ShapedRecipe(key, result)
        recipe.shape(*shape.toTypedArray())
        ingredients.forEach { (key, material) ->
            recipe.setIngredient(key, material)
        }
        return recipe
    }

    fun addRecipe() {
        val recipe = toBukkitRecipe()
        plugin.server.addRecipe(recipe)
    }

    fun removeRecipe() {
        plugin.server.removeRecipe(key)
    }
}