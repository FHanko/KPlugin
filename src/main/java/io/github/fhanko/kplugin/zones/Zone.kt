package io.github.fhanko.kplugin.zones

import io.github.fhanko.kplugin.util.toward
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.util.BoundingBox
import java.io.Serializable

abstract class Zone(val l1: Location, val l2: Location): Serializable {
    private val box: BoundingBox = BoundingBox.of(l1, l2)

    /**
    * Returns all chunks that contain blocks of the Zone
    */
    fun chunkList(): Set<Chunk> {
        val ret = mutableSetOf<Chunk>()
        // Block coordinate shr 4 is the chunk coordinate
        for (x in (box.minX.toInt() shr 4) toward (box.maxX.toInt() shr 4)) {
            for (z in (box.minZ.toInt() shr 4) toward (box.maxZ.toInt() shr 4)) {
                ret.add(l1.world.getChunkAt(x, z))
            }
        }
        return ret
    }

    /**
    * Returns true if that location is in the Zone
    */
    fun isIn(that: Location) = box.contains(that.x, that.y, that.z)
}