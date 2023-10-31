package io.github.fhanko.kplugin.zones.objects

import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.zones.ZoneCube
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class ZoneHeal(start: Location, end: Location): ZoneCube(start, end) {

    private val tidMap = mutableMapOf<Player, Int>()
    override fun enter(p: Player) {
        val tid = KPlugin.instance.server.scheduler.scheduleSyncRepeatingTask(KPlugin.instance,
            {
                if (p.health + 1 <= p.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue) p.health += 1
                if (p.foodLevel + 1 <= 20) p.foodLevel += 1
            }
            , 0, 20)
        tidMap[p] = tid
    }

    override fun leave(p: Player) {
        tidMap[p] ?: return
        KPlugin.instance.server.scheduler.cancelTask(tidMap[p]!!);
    }

    override val borderColor: Color = Color.TEAL
}