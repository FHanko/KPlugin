package io.github.fhanko.itemhandler

import io.github.fhanko.Initializable
import io.github.fhanko.ItemBase
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.event.player.PlayerItemHeldEvent

/**
 * Event listener for item handler
 *
 * Addresses event efficiency concerns by handling them once and firing handler functions
 * instead of firing all events on all items.
 */
object ItemListener: Listener, Initializable {
    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        ItemBase.get(e.item)?.also { if (it is ClickHandler) it.onInteract(e) }
    }

    @EventHandler
    fun onCraft(e: PrepareItemCraftEvent) {
        e.inventory.matrix.forEach { ing ->
            ItemBase.get(ing)?.also { if (it is CraftHandler) it.craftComponent(e) }
        }
        ItemBase.get(e.inventory.result)?.also { if (it is CraftHandler) it.craftResult(e) }
    }

    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {
        ItemBase.get(e.itemDrop.itemStack)?.also { if (it is DropHandler) it.drop(e) }
    }

    @EventHandler
    fun onPickup(e: EntityPickupItemEvent) {
        if (e !is Player) return
        ItemBase.get(e.item.itemStack)?.also { if (it is DropHandler) it.pickup(e) }
    }

    @EventHandler
    fun onHeld(e: PlayerItemHeldEvent) {
        val baseNew = ItemBase.get(e.player.inventory.getItem(e.newSlot))
        val baseOld = ItemBase.get(e.player.inventory.getItem(e.previousSlot))
        // Don't equip a swap to the same item
        if (baseNew != null && baseOld != null && baseNew.id == baseOld.id) return
        baseNew?.also { if (it is EquipHandler) it.equip(e.player, EquipHandler.EquipType.Hand) }
        baseOld?.also { if (it is EquipHandler) it.unequip(e.player, EquipHandler.EquipType.Hand) }
    }

    @EventHandler
    fun onInventoryOpen(e: InventoryOpenEvent) {
        val base = ItemBase.get(e.player.inventory.getItem(e.player.inventory.heldItemSlot))
        base?.also { if (it is EquipHandler) it.unequip(e.player as Player, EquipHandler.EquipType.Hand) }
    }

    @EventHandler
    fun onSlotChange(e: PlayerInventorySlotChangeEvent) {
        val baseNew = ItemBase.get(e.newItemStack)
        val baseOld = ItemBase.get(e.oldItemStack)
        // Don't equip a swap to the same item
        if (baseNew != null && baseOld != null && baseNew.id == baseOld.id) return
        if (baseNew is EquipHandler) baseNew.also {
            if (e.slot == e.player.inventory.heldItemSlot) baseNew.equip(e.player, EquipHandler.EquipType.Hand)
            else if (e.slot == baseNew.armourSlot().slot) baseNew.equip(e.player, EquipHandler.EquipType.Armour)
        }
        if (baseOld is EquipHandler) baseOld.also {
            if (e.slot == e.player.inventory.heldItemSlot) baseOld.unequip(e.player, EquipHandler.EquipType.Hand)
            else if (e.slot == baseOld.armourSlot().slot) baseOld.unequip(e.player, EquipHandler.EquipType.Armour)
        }
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val clickedBase = ItemBase.get(e.currentItem)
        val droppedBase = ItemBase.get(e.cursor)
        if (clickedBase is SlotHandler) clickedBase.slotClicked(e)
        if (droppedBase is SlotHandler) droppedBase.slotDropped(e)
    }

    @EventHandler
    fun onItemDamage(e: PlayerItemDamageEvent) {
        ItemBase.get(e.item)?.also { if (it is DamageHandler) it.damage(e) }
    }
}