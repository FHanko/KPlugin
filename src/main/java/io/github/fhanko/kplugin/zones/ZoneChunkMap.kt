package io.github.fhanko.kplugin.zones

import io.github.fhanko.kplugin.util.FilePersistable
import org.bukkit.Chunk

private const val SAVE_FILE = "Zones.data"

/**
 * Map Zones to their holding Chunks to allow efficient collision detection via Chunk key.
 */
object ZoneChunkMap: FilePersistable<HashSet<Zone>> {
    private val grid = mutableMapOf<Chunk, HashSet<Zone>>()

    init {
        load(SAVE_FILE)?.forEach { addZone(it, false) }
    }

    fun addZone(zone: Zone, save: Boolean = true) {
        val chunkList = zone.chunkList()
        chunkList.forEach {
            if (grid.containsKey(it)) {
                grid[it]!!.add(zone)
            } else {
                grid[it] = mutableSetOf(zone).toHashSet()
            }
        }
        if (save) save(SAVE_FILE, getZones())
    }

    fun removeZone(zone: Zone) {
        val chunkList = zone.chunkList()
        chunkList.forEach {
            grid[it]!!.remove(zone)
        }
    }

    fun getZones(chunk: Chunk): HashSet<Zone>? {
        return grid[chunk]
    }

    // HashSet is serializable unlike MutableSet
    private fun getZones(): HashSet<Zone> {
        val ret = mutableSetOf<Zone>().toHashSet()
        grid.forEach { (_, z) -> z.forEach { ret.add(it) } }
        return ret
    }
}
