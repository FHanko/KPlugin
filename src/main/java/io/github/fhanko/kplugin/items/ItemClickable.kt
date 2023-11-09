package io.github.fhanko.kplugin.items

import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

/**
 * Implementable for subclasses of ItemBase to override left- and right click functions.
 */
interface ItemClickable {
    fun leftClick(e: PlayerInteractEvent) { }
    fun rightClick(e: PlayerInteractEvent) { }

    fun onInteract(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (e.action.isLeftClick) leftClick(e)
        else if (e.action.isRightClick) rightClick(e)
    }
}