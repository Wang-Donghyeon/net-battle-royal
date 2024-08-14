package io.github.anblusis.netBattleRoyal.command

import io.github.anblusis.netBattleRoyal.data.ChestsData.ChestsDataContents
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.plugin
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import org.bukkit.Location
import org.bukkit.Material

object CommandManager {
    fun register()  {
        plugin.kommand {
            register("printChestsData") {
                requires { hasPermission(4) }
                executes {
                    printChestsData()
                }
            }
        }
    }

    private fun printChestsData() {
        if (ChestsDataContents.isEmpty()) {
            plugin.server.broadcast(text("저장된 블록 데이터가 없습니다."))
            return
        }

        val codeSnippet = StringBuilder("val contents = mutableListOf<Map<String, Any>>(\n")

        ChestsDataContents.forEach { chestData ->
            val location = chestData["location"] as Location
            val type = chestData["type"] as Material

            codeSnippet.append(
                """
                mapOf(
                    "location" to Location(Bukkit.getWorld("${location.world}"), ${location.x}, ${location.y}, ${location.z}),
                    "type" to Material.$type,
                ),
                """.trimIndent()
            ).append("\n")
        }

        codeSnippet.append(")")

        plugin.server.broadcast(text(codeSnippet.toString()))
    }
}

