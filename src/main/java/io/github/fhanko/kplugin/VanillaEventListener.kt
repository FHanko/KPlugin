package io.github.fhanko.kplugin

import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.util.PlayerStorage
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.EquipmentSlot
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
        if (e.item != null && ItemBase.readItem(e.item!!, ItemBase.KEY, PersistentDataType.INTEGER) != null) {
            val event = KPluginInteractItemEvent(e.player, e.action, e.item, e.clickedBlock,
                                                 e.blockFace, e.hand!!)
            Bukkit.getPluginManager().callEvent(event)
            if (event.useItemInHand() == Event.Result.DENY) e.isCancelled = true
        }

        if (e.clickedBlock != null && BlockBase.readBlock(e.clickedBlock!!, ItemBase.KEY, PersistentDataType.INTEGER) != null) {
            val event = KPluginInteractBlockEvent(e.player, e.action, e.item, e.clickedBlock,
                                                  e.blockFace, e.hand!!)
            Bukkit.getPluginManager().callEvent(event)
            if (event.useInteractedBlock() == Event.Result.DENY) e.isCancelled = true
        }
    }


}

class KPluginInteractItemEvent(p: Player, a: Action, i: ItemStack?, b: Block?, bf: BlockFace, e: EquipmentSlot):
    PlayerInteractEvent(p, a, i, b, bf, e)

class KPluginInteractBlockEvent(p: Player, a: Action, i: ItemStack?, b: Block?, bf: BlockFace, e: EquipmentSlot):
    PlayerInteractEvent(p, a, i, b, bf, e)