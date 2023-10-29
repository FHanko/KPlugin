package io.github.fhanko.kplugin.zones

import org.bukkit.Chunk
import org.bukkit.Location

sealed class Zone {
    abstract fun isIn(that: Location): Boolean
}

data class ZoneCube(var start: Location, var end: Location): Zone() {
    override fun isIn(that: Location) =
        (that.x > start.x && that.z > start.z && that.y > start.y && that.x < end.x && that.z < end.z && that.y < end.y)
}
data class ZoneCylinder(var center: Location, var radius: Int, var height: Int): Zone() {
    override fun isIn(that: Location) =
        (that.let { it.y = 0.0; it }.distance(this.let { it.center.y = 0.0; it.center }) < radius) && (that.y - center.y < height)
}

object ZoneMap {
    private val grid = mutableMapOf<Chunk, MutableList<Zone>>()

    fun addZone(chunk: Chunk, zone: Zone) {
        if (grid.containsKey(chunk)) {
            grid[chunk]!!.add(zone)
        } else {
            grid[chunk] = mutableListOf(zone)
        }
    }

    fun getZones(chunk: Chunk): List<Zone>? {
        return grid[chunk];
    }
}
