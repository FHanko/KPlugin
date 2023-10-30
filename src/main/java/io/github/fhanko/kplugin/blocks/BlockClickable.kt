package io.github.fhanko.kplugin.blocks

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

interface BlockClickable: Listener, BlockComparable {
    fun leftClick(e: PlayerInteractEvent) { }
    fun rightClick(e: PlayerInteractEvent) { }
    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (compareBlockId(e.clickedBlock)) {
            if (e.action.isLeftClick) leftClick(e)
            if (e.action.isRightClick) rightClick(e)
        }
    }
}