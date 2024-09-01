package io.github.anblusis.netBattleRoyal.command

import io.github.anblusis.netBattleRoyal.data.BattleRoyalItemData
import io.github.anblusis.netBattleRoyal.data.BattleRoyalMap
import io.github.anblusis.netBattleRoyal.data.ChestType
import io.github.anblusis.netBattleRoyal.data.DataManager
import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.github.anblusis.netBattleRoyal.world.WorldData
import io.github.monun.invfx.openFrame
import io.github.monun.kommand.Kommand
import io.github.monun.kommand.KommandArgument.Companion.dynamic
import io.github.monun.kommand.PluginKommand
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object CommandManager {
    fun register(kommand: PluginKommand)  {
        kommand.register("netbattleroyal", "netbr") {
            val battleRoyalItemArgument = dynamic { _, input ->
                BattleRoyalItemData.valueOf(input)
            }.apply {
                suggests {
                    suggest(BattleRoyalItemData.values().map { it.name })
                }
            }
            val worldArgument = dynamic { _, input ->
                plugin.server.getWorld(input)
            }.apply {
                suggests {
                    suggest(plugin.server.worlds.map { it.name })
                }
            }

            then("givebattleroyalitem") {
                requires { isOp }
                then("players" to players()) {
                    then("item" to battleRoyalItemArgument) {
                        executes {
                            giveBattleRoyalItem(it["players"], it["item"], 1)
                        }
                        then("item" to int() ) {
                            executes {
                                giveBattleRoyalItem(it["players"], it["item"], it["count"])
                            }
                        }
                    }
                }
            }
            then("map") {
                executes {
                    require(sender is Player)
                    makeBattleRoyalMap(sender as Player)
                }
                then("player" to player()) {
                    requires { isOp }
                    executes {
                        makeBattleRoyalMap(it["player"])
                    }
                }
            }
            then("ui") {
                executes {
                    require(sender is Player)
                    showBattleRoyalUI(sender as Player)
                }
                then("player" to player()) {
                    requires { isOp }
                    executes {
                        showBattleRoyalUI(it["player"])
                    }
                }
            }
            then("printchestsdata") {
                requires { isOp }
                executes {
                    printChestsData()
                }
            }
            then("experesschestsdata") {
                requires { isOp }
                then("block") {
                    executes {
                        expressChestsData(true)
                    }
                }
                then("effect") {
                    executes {
                        expressChestsData(false)
                    }
                }
            }
            then("syncchestsdata") {
                requires { isOp }
                then("player") {
                    then("player" to player()) {
                        executes {
                            synchronizeChestsData(DataManager.getMarmotte(it["player"])?.game)
                        }
                    }
                }
                then("world") {
                    then("world" to worldArgument) {
                        executes {
                            synchronizeChestsData(plugin.games.find { game -> game.world == it["world"] })
                        }
                    }
                }
            }
            then("createbattleroyal") {
                then("map" to string()) {
                    then("mode" to int()) {
                        then("players" to players()) {
                            executes {
                                createBattleRoyal(it["map"], it["mode"], it["players"])
                            }
                        }
                    }
                }
            }
            then("removebattleroyal") {
                then("player") {
                    then("player" to player()) {
                        executes {
                            removeBattleRoyal(DataManager.getMarmotte(it["player"])?.game)
                        }
                    }
                }
                then("world") {
                    then("world" to worldArgument) {
                        executes {
                            removeBattleRoyal(plugin.games.find { game -> game.world == it["world"] })
                        }
                    }
                }
            }
        }
    }

    private fun giveBattleRoyalItem(players: List<Player>, item: BattleRoyalItemData, count: Int) {
        players.forEach { player ->
            player.inventory.addItem(item.item.apply {
                amount = count
            })
        }
    }

    private fun makeBattleRoyalMap(player: Player) {
        DataManager.getMarmotte(player)?.game?.let {
            player.inventory.addItem(BattleRoyalMap(it).item)
        }
    }

    private fun showBattleRoyalUI(player: Player) {
        DataManager.getMarmotte(player)?.game?.let {
            player.openFrame(it.mainInv)
        }
    }

    private fun printChestsData() {
        val codeSnippet = StringBuilder("listOf(\n")

        plugin.debugChestData.forEach { chestData ->
            val location = chestData.location
            val type = chestData.type

            codeSnippet.append(
                """
                ChestData(
                    Location(world, ${location.x}, ${location.y}, ${location.z}),
                    ChestType.$type
                ),
                """
            ).append("\n")
        }

        codeSnippet.append(")")

        plugin.server.broadcast(text(codeSnippet.toString()))
    }

    private fun createBattleRoyal(map: String, mode: Int, players: List<Player>) {
        plugin.games.add(Game(map, mode, players.toMutableList()))
    }

    private fun removeBattleRoyal(game: Game?) {
        game?.remove()
    }

    private fun expressChestsData(makeBlock: Boolean) {
        plugin.debugChestData.forEach { chestData ->
            val location = chestData.location.clone().apply {
                x += 0.5
                y += 0.5
                z += 0.5
            }

            if (makeBlock) {
                location.block.type = chestData.type.material
            } else {
                val dustOptions = when (chestData.type) {
                    ChestType.NORMAL -> Particle.DustOptions(org.bukkit.Color.WHITE, 5.0f)
                    ChestType.RARE -> Particle.DustOptions(org.bukkit.Color.GREEN, 5.0f)
                    ChestType.EPIC -> Particle.DustOptions(org.bukkit.Color.PURPLE, 5.0f)
                }
                location.world.spawnParticle(Particle.REDSTONE, location, 1, 0.0, 0.0, 0.0, 0.0, dustOptions)
            }
        }
    }

    private fun synchronizeChestsData(game: Game?) {
        plugin.debugChestData.clear()
        val chests = game?.chests ?: return

        chests.forEach { chest ->
            plugin.debugChestData.add(chest.chestData)
        }
    }
}
