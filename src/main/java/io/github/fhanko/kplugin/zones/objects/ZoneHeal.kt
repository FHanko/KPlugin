package io.github.fhanko.kplugin.zones.objects

import io.github.fhanko.kplugin.util.Schedulable
import io.github.fhanko.kplugin.zones.Zone
import io.github.fhanko.kplugin.zones.handler.EnterHandler
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class ZoneHeal(start: Location, end: Location): Zone(start, end), EnterHandler, Schedulable {
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