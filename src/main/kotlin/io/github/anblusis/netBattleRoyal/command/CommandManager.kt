package io.github.anblusis.netBattleRoyal.command

import io.github.anblusis.netBattleRoyal.data.BattleRoyalItemData
import io.github.anblusis.netBattleRoyal.data.BattleRoyalMap
import io.github.anblusis.netBattleRoyal.data.ChestType
import io.github.anblusis.netBattleRoyal.data.DataManager
import io.github.anblusis.netBattleRoyal.game.Game
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import io.github.monun.invfx.openFrame
import io.github.monun.kommand.KommandArgument.Companion.dynamic
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import org.bukkit.Particle
import org.bukkit.entity.Player

object CommandManager {
    fun register()  {
        plugin.kommand {
            val battleRoyalItemArgument = dynamic { _, input ->
                BattleRoyalItemData.valueOf(input)
            }.apply {
                suggests {
                    suggest(BattleRoyalItemData.values().map { it.name })
                }
            }

            register("giveBattleRoyalItem") {
                requires { hasPermission(4) }
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
            register("battleRoyalMap", "brmap") {
                executes {
                    require(sender is Player)
                    makeBattleRoyalMap(sender as Player)
                }
                then("player" to player()) {
                    requires { hasPermission(4) }
                    executes {
                        makeBattleRoyalMap(it["player"])
                    }
                }
            }
            register("battleRoyalUI", "brui") {
                executes {
                    require(sender is Player)
                    showBattleRoyalUI(sender as Player)
                }
                then("player" to player()) {
                    requires { hasPermission(4) }
                    executes {
                        showBattleRoyalUI(it["player"])
                    }
                }
            }
            register("printChestsData") {
                requires { hasPermission(4) }
                executes {
                    printChestsData()
                }
            }
            register("spawnParticleAtChestsData") {
                requires { hasPermission(4) }
                executes {
                    spawnParticleAtChestsData()
                }
            }
            register("battleRoyal", "br") {
                requires { hasPermission(4) }
                then("createBattleRoyal") {
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
                then("removeBattleRoyal") {
                    then("player" to player()) {
                        executes {
                            removeBattleRoyal(it["player"])
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
        DataManager.getMarmotte(player).game?.let {
            player.inventory.addItem(BattleRoyalMap(it).item)
        }
    }

    private fun showBattleRoyalUI(player: Player) {
        DataManager.getMarmotte(player).game?.let {
            player.openFrame(it.mainInv)
        }
    }

    private fun printChestsData() {
        val codeSnippet = StringBuilder("chests = mutableListOf(\n")

        plugin.debugChestData.forEach { chestData ->
            val location = chestData.location
            val type = chestData.type

            codeSnippet.append(
                """
                ChestData(
                    Location(Bukkit.getWorld("${location.world}"), ${location.x}, ${location.y}, ${location.z}),
                    ChestType.$type
                ),
                """.trimIndent()
            ).append("\n")
        }

        codeSnippet.append(")")

        plugin.server.broadcast(text(codeSnippet.toString()))
    }

    private fun createBattleRoyal(map: String, mode: Int, players: List<Player>) {
        plugin.games.add(Game(map, mode, players.toMutableList()))
    }

    private fun removeBattleRoyal(player: Player) {
        DataManager.getMarmotte(player).game?.remove()
    }

    private fun spawnParticleAtChestsData() {
        plugin.debugChestData.forEach { chestData ->
            val location = chestData.location.clone().apply {
                x += 0.5
                y += 0.5
                z += 0.5
            }
            val dustOptions = when (chestData.type) {
                ChestType.NORMAL -> Particle.DustOptions(org.bukkit.Color.WHITE, 5.0f)
                ChestType.RARE -> Particle.DustOptions(org.bukkit.Color.YELLOW, 5.0f)
                ChestType.EPIC -> Particle.DustOptions(org.bukkit.Color.PURPLE, 5.0f)
            }
            location.world.spawnParticle(Particle.REDSTONE, location, 1, 0.0, 0.0, 0.0, 0.0, dustOptions)
        }
    }
}
