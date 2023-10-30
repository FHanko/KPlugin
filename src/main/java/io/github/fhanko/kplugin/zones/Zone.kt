package io.github.fhanko.kplugin.zones

import io.github.fhanko.kplugin.util.toward
import org.bukkit.Chunk
import org.bukkit.Location
import kotlin.math.max
import kotlin.math.min

sealed class Zone {
    /**
     * Returns true if that location is in the Zone
     */
    abstract fun isIn(that: Location): Boolean

    /**
     * Returns all chunks that contain blocks of the Zone
     */
    abstract fun chunkList(): List<Chunk>
}

data class ZoneCube(var start: Location, var end: Location): Zone() {
    override fun isIn(that: Location) =
        (that.x > min(start.x, end.x) && that.z > min(start.z, end.z) && that.y > min(start.y, end.y) &&
                that.x < max(start.x, end.x) && that.z < max(start.z, end.z) && that.y < max(start.y, end.y))

    override fun chunkList(): List<Chunk> {
        val set = mutableSetOf<Chunk>()
        // Block coordinate shr 4 is the chunk coordinate
        for (x in (start.x.toInt() shr 4) toward (end.x.toInt() shr 4)) {
            for (z in (start.z.toInt() shr 4) toward (end.z.toInt() shr 4)) {
                set.add(start.world.getChunkAt(x, z))
            }
        }
        return set.toList()
    }
}

data class ZoneCylinder(var center: Location, var radius: Int, var height: Int): Zone() {
    override fun isIn(that: Location) =
        (that.let { it.y = 0.0; it }.distance(this.let { it.center.y = 0.0; it.center }) < radius) && (that.y - center.y < height)

    override fun chunkList(): List<Chunk> {
        throw NotImplementedError()
    }
}

/**
 * Map Zones to their holding Chunks to allow efficient collision detection via Chunk key.
 */
object ZoneMap {
    private val grid = mutableMapOf<Chunk, MutableList<Zone>>()

    fun addZone(zone: Zone) {
        val chunkList = zone.chunkList()
        chunkList.forEach {
            if (grid.containsKey(it)) {
                grid[it]!!.add(zone)
            } else {
                grid[it] = mutableListOf(zone)
            }
        }
    }

    fun removeZone(zone: Zone) {
        val chunkList = zone.chunkList()
        chunkList.forEach {
            grid[it]!!.remove(zone)
        }
    }

    fun getZones(chunk: Chunk): List<Zone>? {
        return grid[chunk]
    }
}
