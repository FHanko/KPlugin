package io.github.fhanko.kplugin.blocks

import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

/**
 * Implementable for subclasses of ItemBlock to override left- and right click functions.
 */
interface BlockClickable: Listener, BlockComparable {
    fun leftClick(e: PlayerInteractEvent) { }
    fun rightClick(e: PlayerInteractEvent) { }

    fun onInteract(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (e.action.isLeftClick) leftClick(e)
        else if (e.action.isRightClick) rightClick(e)
    }
}