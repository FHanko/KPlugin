package com.github.fhanko.exampleplugin.zones

import com.github.fhanko.util.Schedulable
import com.github.fhanko.zones.Zone
import com.github.fhanko.zones.zonehandler.EnterHandler
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class ZoneHeal(id: Int, start: Location, end: Location): Zone(id, start, end), EnterHandler, Schedulable {
    constructor(map: Map<String, Any>): this(map["id"] as Int, map["start"] as Location, map["end"] as Location)
    override fun enter(p: Player) {
        scheduleRepeat(p.uniqueId.toString(), 20, ::healPlayer, p)
    }

    private fun healPlayer(params: List<Any>) {
        val p = params[0] as Player
        if (p.health + 1 <= p.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue) p.health += 1
        if (p.foodLevel + 1 <= 20) p.foodLevel += 1
    }

    override fun leave(p: Player) {
        scheduleCancel(p.uniqueId.toString())
    }
}