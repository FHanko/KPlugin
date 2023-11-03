@file:Suppress("unused")
package io.github.fhanko.kplugin.items

import io.github.fhanko.kplugin.KPluginPlayerInventorySlotChangeEvent
import io.github.fhanko.kplugin.KPluginPlayerItemHeldEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

interface ItemEquippable: Listener, ItemComparable {
    enum class EquipType { Hand, Armour }
    fun equip(p: Player, e: EquipType) { }
    fun unequip(p: Player, e: EquipType) { }
    @EventHandler
    fun onHeld(e: KPluginPlayerItemHeldEvent) {
        if (compareId(e.player.inventory.getItem(e.newSlot))) equip(e.player, EquipType.Hand)
        if (compareId(e.player.inventory.getItem(e.previousSlot))) unequip(e.player, EquipType.Hand)
    }

    @EventHandler
    fun onSlotChange(e: KPluginPlayerInventorySlotChangeEvent) {
        if (compareId(e.newItemStack)) {
            if (e.slot == e.player.inventory.heldItemSlot) equip(e.player, EquipType.Hand)
            else if (e.slot == armourSlot().slot) equip(e.player, EquipType.Armour)
        } else if (compareId(e.oldItemStack)) {
            if (e.slot == e.player.inventory.heldItemSlot) unequip(e.player, EquipType.Hand)
            else if (e.slot == armourSlot().slot) unequip(e.player, EquipType.Armour)
        }
    }

    enum class ArmourSlot(var slot: Int) {
        None(-1), Boots(36), Legs(37), Body(38), Helmet(39), Shield(40)
    }

    fun armourSlot(): ArmourSlot = ArmourSlot.None
}