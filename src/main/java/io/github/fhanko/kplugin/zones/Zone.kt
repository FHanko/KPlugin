package io.github.fhanko.kplugin.zones

import io.github.fhanko.kplugin.util.toward
import org.apache.commons.lang3.DoubleRange
import org.bukkit.Chunk
import org.bukkit.Location
import kotlin.math.max
import kotlin.math.min

abstract class Zone {
    /**
     * Returns true if that location is in the Zone
     */
    abstract fun isIn(that: Location): Boolean

    /**
     * All chunks that contain blocks of the Zone
     */
    abstract val chunkList: Set<Chunk>

    /**
     * Borders of the zone
     */
    abstract val borders: List<Location>
}

data class ZoneCube(var start: Location, var end: Location): Zone() {
    override val borders = mutableListOf<Location>()
    override val chunkList = mutableSetOf<Chunk>()
    init {
        for (x in 0 toward (end.x - start.x).toInt()) {
            borders.add(Location(start.world, start.x + x, start.y, start.z))
            borders.add(Location(start.world, start.x + x, end.y, start.z))
            borders.add(Location(start.world, start.x + x, end.y, end.z))
            borders.add(Location(start.world, start.x + x, start.y, end.z))
        }
        for (y in 0 toward (end.y - start.y).toInt()) {
            borders.add(Location(start.world, start.x, start.y + y, start.z))
            borders.add(Location(start.world, end.x, start.y + y, start.z))
            borders.add(Location(start.world, end.x, start.y + y, end.z))
            borders.add(Location(start.world, start.x, start.y + y, end.z))
        }
        for (z in 0 toward (end.z - start.z).toInt()) {
            borders.add(Location(start.world, start.x, start.y, start.z + z))
            borders.add(Location(start.world, end.x, start.y, start.z + z))
            borders.add(Location(start.world, end.x, end.y, start.z + z))
            borders.add(Location(start.world, start.x, end.y, start.z + z))
        }

        // Block coordinate shr 4 is the chunk coordinate
        for (x in (start.x.toInt() shr 4) toward (end.x.toInt() shr 4)) {
            for (z in (start.z.toInt() shr 4) toward (end.z.toInt() shr 4)) {
                chunkList.add(start.world.getChunkAt(x, z))
            }
        }
    }

    override fun isIn(that: Location) =
        (that.x > min(start.x, end.x) && that.z > min(start.z, end.z) && that.y > min(start.y, end.y) &&
                that.x < max(start.x, end.x) && that.z < max(start.z, end.z) && that.y < max(start.y, end.y))
}

/**
 * Map Zones to their holding Chunks to allow efficient collision detection via Chunk key.
 */
object ZoneMap {
    private val grid = mutableMapOf<Chunk, MutableList<Zone>>()

    fun addZone(zone: Zone) {
        val chunkList = zone.chunkList
        chunkList.forEach {
            if (grid.containsKey(it)) {
                grid[it]!!.add(zone)
            } else {
                grid[it] = mutableListOf(zone)
            }
        }
    }

    fun removeZone(zone: Zone) {
        val chunkList = zone.chunkList
        chunkList.forEach {
            grid[it]!!.remove(zone)
        }
    }

    fun getZones(chunk: Chunk): List<Zone>? {
        return grid[chunk]
    }
}
