package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

enum class Recipe(recipeName: String, val result: ItemStack, private val shape: List<String>, private val ingredients: Map<Char, Material>) {
    MAGIC_SWORD("magic_sword",
        BattleRoyalItemData.MAGIC_SWORD.item,
        listOf(" A ", " B ", " A "),
        mapOf('A' to Material.DIAMOND, 'B' to Material.STICK)
    ),

    MAGIC_STICK("magic_stick",
    BattleRoyalItemData.MAGIC_STICK.item,
    listOf("AA ", " B ", " AA"),
    mapOf('A' to Material.BLAZE_POWDER, 'B' to Material.STICK)
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

    fun toMaterialShape(): List<Material?> = shape.flatMap { it.toCharArray().toList() }.map { ingredients[it] }

    fun addToServer() {
        val recipe = toBukkitRecipe()
        plugin.server.addRecipe(recipe)
    }

    fun removeFromServer() {
        plugin.server.removeRecipe(key)
    }
}