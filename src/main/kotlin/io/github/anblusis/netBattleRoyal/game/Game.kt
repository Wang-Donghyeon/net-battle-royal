package io.github.anblusis.netBattleRoyal.game
import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.data.DataManager.getMarmotte
import io.github.anblusis.netBattleRoyal.game.event.FightStart
import io.github.anblusis.netBattleRoyal.game.event.WorldBorderDecrease
import io.github.anblusis.netBattleRoyal.inv.InvManager
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.github.anblusis.netBattleRoyal.world.City
import io.github.monun.invfx.frame.InvFrame
import io.github.monun.tap.task.TickerTask
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldBorder
import org.bukkit.entity.Player
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.abs

class Game(
    worldName: String,
    val mode: Int,
    val players: MutableList<Player>
) {
    internal lateinit var chests: MutableList<RoyalChest>
    internal lateinit var regions: List<Region>
    internal lateinit var world: World
    internal lateinit var center: Location
    internal lateinit var chestTables: HashMap<ChestType, ChestLootTable>
    internal lateinit var mapImage: BufferedImage
    internal lateinit var worldBorder: WorldBorder
    internal lateinit var state: GameState
    private lateinit var chestLocations: List<ChestData>
    private val tickTask: TickerTask
    internal val mainInv: InvFrame

    private var chestCount: Int = 0
    internal val worldBorderDots: HashMap<Pair<Int, Int>, Color?> = hashMapOf()
    internal val chestRegionCount: HashMap<Region?, Int> = hashMapOf()
    internal val tasks: MutableList<GameTask> = mutableListOf()
    internal val maps: MutableList<BattleRoyalMap> = mutableListOf()

    val worldBorderCenter
        get() = worldBorder.center

    val worldBorderSize
        get() = worldBorder.size

    init {
        registerGame(worldName)
        registerPlayers(players)
        registerEvent()

        setChests()

        tickTask = plugin.ticker.runTaskTimer(this::onTick, 0L, 1L)
        mainInv = InvManager.createMainInv(this)
    }

    private fun onTick() {
        chests.forEach { it.update() }
        players.forEach {
            getMarmotte(it).update()
        }
        val timedOutTasks = mutableListOf<GameTask>()
        tasks.forEach { task ->
            if (task.tick == 0) timedOutTasks.add(task)
            else task.tick--
        }
        timedOutTasks.forEach { task ->
            if (task.canRestart) {
                task.tick = task.maxTick
            } else {
                tasks.remove(task)
            }

            task.run()
        }
    }

    private fun setChests() {
        chests = mutableListOf()
        val leftChestLocations = mutableListOf<ChestData>()
        leftChestLocations.addAll(chestLocations)
        leftChestLocations.shuffle()

        run {
            repeat(chestCount) {
                if (leftChestLocations.isEmpty()) return@run
                val chest = RoyalChest(this, leftChestLocations.first(), chestTables[leftChestLocations.first().type]!!)
                chests.add(chest)
                leftChestLocations.removeFirst()
            }
        }

        chests.forEach {
            chestRegionCount[it.region] = (chestRegionCount[it.region] ?: 0) + 1
        }
    }

    private fun registerGame(worldName: String) {
        state = GameState.READYING

        world = plugin.server.getWorld(worldName)!!
        worldBorder = world.worldBorder

        when (worldName) {
            "world" -> {
                center = City.getCenter()
                worldBorder.size = City.getWorldBorderSize()
                chestCount = City.getChestCount()
                chestLocations = City.getChestLocations()
                regions = City.getRegions()
                chestTables = City.getChestTables()
                mapImage = City.getMapImage()
            }
        }

        worldBorder.center = center
    }

    private fun registerPlayers(players: MutableList<Player>) {
        players.forEach { player ->
            getMarmotte(player).joinGame(this)
        }
    }

    private fun registerEvent() {
        tasks.add(GameTask(this, WorldBorderDecrease(this, 600), "월드보더 감소", 9000, 6000, 0, true))
        tasks.add(GameTask(this, FightStart(this, 3000), "무적 해제", 3000, 3000, 1, false))
    }

    fun isInRegion(region: Region, spot: Location): Boolean {
        if (spot.world != region.center.world) return false
        val dx = abs(spot.x - region.center.x)
        val dz = abs(spot.z - region.center.z)
        return dx <= region.width / 2 && dz <= region.height / 2
    }

    fun remove() {
        tickTask.cancel()
        val willRemovedChests = chests.toList()
        willRemovedChests.forEach { it.remove() }
        val willRemovedPlayers = players.toList()
        willRemovedPlayers.forEach { getMarmotte(it).leaveGame() }
        plugin.games.remove(this)
    }
}
