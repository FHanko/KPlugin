package io.github.fhanko.kplugin

import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.items.ItemBase
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

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
        if (e is KPluginInteractItemEvent || e is KPluginInteractBlockEvent) return
        if (ItemBase.isMarked(e.item)) {
            val event = KPluginInteractItemEvent(e.player, e.action, e.item, e.clickedBlock,
                                                 e.blockFace, e.hand, e.clickedPosition)
            Bukkit.getPluginManager().callEvent(event)
            e.setUseItemInHand(event.useItemInHand())
        }

        if (BlockBase.isMarked(e.clickedBlock)) {
            val event = KPluginInteractBlockEvent(e.player, e.action, e.item, e.clickedBlock,
                                                  e.blockFace, e.hand, e.clickedPosition)
            Bukkit.getPluginManager().callEvent(event)
            e.setUseInteractedBlock(event.useInteractedBlock())
        }
    }

    @EventHandler
    fun onCraft(e: PrepareItemCraftEvent) {
        if (e is KPluginPrepareItemCraftEvent) return
        if (e.inventory.any { ItemBase.isMarked(it) }) {
            val event = KPluginPrepareItemCraftEvent(e.inventory, e.view, e.isRepair)
            Bukkit.getPluginManager().callEvent(event)
        }
    }

    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {
        if (e is KPluginPlayerDropItemEvent) return
        if (ItemBase.isMarked(e.itemDrop.itemStack)) {
            val event = KPluginPlayerDropItemEvent(e.player, e.itemDrop)
            Bukkit.getPluginManager().callEvent(event)
            e.isCancelled = event.isCancelled
        }
    }

    @EventHandler
    fun onPickup(e: EntityPickupItemEvent) {
        if (e is KPluginPlayerPickupItemEvent) return
        if (e !is Player) return
        if (ItemBase.isMarked(e.item.itemStack)) {
            val event = KPluginPlayerPickupItemEvent(e.player!!, e.item, e.remaining)
            Bukkit.getPluginManager().callEvent(event)
            e.isCancelled = event.isCancelled
        }
    }

    @EventHandler
    fun onHeld(e: PlayerItemHeldEvent) {
        if (e is KPluginPlayerItemHeldEvent) return
        if (ItemBase.isMarked(e.player.inventory.getItem(e.newSlot)) || ItemBase.isMarked(e.player.inventory.getItem(e.previousSlot))) {
            val event = KPluginPlayerItemHeldEvent(e.player, e.previousSlot, e.newSlot)
            Bukkit.getPluginManager().callEvent(event)
            e.isCancelled = event.isCancelled
        }
    }

    @EventHandler
    fun onSlotChange(e: PlayerInventorySlotChangeEvent) {
        if (e is KPluginPlayerInventorySlotChangeEvent) return
        if (ItemBase.isMarked(e.oldItemStack) || ItemBase.isMarked(e.newItemStack)) {
            val event = KPluginPlayerInventorySlotChangeEvent(e.player, e.rawSlot, e.oldItemStack, e.newItemStack)
            Bukkit.getPluginManager().callEvent(event)
        }
    }
}

class KPluginInteractItemEvent(p: Player, a: Action, i: ItemStack?, b: Block?, bf: BlockFace, e: EquipmentSlot?, v: Vector?):
    PlayerInteractEvent(p, a, i, b, bf, e, v)
class KPluginInteractBlockEvent(p: Player, a: Action, i: ItemStack?, b: Block?, bf: BlockFace, e: EquipmentSlot?, v: Vector?):
    PlayerInteractEvent(p, a, i, b, bf, e, v)
class KPluginPrepareItemCraftEvent(c: CraftingInventory, i: InventoryView, ir: Boolean): PrepareItemCraftEvent(c, i, ir)
class KPluginPlayerDropItemEvent(p: Player, i: Item): PlayerDropItemEvent(p, i)
class KPluginPlayerPickupItemEvent(p: Player, i: Item, r: Int): EntityPickupItemEvent(p, i, r)
class KPluginPlayerItemHeldEvent(p: Player, pr: Int, c: Int): PlayerItemHeldEvent(p, pr, c)
class KPluginPlayerInventorySlotChangeEvent(p: Player, r: Int, o: ItemStack, n: ItemStack): PlayerInventorySlotChangeEvent(p, r, o, n)