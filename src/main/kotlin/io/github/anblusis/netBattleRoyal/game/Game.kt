package io.github.anblusis.netBattleRoyal.game
import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldBorder
import org.bukkit.entity.Player
import org.bukkit.map.MapRenderer
import org.bukkit.util.Vector

class Game(
    worldName: String,
    val mode: Int,
    val players: MutableList<Player>
) {
    lateinit var chests: MutableList<RoyalChest>
    lateinit var regions: List<Region>
    lateinit var world: World
    lateinit var center: Location
    lateinit var worldBorder: WorldBorder
    private lateinit var chestLocations: List<ChestData>

    val worldBorderCenter
        get() = worldBorder.center

    val worldBorderSize
        get() = worldBorder.size

    private var chestCount: Int = 0

    init {
        registerWorld(worldName)
        registerPlayers(players)

        setChests()
    }

    private fun setChests() {
        chests = mutableListOf()
        val leftChestLocations = mutableListOf<ChestData>()
        leftChestLocations.addAll(chestLocations)
        leftChestLocations.shuffle()

        run {
            repeat(chestCount) {
                if (leftChestLocations.isEmpty()) return@run
                val chest = RoyalChest(this, leftChestLocations.first())
                chests.add(chest)
                leftChestLocations.removeFirst()
            }
        }
    }

    private fun registerWorld(worldName: String) {
        world = plugin.server.getWorld(worldName)!!
        worldBorder = world.worldBorder

        when (worldName) {
            "school" -> {
                center = Vector(0.0, 0.0, 0.0).toLocation(world)
                worldBorder.size = 1600.0
                chestCount = 100
                chestLocations = mutableListOf()
                regions = mutableListOf()
            }
        }

        worldBorder.center = center
    }

    private fun registerPlayers(players: MutableList<Player>) {
        players.forEach { player ->
            DataManager.getMarmotte(player).joinGame(this)
        }
    }
}
