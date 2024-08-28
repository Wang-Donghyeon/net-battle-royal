package io.github.anblusis.netBattleRoyal.world

import io.github.anblusis.netBattleRoyal.data.*
import io.github.anblusis.netBattleRoyal.main.NetBattleRoyal.Companion.plugin
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

object City : WorldData {
    private val world = plugin.server.getWorld("world")

    override fun getCenter(): Location =
        Location(world, -291.50, 0.0, 821.50)

    override fun getWorldBorderSize(): Double =
        500.0

    override fun getChestCount(): Int =
        100

    override fun getChestLocations(): List<ChestData> =
        listOf(
            // 대저택
            ChestData(
                Location(world, -150.0, 29.0, 740.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -150.0, 32.0, 716.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -177.0, 30.0, 733.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -123.0, 30.0, 733.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -127.0, 39.0, 704.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -173.0, 39.0, 704.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -181.0, 22.0, 743.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -173.0, 22.0, 752.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -173.0, 22.0, 750.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -150.0, 22.0, 735.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -150.0, 22.0, 745.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -115.0, 22.0, 748.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -126.0, 22.0, 763.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -157.0, 22.0, 757.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -166.0, 22.0, 763.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -169.0, 22.0, 735.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -131.0, 22.0, 735.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -150.0, 22.0, 721.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -119.0, 22.0, 729.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -181.0, 22.0, 729.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -168.0, 24.0, 730.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -181.0, 22.0, 704.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -119.0, 22.0, 704.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -132.0, 24.0, 730.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -159.0, 31.0, 715.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -141.0, 31.0, 715.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -133.0, 30.0, 729.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -167.0, 30.0, 729.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -181.0, 30.0, 704.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -119.0, 30.0, 704.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -134.0, 37.0, 708.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -166.0, 37.0, 708.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -123.0, 37.0, 729.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -131.0, 37.0, 729.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -177.0, 37.0, 729.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -169.0, 37.0, 729.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -140.0, 35.0, 714.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -160.0, 35.0, 714.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -150.0, 38.0, 712.0),
                ChestType.NORMAL
            ),
            // 야구장
            ChestData(
                Location(world, -421.0, 58.0, 768.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -433.0, 58.0, 743.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -381.0, 58.0, 695.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -352.0, 58.0, 695.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -334.0, 46.0, 791.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -408.0, 21.0, 716.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -432.0, 29.0, 775.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -317.0, 35.0, 752.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -437.0, 21.0, 651.0),
                ChestType.RARE
            ),
            ChestData(
                Location(world, -379.0, 18.0, 747.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -402.0, 18.0, 724.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -352.0, 18.0, 716.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -410.0, 18.0, 774.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -352.0, 18.0, 774.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -379.0, 18.0, 774.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -352.0, 18.0, 747.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -324.0, 46.0, 743.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -373.0, 46.0, 800.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -412.0, 42.0, 792.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -328.0, 20.0, 796.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -310.0, 21.0, 780.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -340.0, 20.0, 808.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -339.0, 24.0, 786.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -340.0, 24.0, 787.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -331.0, 30.0, 796.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -319.0, 29.0, 783.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -322.0, 28.0, 751.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -326.0, 29.0, 717.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -373.0, 28.0, 807.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -317.0, 35.0, 784.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -318.0, 35.0, 724.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -320.0, 38.0, 752.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -340.0, 38.0, 800.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -334.0, 20.0, 721.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -316.0, 20.0, 716.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -327.0, 21.0, 741.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -327.0, 21.0, 771.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -326.0, 20.0, 798.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -357.0, 21.0, 799.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -404.0, 21.0, 794.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -400.0, 18.0, 824.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -423.0, 20.0, 793.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -398.0, 26.0, 799.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -415.0, 28.0, 797.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -423.0, 22.0, 803.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -376.0, 22.0, 803.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -434.0, 22.0, 787.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -417.0, 18.0, 789.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -421.0, 25.0, 765.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -411.0, 18.0, 747.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -379.0, 18.0, 716.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -434.0, 27.0, 748.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -430.0, 27.0, 726.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -416.0, 29.0, 720.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -406.0, 26.0, 706.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -387.0, 27.0, 694.0),
                ChestType.NORMAL
            ),
            ChestData(
                Location(world, -352.0, 27.0, 694.0),
                ChestType.NORMAL
            )
        )

    override fun getRegions(): List<Region> =
        listOf(
            Region("school", "학교", Location(world, -228.0, 0.0, 960.0), 108.0, 83.0, 2),
            Region("baseballStadium", "야구장", Location(world, -371.5, 0.0, 750.5), 129.0, 119.0, 2),
            Region("apartment", "아파트", Location(world, -246.0, 0.0, 758.0), 66.0, 104.0, 2),
            Region("mansion", "대저택", Location(world, -149.0, 0.0, 732.5), 70.0, 69.0, 2),
            Region("sandBeach", "모래사장", Location(world, -69.5, 0.0, 792.0), 23.0, 236.0, 1),
            Region("hospital", "병원", Location(world, -133.5, 0.0, 874.0), 49.0, 34.0, 2),
            Region("mart", "마트", Location(world, -92.0, 0.0, 954.0), 32.0, 64.0, 2),
            Region("residentialArea", "주택가", Location(world, -149.0, 0.0, 817.5), 80.0, 49.0, 2),
            Region("store", "상가", Location(world, -242.0, 0.0, 863.0), 74.0, 62.0, 2),
            Region("highBuilding", "고층 빌딩", Location(world, -315.5, 0.0, 867.0), 35.0, 62.0, 2),
        )

    override fun getChestTables(): HashMap<ChestType, ChestLootTable> =
        hashMapOf(
            ChestType.NORMAL to
                    ChestLootTable(
                        listOf(
                            6..8,
                            0..2
                        ),
                        listOf(
                            ChestItemData(ItemStack(Material.STICK), 4..12, 6..8, 10.0),
                            ChestItemData(ItemStack(Material.LEATHER), 4..8, 6..8, 8.0),
                            ChestItemData(ItemStack(Material.WHITE_WOOL), 12..15, 6..8, 14.0),
                            ChestItemData(ItemStack(Material.BREAD), 2..5, 6..8, 5.0),
                            ChestItemData(ItemStack(Material.APPLE), 2..5, 6..8, 5.0, listOf("baseballStadium")),
                            ChestItemData(ItemStack(Material.COOKED_BEEF), 2..5, 6..8, 5.0),
                            ChestItemData(ItemStack(Material.ARROW), 4..8, 6..8, 8.0)
                        )
                    ),
            ChestType.RARE to
                    ChestLootTable(
                        listOf(

                        ),
                        listOf(

                        )
                    ),
            ChestType.EPIC to
                    ChestLootTable(
                        listOf(

                        ),
                        listOf(

                        )
                    )
        )

    override fun getMapImage(): BufferedImage {
        val imageStream = javaClass.getResourceAsStream("/images/cityMap.png")
        return try {
            ImageIO.read(imageStream)
        } catch (e: IllegalArgumentException) {
            BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB)
        }
    }

    override fun getMapColors(): List<Byte> {
        val data: InputStream = javaClass.getResourceAsStream("/datas/city.txt")
            ?: throw IllegalArgumentException("Resource not found: /datas/city.txt")

        return data.bufferedReader().use { reader ->
            val content = reader.readText().trim()

            content.split(",")
                .map { it.trim().toByte() }
        }
    }
}