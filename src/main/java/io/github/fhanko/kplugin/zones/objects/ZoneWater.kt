package io.github.fhanko.kplugin.zones.objects

import io.github.fhanko.kplugin.util.Schedulable
import io.github.fhanko.kplugin.util.random
import io.github.fhanko.kplugin.zones.Zone
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.data.type.Farmland

class ZoneWater(start: Location, end: Location): Zone(start, end), Schedulable {
    private val scheduleKey = "${start}${end}"

    override fun create() {
        super.create()
        scheduleRepeat(scheduleKey, 20000, ::waterBlocks)
    }

    private fun waterBlocks(params: List<Any>) {
        blocks.forEach {
            if (random.nextDouble() > 0.2) return@forEach
            if (it.type == Material.DIRT || it.type == Material.GRASS_BLOCK) it.type = Material.FARMLAND
            if (it.type == Material.FARMLAND) {
                it.blockData.apply { this as Farmland; this.moisture = this.maximumMoisture; it.blockData = this }
                it.world.spawnParticle(Particle.DRIP_WATER, it.location.toCenterLocation().add(0.0, 0.6, 0.0), 1)
            }
        }
    }

    override fun remove() {
        cancelSchedule(scheduleKey)
    }
}