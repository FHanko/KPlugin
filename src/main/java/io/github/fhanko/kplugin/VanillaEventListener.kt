package io.github.fhanko.kplugin

import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.util.KPluginEvent
import io.github.fhanko.kplugin.util.rem
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.*
import org.bukkit.event.world.WorldSaveEvent
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
        if (ItemBase.isMarked(e.item)) {
            val event = KPluginInteractItemEvent(e.player, e.action, e.item, e.clickedBlock,
                                                 e.blockFace, e.hand, e.interactionPoint?.rem(1))
            Bukkit.getPluginManager().callEvent(event)
            e.setUseItemInHand(event.baseEvent.useItemInHand())
        }

        if (BlockBase.isMarked(e.clickedBlock)) {
            val event = KPluginInteractBlockEvent(e.player, e.action, e.item, e.clickedBlock,
                                                  e.blockFace, e.hand, e.interactionPoint?.rem(1))
            Bukkit.getPluginManager().callEvent(event)
            e.setUseInteractedBlock(event.baseEvent.useInteractedBlock())
        }
    }

    @EventHandler
    fun onCraft(e: PrepareItemCraftEvent) {
        if (e.inventory.any { ItemBase.isMarked(it) }) {
            val event = KPluginPrepareItemCraftEvent(e.inventory, e.view, e.isRepair)
            Bukkit.getPluginManager().callEvent(event)
        }
    }

    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {
        if (ItemBase.isMarked(e.itemDrop.itemStack)) {
            val event = KPluginPlayerDropItemEvent(e.player, e.itemDrop)
            Bukkit.getPluginManager().callEvent(event)
            e.isCancelled = event.baseEvent.isCancelled
        }
    }

    @EventHandler
    fun onPickup(e: EntityPickupItemEvent) {
        if (e !is Player) return
        if (ItemBase.isMarked(e.item.itemStack)) {
            val event = KPluginPlayerPickupItemEvent(e.player!!, e.item, e.remaining)
            Bukkit.getPluginManager().callEvent(event)
            e.isCancelled = event.baseEvent.isCancelled
        }
    }

    @EventHandler
    fun onHeld(e: PlayerItemHeldEvent) {
        if (ItemBase.isMarked(e.player.inventory.getItem(e.newSlot)) || ItemBase.isMarked(e.player.inventory.getItem(e.previousSlot))) {
            val event = KPluginPlayerItemHeldEvent(e.player, e.previousSlot, e.newSlot)
            Bukkit.getPluginManager().callEvent(event)
            e.isCancelled = event.baseEvent.isCancelled
        }
    }

    @EventHandler
    fun onSlotChange(e: PlayerInventorySlotChangeEvent) {
        if (ItemBase.isMarked(e.oldItemStack) || ItemBase.isMarked(e.newItemStack)) {
            val event = KPluginPlayerInventorySlotChangeEvent(e.player, e.rawSlot, e.oldItemStack, e.newItemStack)
            Bukkit.getPluginManager().callEvent(event)
        }
    }

    @EventHandler
    fun onItemDamage(e: PlayerItemDamageEvent) {
        if (ItemBase.isMarked(e.item)) {
            val event = KPluginPlayerItemDamageEvent(e.player, e.item, e.damage, e.originalDamage)
            Bukkit.getPluginManager().callEvent(event)
            e.isCancelled = event.baseEvent.isCancelled
        }
    }

    @EventHandler
    fun onWorldSave(e: WorldSaveEvent) {
        val event1 = KPluginPreWorldSaveEvent(e.world)
        val event2 = KPluginPostWorldSaveEvent(e.world)
        Bukkit.getPluginManager().callEvent(event1)
        Bukkit.getPluginManager().callEvent(event2)
    }
}

class KPluginInteractItemEvent(p: Player, a: Action, i: ItemStack?, b: Block?, bf: BlockFace, e: EquipmentSlot?, v: Vector?):
    KPluginEvent() { val baseEvent = PlayerInteractEvent(p, a, i, b, bf, e, v) }
class KPluginInteractBlockEvent(p: Player, a: Action, i: ItemStack?, b: Block?, bf: BlockFace, e: EquipmentSlot?, v: Vector?):
    KPluginEvent() { val baseEvent = PlayerInteractEvent(p, a, i, b, bf, e, v) }
class KPluginPrepareItemCraftEvent(c: CraftingInventory, i: InventoryView, ir: Boolean):
    KPluginEvent() { val baseEvent = PrepareItemCraftEvent(c, i, ir) }
class KPluginPlayerDropItemEvent(p: Player, i: Item):
    KPluginEvent() { val baseEvent = PlayerDropItemEvent(p, i) }
class KPluginPlayerPickupItemEvent(p: Player, i: Item, r: Int):
    KPluginEvent() { val baseEvent = EntityPickupItemEvent(p, i, r) }
class KPluginPlayerItemHeldEvent(p: Player, pr: Int, c: Int):
    KPluginEvent() { val baseEvent = PlayerItemHeldEvent(p, pr, c) }
class KPluginPlayerInventorySlotChangeEvent(p: Player, r: Int, o: ItemStack, n: ItemStack):
    KPluginEvent() { val baseEvent = PlayerInventorySlotChangeEvent(p, r, o, n) }
class KPluginPlayerItemDamageEvent(p: Player, w: ItemStack, d: Int, o: Int):
    KPluginEvent() { val baseEvent = PlayerItemDamageEvent(p, w, d, o) }
class KPluginPreWorldSaveEvent(w: World):
    KPluginEvent() { val baseEvent = WorldSaveEvent(w) }
class KPluginPostWorldSaveEvent(w: World):
    KPluginEvent() { val baseEvent = WorldSaveEvent(w) }