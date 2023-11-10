package io.github.fhanko.kplugin.zones.objects

import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.util.Schedulable
import io.github.fhanko.kplugin.zones.ZoneCube
import io.github.fhanko.kplugin.zones.handler.EnterHandler
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class ZoneHeal(start: Location, end: Location): ZoneCube(start, end), EnterHandler, Schedulable {
    private val tidMap = mutableMapOf<Player, Int>()
    override fun enter(p: Player) {
        scheduleRepeat(p.uniqueId.toString(), 1000, ::healPlayer, p)
    }

    private fun healPlayer(params: List<Any>) {
        val p = params[0] as Player
        if (p.health + 1 <= p.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue) p.health += 1
        if (p.foodLevel + 1 <= 20) p.foodLevel += 1
    }

    override fun leave(p: Player) {
        cancelSchedule(p.uniqueId.toString())
    }

    override val borderColor: Color = Color.TEAL
}