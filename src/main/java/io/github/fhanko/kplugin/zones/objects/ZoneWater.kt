package io.github.fhanko.kplugin.zones.objects

import io.github.fhanko.kplugin.util.Schedulable
import io.github.fhanko.kplugin.zones.Zone
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.type.Farmland

class ZoneWater(start: Location, end: Location): Zone(start, end), Schedulable {
    private val scheduleKey = "${start}${end}"
    override fun create() {
        super.create()
        scheduleRepeat(scheduleKey, 5000, ::waterBlocks)
    }

    private fun waterBlocks(params: List<Any>) {
        blocks.forEach {
            if (it.type == Material.FARMLAND) {
                it.blockData.apply { this as Farmland; this.moisture = this.maximumMoisture; it.blockData = this }
            }
        }
    }

    override fun remove() {
        cancelSchedule(scheduleKey)
    }
}