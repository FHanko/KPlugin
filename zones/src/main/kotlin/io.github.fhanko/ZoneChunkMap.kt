package io.github.fhanko

import org.bukkit.Chunk
import org.bukkit.Location

/**
 * Map Zones to their holding Chunks to allow efficient collision detection via Chunk key.
 */
object ZoneChunkMap: FilePersistable<HashSet<Zone>> {
    private val grid = mutableMapOf<Pair<Int, Int>, HashSet<Zone>>()
    override val saveFile = "Zones.data"

    init {
        load()?.forEach { addZone(it, false) }
    }

    /**
     * Adds [zone] to the chunk [grid]. Persists the [grid] if [save] is true.
     */
    fun addZone(zone: Zone, save: Boolean = true) {
        zone.create()
        val chunkList = zone.chunkList()
        chunkList.forEach {
            if (grid.containsKey(it.x to it.z)) {
                grid[it.x to it.z]!!.add(zone)
            } else {
                grid[it.x to it.z] = mutableSetOf(zone).toHashSet()
            }
        }
        if (save) save(getZones())
    }

    /**
     * Removes [zone] from the chunk [grid]. Persists the [grid] if [save] is true.
     */
    fun removeZone(zone: Zone, save: Boolean = true) {
        zone.remove()
        val chunkList = zone.chunkList()
        chunkList.forEach {
            grid[it.x to it.z]!!.remove(zone)
        }
        if (save) save(getZones())
    }

    /**
     * Returns a [Zone] in [grid] from its bounds [start] and [end].
     */
    fun fromBounds(start: Location, end: Location): Zone? {
        grid[(start.x.toInt() shr 4) to (start.z.toInt() shr 4)]?.forEach {
            if (it.start == start && it.end == end) return it
        }
        return null
    }

    /**
     * Returns all [Zone]s contained in [chunk].
     */
    fun getZones(chunk: Chunk): HashSet<Zone>? {
        return grid[chunk.x to chunk.z]
    }

    /**
     * Returns all [Zone]s contained in [chunk] and further chunks in [radius].
     */
    fun getRadiusZones(chunk: Chunk, radius: Int): HashSet<Zone> {
        val ret = HashSet<Zone>()
        for(x in chunk.x - radius .. chunk.x + radius)
            for(z in chunk.z - radius .. chunk.z + radius)
                grid[x to z]?.let { ret.addAll(it) }
        return ret
    }

    /**
     * Returns all [Zone]s from [grid].
     */
    private fun getZones(): HashSet<Zone> {
        val ret = mutableSetOf<Zone>().toHashSet()
        grid.forEach { (_, z) -> z.forEach { ret.add(it) } }
        return ret
    }
}
