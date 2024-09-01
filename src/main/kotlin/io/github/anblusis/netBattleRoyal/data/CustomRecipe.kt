package io.github.anblusis.netBattleRoyal.data

import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe

enum class CustomRecipe(
    recipeName: String,
    val result: ItemStack,
    private val shape: List<String>,
    private val ingredients: Map<Char, ItemStack>,
    private val counts: Map<Char, Int>,
    private val type: CustomRecipeType,
    val worlds: List<World>
) {

    MAGIC_STICK("magic_stick",
        BattleRoyalItemData.MAGIC_STICK.item,
        listOf("AA ", " B ", " AA"),
        mapOf('A' to ItemStack(Material.BLAZE_POWDER), 'B' to BattleRoyalItemData.MAGIC_STICK.item),
        mapOf(),
        CustomRecipeType.SHAPED,
        listOf()
    ),

    DMDDO("dmddo",
        ItemStack(Material.AMETHYST_BLOCK),
        listOf("AAA", "A  ", "   "),
        mapOf('A' to ItemStack(Material.AMETHYST_SHARD)),
        mapOf('A' to 4),
        CustomRecipeType.SHAPELESS,
        listOf()
    );

    private val key = NamespacedKey(plugin, recipeName)

    fun toBukkitRecipe(): Recipe {
        when (type) {
            CustomRecipeType.SHAPED -> {
                val recipe = ShapedRecipe(key, result)
                recipe.shape(*shape.toTypedArray())
                ingredients.forEach { (key, item) ->
                    recipe.setIngredient(key, item)
                }
                return recipe
            }
            CustomRecipeType.SHAPELESS -> {
                val recipe = org.bukkit.inventory.ShapelessRecipe(key, result)
                ingredients.forEach { (key, item) ->
                    recipe.addIngredient(counts[key]!!, item)
                }
                return recipe
            }
        }
    }

    fun toItemShape(): List<ItemStack?> = shape.flatMap { it.toCharArray().toList() }.map { ingredients[it] }

    fun addToServer() {
        val recipe = toBukkitRecipe()
        plugin.server.addRecipe(recipe)
    }

    fun removeFromServer() {
        plugin.server.removeRecipe(key)
    }
}

enum class CustomRecipeType {
    SHAPED, SHAPELESS
}