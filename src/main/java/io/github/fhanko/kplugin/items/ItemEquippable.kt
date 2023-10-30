package io.github.fhanko.kplugin.items

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent

interface ItemEquippable: Listener, ItemComparable {
    open fun equip(p: Player) {}
    open fun unequip(p: Player) {}
    @EventHandler
    fun onHeld(e: PlayerItemHeldEvent) {
        if (compareId(e.player.inventory.getItem(e.newSlot))) equip(e.player)
        if (compareId(e.player.inventory.getItem(e.previousSlot))) unequip(e.player)
    }

    @EventHandler
    fun onSlotChange(e: PlayerInventorySlotChangeEvent) {
        if (e.slot == e.player.inventory.heldItemSlot) {
            if (compareId(e.newItemStack)) equip(e.player)
            else if (compareId(e.oldItemStack)) unequip(e.player)
        }
    }
}