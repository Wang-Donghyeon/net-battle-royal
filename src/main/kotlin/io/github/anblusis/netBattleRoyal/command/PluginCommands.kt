package io.github.anblusis.netBattleRoyal.command

import io.github.monun.kommand.kommand
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

class PluginCommand(
    private val plugin: JavaPlugin
) {
    fun startCommands()  {
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
        val chestsData = mutableListOf<Map<String, Any>>() // 임시
        if (chestsData.isEmpty()) {
            print("저장된 블록 데이터가 없습니다.")
            return
        }

        val codeSnippet = StringBuilder("val chestsData = mutableListOf<Map<String, Any>>(\n")

        chestsData.forEach { chestData ->
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

        print(codeSnippet.toString())
    }
}

