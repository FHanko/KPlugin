package io.github.fhanko.kplugin

import com.jeff_media.customblockdata.CustomBlockData
import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.handler.*
import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.util.HibernateUtil
import io.github.fhanko.kplugin.util.copyPdc
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.*
import org.bukkit.event.world.WorldSaveEvent
import org.bukkit.inventory.ItemStack

/**
 * Addresses event efficiency concerns by handling them once and firing handler functions
 * instead of firing all events on all items.
 */
class VanillaEventListener: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        e.player.sendMessage("Welcome!")
    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        ItemBase.get(e.item)?.also { if (it is ClickHandler) it.onInteract(e) }
        BlockBase.get(e.clickedBlock)?.also { if (it is ClickHandler) it.onBlockInteract(e) }
    }

    @EventHandler
    fun onCraft(e: PrepareItemCraftEvent) {
        e.inventory.forEach { ing ->
            ItemBase.get(ing)?.also { if (it is CraftHandler) it.craft(e) }
        }
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
    fun onItemDamage(e: PlayerItemDamageEvent) {
        ItemBase.get(e.item)?.also { if (it is DamageHandler) it.damage(e) }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onWorldSave(e: WorldSaveEvent) {
        if (e.world.environment != World.Environment.NORMAL) return
        HibernateUtil.postWorldSave(e)
    }

    @EventHandler
    fun onPlace(e: BlockPlaceEvent) {
        val base = ItemBase.get(e.itemInHand)
        if (base is PlaceHandler) base.also {
            it.place(e)
            val blockData = CustomBlockData(e.blockPlaced, KPlugin.instance)
            copyPdc(e.itemInHand.itemMeta.persistentDataContainer, blockData)
        }
    }

    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        val base = BlockBase.get(e.block)
        if (base is PlaceHandler) base.also {
            it.destroy(e)
            e.isDropItems = false
            val i = ItemStack(it.item)
            i.editMeta { im -> copyPdc(CustomBlockData(e.block, KPlugin.instance), im.persistentDataContainer) }
            i.amount = 1
            e.block.world.dropItemNaturally(e.block.location, i)
        }
    }
}