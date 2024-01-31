package io.github.fhanko.blocks

import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent
import io.github.fhanko.TexturedBlock
import io.github.fhanko.ZoneChunkMap
import io.github.fhanko.mm
import io.github.fhanko.zones.ZoneWater
import org.bukkit.Material
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.util.Vector

private val radius = Vector(4, 1, 4)
private const val TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY3OTliZmFhM2EyYzYzYWQ4NWRkMzc4ZTY2ZDU3ZDlhOTdhM2Y4NmQwZDlmNjgzYzQ5ODYzMmY0ZjVjIn19fQ=="
object Sprinkler: TexturedBlock(TEXTURE,6, Material.IRON_BLOCK, mm.deserialize("Sprinkler"), listOf(mm.deserialize("Will keep your soil moist"))) {
    override fun place(e: BlockPlaceEvent) {
        super.place(e)
        val bLoc = e.blockPlaced.location.subtract(radius) to e.blockPlaced.location.add(radius)
        ZoneChunkMap.addZone(ZoneWater(bLoc.first, bLoc.second), true)
    }
    override fun destroy(e: CustomBlockDataRemoveEvent) {
        super.destroy(e)
        val bLoc = e.block.location.subtract(radius) to e.block.location.add(radius)
        ZoneChunkMap.removeZone(ZoneChunkMap.fromBounds(bLoc.first, bLoc.second)!!, true)
    }
}