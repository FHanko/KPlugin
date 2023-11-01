package io.github.fhanko.kplugin.blocks.objects

import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.blocks.BlockClickable
import io.github.fhanko.kplugin.util.Persistable
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

private val CHEST_KEY = NamespacedKey("kplugin", "connectedchest")

/**
 * Connected chest are a set of chests that all share the same content at possibly different locations.
 */
object ConnectedChest: BlockBase(1001, Material.CHEST, "Connected Chest"), BlockClickable, Persistable<Int>, InventoryHolder {
    private var chestId = 0
    private val inventoryMap = mutableMapOf<Int, Inventory>()

    /**
     * Adds amount of chests to the players inventory that are connected by incremented chestId.
     */
    override fun give(player: Player, amount: Int) {
        val i = ItemStack(item)
        markItem(i, CHEST_KEY, chestId)
        inventoryMap[chestId] = KPlugin.instance.server.createInventory(this, 9)
        chestId += 1
        save("ConnectedChest.data", chestId)

        i.amount = amount
        player.inventory.addItem(i)
    }

    override fun rightClick(e: PlayerInteractEvent) {
        e.isCancelled = true

        val ci: Int = getBlockPdc(e.clickedBlock!!).get(CHEST_KEY, PersistentDataType.INTEGER)!!
        e.player.openInventory(inventoryMap[ci] ?: return)
    }

    override fun getInventory(): Inventory { throw Exception("Unreachable. ConnectedChest inventories are stored in inventoryMap.") }
}
