package com.github.fhanko.zones

import com.github.fhanko.util.PluginInstance
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * Map Zones to their holding Chunks to allow efficient collision detection via Chunk key.
 */
object ZoneChunkMap {
    private val grid = mutableMapOf<Pair<Int, Int>, HashSet<Zone>>()

    /**
     * Adds [zone] to the chunk [grid]. Saves zone config if [persist] is true.
     */
    fun addZone(zone: Zone, persist: Boolean = true) {
        zone.addBlocks()
        zone.chunkList().forEach {
            if (grid.containsKey(it.x to it.z)) {
                grid[it.x to it.z]!!.add(zone)
            } else {
                grid[it.x to it.z] = mutableSetOf(zone).toHashSet()
            }
        }
        zone.create()
        if (persist) save()
    }

    /**
     * Removes [zone] from the chunk [grid].
     */
    fun removeZone(zone: Zone) {
        zone.remove()
        zone.chunkList().forEach {
            grid[it.x to it.z]!!.remove(zone)
        }
        save()
    }

    fun getZone(id: Int): Zone? = getZones().firstOrNull { it.id == id }

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
    fun getZones(chunk: Chunk): HashSet<Zone>? = grid[chunk.x to chunk.z]

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
    fun getZones(): HashSet<Zone> = grid.values.flatten().toHashSet()

    fun newId() = getZones().maxOfOrNull { it.id + 1 } ?: 0

    private val config = PluginInstance.instance.config
    fun save() {
        config.set("zones", getZones().toList())
        config.save(File(PluginInstance.instance.dataFolder,"zones.yml"))
    }

    fun load() {
        if (!File(PluginInstance.instance.dataFolder,"zones.yml").exists()) return
        config.load(File(PluginInstance.instance.dataFolder,"zones.yml"))
        if (!config.contains("zones")) return
        (config.get("zones") as ArrayList<Zone>).map { addZone(it, false) }
    }
}
