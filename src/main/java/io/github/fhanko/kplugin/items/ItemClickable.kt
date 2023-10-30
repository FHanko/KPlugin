package io.github.fhanko.kplugin.items

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

interface ItemClickable: Listener, ItemComparable {
    fun leftClick(e: PlayerInteractEvent) { }
    fun rightClick(e: PlayerInteractEvent) { }
    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (compareId(e.item)) {
            if (e.action.isLeftClick) leftClick(e)
            if (e.action.isRightClick) rightClick(e)
        }
    }
}