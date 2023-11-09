package io.github.fhanko.kplugin

import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.blocks.BlockClickable
import io.github.fhanko.kplugin.items.*
import io.github.fhanko.kplugin.items.objects.TestItem.armourSlot
import io.github.fhanko.kplugin.util.HibernateUtil
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.*
import org.bukkit.event.world.WorldSaveEvent

/**
 * Addresses event efficiency concerns by handling them once and firing custom Events
 * instead of firing all events on all items.
 */
class VanillaEventListener: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        ItemBase.get(e.item)?.also { if (it is ItemClickable) it.onInteract(e) }
        BlockBase.get(e.clickedBlock)?.also { if (it is BlockClickable) it.onInteract(e) }
    }

    @EventHandler
    fun onCraft(e: PrepareItemCraftEvent) {
        e.inventory.forEach { ing ->
            ItemBase.get(ing)?.also { if (it is ItemCraftable) it.craft(e) }
        }
    }

    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {
        ItemBase.get(e.itemDrop.itemStack)?.also { if (it is ItemDroppable) it.drop(e) }
    }

    @EventHandler
    fun onPickup(e: EntityPickupItemEvent) {
        if (e !is Player) return
        ItemBase.get(e.item.itemStack)?.also { if (it is ItemDroppable) it.pickup(e) }
    }

    @EventHandler
    fun onHeld(e: PlayerItemHeldEvent) {
        val baseNew = ItemBase.get(e.player.inventory.getItem(e.newSlot))
        val baseOld = ItemBase.get(e.player.inventory.getItem(e.previousSlot))
        // Don't equip a swap to the same item
        if (baseNew != null && baseOld != null && baseNew.id == baseOld.id) return
        baseNew?.also { if (it is ItemEquippable) it.equip(e.player, ItemEquippable.EquipType.Hand) }
        baseOld?.also { if (it is ItemEquippable) it.unequip(e.player, ItemEquippable.EquipType.Hand) }
    }

    @EventHandler
    fun onSlotChange(e: PlayerInventorySlotChangeEvent) {
        val baseNew = ItemBase.get(e.newItemStack)
        val baseOld = ItemBase.get(e.oldItemStack)
        // Don't equip a swap to the same item
        if (baseNew != null && baseOld != null && baseNew.id == baseOld.id) return
        if (baseNew is ItemEquippable) baseNew.also {
            if (e.slot == e.player.inventory.heldItemSlot) baseNew.equip(e.player, ItemEquippable.EquipType.Hand)
            else if (e.slot == baseNew.armourSlot().slot) baseNew.equip(e.player, ItemEquippable.EquipType.Armour)
        }
        if (baseOld is ItemEquippable) baseOld.also {
            if (e.slot == e.player.inventory.heldItemSlot) baseOld.unequip(e.player, ItemEquippable.EquipType.Hand)
            else if (e.slot == baseOld.armourSlot().slot) baseOld.unequip(e.player, ItemEquippable.EquipType.Armour)
        }
    }

    @EventHandler
    fun onItemDamage(e: PlayerItemDamageEvent) {
        ItemBase.get(e.item)?.also { if (it is ItemDamageable) it.damage(e) }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onWorldSave(e: WorldSaveEvent) {
        if (e.world.environment != World.Environment.NORMAL) return
        HibernateUtil.postWorldSave(e)
    }
}