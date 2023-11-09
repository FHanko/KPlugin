package io.github.fhanko.kplugin.handler

import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

/**
 * Implementable for subclasses of ItemBase to override left- and right click functions.
 */
interface ClickHandler {
    fun leftClick(e: PlayerInteractEvent) { }
    fun rightClick(e: PlayerInteractEvent) { }
    fun onInteract(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (e.action.isLeftClick) leftClick(e)
        else if (e.action.isRightClick) rightClick(e)
    }

    fun leftClickBlock(e: PlayerInteractEvent) { }
    fun rightClickBlock(e: PlayerInteractEvent) { }
    fun onBlockInteract(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (e.action.isLeftClick) leftClickBlock(e)
        else if (e.action.isRightClick) rightClickBlock(e)
    }
}