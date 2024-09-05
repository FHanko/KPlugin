package io.github.fhanko.zones

import io.github.fhanko.Schedulable
import io.github.fhanko.Zone
import io.github.fhanko.random
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.data.type.Farmland

class ZoneWater(id: Int, start: Location, end: Location): Zone(id, start, end), Schedulable {
    constructor(map: Map<String, Any>): this(map["id"] as Int, map["start"] as Location, map["end"] as Location)
    private val scheduleKey = "${start}${end}"

    override fun create() {
        scheduleRepeat(scheduleKey, 400, ::waterBlocks)
    }

    private fun waterBlocks(params: List<Any>) {
        blocks.forEach {
            if (random.nextDouble() > 0.2) return@forEach
            if (it.type == Material.DIRT || it.type == Material.GRASS_BLOCK) it.type = Material.FARMLAND
            if (it.type == Material.FARMLAND) {
                it.blockData.apply { this as Farmland; this.moisture = this.maximumMoisture; it.blockData = this }
                it.world.spawnParticle(Particle.DRIPPING_WATER, it.location.toCenterLocation().add(0.0, 0.6, 0.0), 1)
            }
        }
    }

    override fun remove() {
        scheduleCancel(scheduleKey)
    }
}