package io.github.fhanko.kplugin.blocks.objects

import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.blocks.BlockClickable
import io.github.fhanko.kplugin.util.FilePersistable
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftInventoryCustom
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

private val CHEST_KEY = NamespacedKey("kplugin", "connectedchest")
private const val SAVE_FILE = "ConnectedChest.data"

/**
 * Connected chest are a set of chests that all share the same content at possibly different locations.
 */
object ConnectedChest: BlockBase(1001, Material.CHEST, "Connected Chest"), BlockClickable, FilePersistable<HashMap<Int, HashMap<String, Any>>>, InventoryHolder {
    private var inventoryMap = HashMap<Int, Inventory>()

    init {
        inventoryMap = deserialize(load(SAVE_FILE))
    }

    private fun chestId(): Int {
        return inventoryMap.maxByOrNull { it.key }?.key?.plus(1) ?: 0
    }

    /**
     * Adds amount of chests to the players inventory that are connected by incremented chestId.
     */
    override fun give(player: Player, amount: Int, vararg args: String) {
        val i = ItemStack(item)
        val cid = chestId()
        markItem(i, CHEST_KEY, cid)
        var invSize = 9
        if (args.isNotEmpty() && args[0].toIntOrNull() != null && args[0].toInt() in 9..54 step 9) invSize = args[0].toInt()
        inventoryMap[cid] = CraftInventoryCustom(this, invSize)
        save(SAVE_FILE, serialize())

        i.amount = amount
        player.inventory.addItem(i)
    }

    override fun rightClick(e: PlayerInteractEvent) {
        e.isCancelled = true

        val ci: Int = getBlockPdc(e.clickedBlock!!).get(CHEST_KEY, PersistentDataType.INTEGER)!!
        e.player.openInventory(inventoryMap[ci] ?: return)
    }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        if (e.inventory.holder == this) {
            save(SAVE_FILE, serialize())
        }
    }

    override fun getInventory(): Inventory { throw Exception("Unreachable. ConnectedChest inventories are stored in inventoryMap.") }

    private fun serialize(): HashMap<Int, HashMap<String, Any>> {
        val map = HashMap<Int, HashMap<String, Any>>()
        inventoryMap.forEach { (k, v) ->
            map[k] = HashMap<String, Any>()
            map[k]!!["size"] = v.size
            map[k]!!["content"] = v.contents
        }

        return map
    }

    private fun deserialize(map: HashMap<Int, HashMap<String, Any>>?): HashMap<Int, Inventory> {
        val ret = HashMap<Int, Inventory>();
        map?.forEach { (k, v) ->
            val inv = CraftInventoryCustom(this, v["size"] as Int)
            inv.contents = v["content"] as Array<out ItemStack?>
            ret[k] = inv
        }
        return ret
    }
}

