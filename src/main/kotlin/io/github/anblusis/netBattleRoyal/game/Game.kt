package io.github.anblusis.netBattleRoyal.game
import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.data.DataManager.getMarmotte
import io.github.anblusis.netBattleRoyal.inv.InvManager
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.github.monun.invfx.frame.InvFrame
import io.github.monun.tap.task.Ticker
import io.github.monun.tap.task.TickerTask
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldBorder
import org.bukkit.entity.Player
import org.bukkit.map.MapRenderer
import org.bukkit.util.Vector
import kotlin.math.abs

class Game(
    worldName: String,
    val mode: Int,
    private val players: MutableList<Player>
) {
    lateinit var chests: MutableList<RoyalChest>
    lateinit var regions: List<Region>
    lateinit var world: World
    lateinit var center: Location
    lateinit var chestTables: HashMap<ChestType, ChestLootTable>
    private lateinit var worldBorder: WorldBorder
    private lateinit var chestLocations: List<ChestData>
    private val task: TickerTask
    internal val mainInv: InvFrame

    private var chestCount: Int = 0
    val maps: MutableList<BattleRoyalMap> = mutableListOf()

    val worldBorderCenter
        get() = worldBorder.center

    val worldBorderSize
        get() = worldBorder.size

    init {
        registerGame(worldName)
        registerPlayers(players)

        setChests()

        task = plugin.ticker.runTaskTimer(this::onTick, 0L, 1L)
        mainInv = InvManager.createMainInv(this)
    }

    private fun onTick() {
        chests.forEach { it.update() }
    }

    private fun setChests() {
        chests = mutableListOf()
        val leftChestLocations = mutableListOf<ChestData>()
        leftChestLocations.addAll(chestLocations)
        leftChestLocations.shuffle()

        run {
            repeat(chestCount) {
                if (leftChestLocations.isEmpty()) return@run
                val chest = RoyalChest(this, leftChestLocations.first(), chestTables[chestLocations.first().type]!!)
                chests.add(chest)
                leftChestLocations.removeFirst()
            }
        }
    }

    private fun registerGame(worldName: String) {
        world = plugin.server.getWorld(worldName)!!
        worldBorder = world.worldBorder

        when (worldName) {
            "world" -> {
                center = Vector(-248.0, 0.0, 840.0).toLocation(world)
                worldBorder.size = 380.0
                chestCount = 100
                chestLocations = mutableListOf()
                regions = mutableListOf()
            }
        }

        worldBorder.center = center

        chestTables[ChestType.NORMAL] = ChestLootTable(listOf(), listOf())
        chestTables[ChestType.RARE] = ChestLootTable(listOf(), listOf())
        chestTables[ChestType.EPIC] = ChestLootTable(listOf(), listOf())
    }

    private fun registerPlayers(players: MutableList<Player>) {
        players.forEach { player ->
            getMarmotte(player).joinGame(this)
        }
    }

    fun isInRegion(region: Region, spot: Location): Boolean {
        if (spot.world != region.center.world) return false
        val dx = abs(spot.x - region.center.x)
        val dz = abs(spot.z - region.center.z)
        return dx <= region.width / 2 && dz <= region.height / 2
    }

    fun remove() {
        task.cancel()
        chests.forEach { it.remove() }
        players.forEach { getMarmotte(it).leaveGame() }
    }
}
