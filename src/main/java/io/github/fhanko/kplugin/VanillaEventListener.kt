package io.github.fhanko.kplugin

import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.util.PlayerStorage
import io.papermc.paper.event.player.PlayerPickItemEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

/**
 * Addresses event efficiency concerns by handling them once and firing custom Events
 */
class VanillaEventListener: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        PlayerStorage.register(e.player)
    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if (e is KPluginInteractItemEvent || e is KPluginInteractBlockEvent) return
        if (e.action == Action.PHYSICAL) return
        if (e.item != null && ItemBase.isMarked(e.item!!)) {
            val event = KPluginInteractItemEvent(e.player, e.action, e.item, e.clickedBlock,
                                                 e.blockFace, e.hand!!)
            Bukkit.getPluginManager().callEvent(event)
            if (event.useItemInHand() == Event.Result.DENY) e.isCancelled = true
        }

        if (e.clickedBlock != null && BlockBase.isMarked(e.clickedBlock!!)) {
            val event = KPluginInteractBlockEvent(e.player, e.action, e.item, e.clickedBlock,
                                                  e.blockFace, e.hand!!)
            Bukkit.getPluginManager().callEvent(event)
            if (event.useInteractedBlock() == Event.Result.DENY) e.isCancelled = true
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
}

class KPluginInteractItemEvent(p: Player, a: Action, i: ItemStack?, b: Block?, bf: BlockFace, e: EquipmentSlot):
    PlayerInteractEvent(p, a, i, b, bf, e)

class KPluginInteractBlockEvent(p: Player, a: Action, i: ItemStack?, b: Block?, bf: BlockFace, e: EquipmentSlot):
    PlayerInteractEvent(p, a, i, b, bf, e)

class KPluginPrepareItemCraftEvent(c: CraftingInventory, i: InventoryView, ir: Boolean): PrepareItemCraftEvent(c, i, ir)

class KPluginPlayerDropItemEvent(p: Player, i: Item): PlayerDropItemEvent(p, i)
class KPluginPlayerPickupItemEvent(p: Player, i: Item, r: Int): EntityPickupItemEvent(p, i, r)