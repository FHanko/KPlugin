package io.github.fhanko.kplugin.zones

import io.github.fhanko.kplugin.util.Persistable
import org.bukkit.Chunk

/**
 * Map Zones to their holding Chunks to allow efficient collision detection via Chunk key.
 */
object ZoneMap: Persistable {
    private val grid = mutableMapOf<Chunk, MutableList<Zone>>()

    init {
        load<Set<Zone>>("Zones.data")?.forEach { addZone(it, false) }
    }

    fun addZone(zone: Zone, save: Boolean = true) {
        val chunkList = zone.chunkList()
        chunkList.forEach {
            if (grid.containsKey(it)) {
                grid[it]!!.add(zone)
            } else {
                grid[it] = mutableListOf(zone)
            }
        }
        if (save) save("Zones.data", getZones())
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

    private fun getZones(): Set<Zone> {
        val ret = mutableSetOf<Zone>()
        grid.forEach { (_, z) -> z.forEach { ret.add(it) } }
        return ret
    }
}
