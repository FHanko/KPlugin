package io.github.fhanko.kplugin.zones

import io.github.fhanko.kplugin.util.FilePersistable
import org.bukkit.Chunk

private const val SAVE_FILE = "Zones.data"

/**
 * Map Zones to their holding Chunks to allow efficient collision detection via Chunk key.
 */
object ZoneChunkMap: FilePersistable<HashSet<Zone>> {
    private val grid = mutableMapOf<Pair<Int, Int>, HashSet<Zone>>()

    init {
        load(SAVE_FILE)?.forEach { addZone(it, false) }
    }

    fun addZone(zone: Zone, save: Boolean = true) {
        val chunkList = zone.chunkList()
        chunkList.forEach {
            if (grid.containsKey(it.x to it.z)) {
                grid[it.x to it.z]!!.add(zone)
            } else {
                grid[it.x to it.z] = mutableSetOf(zone).toHashSet()
            }
        }
        if (save) save(SAVE_FILE, getZones())
    }

    fun removeZone(zone: Zone) {
        val chunkList = zone.chunkList()
        chunkList.forEach {
            grid[it.x to it.z]!!.remove(zone)
        }
    }

    fun getZones(chunk: Chunk): HashSet<Zone>? {
        return grid[chunk.x to chunk.z]
    }

    /**
     * Returns all Zones in a chunk radius.
     */
    fun getRadiusZones(chunk: Chunk, radius: Int): HashSet<Zone> {
        val ret = HashSet<Zone>()
        for(x in chunk.x - radius .. chunk.x + radius)
            for(z in chunk.z - radius .. chunk.z + radius)
                grid[x to z]?.let { ret.addAll(it) }
        return ret
    }

    private fun getZones(): HashSet<Zone> {
        val ret = mutableSetOf<Zone>().toHashSet()
        grid.forEach { (_, z) -> z.forEach { ret.add(it) } }
        return ret
    }
}
