@file:Suppress("unused")
package io.github.fhanko.kplugin.blocks.objects

import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.blocks.BlockClickable
import io.github.fhanko.kplugin.util.HibernateUtil
import io.github.fhanko.kplugin.util.converter.InventoryConverter
import jakarta.persistence.*
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftInventoryCustom
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

private val CHEST_KEY = NamespacedKey("kplugin", "connectedchest")

/**
 * Connected chest are a set of chests that all share the same content at possibly different locations.
 */
object ConnectedChest: BlockBase(1001, Material.CHEST, "Connected Chest"), BlockClickable, InventoryHolder {
    private var inventoryMap = HibernateUtil.loadAll(KInventory::class.java)?.toMutableList() ?: mutableListOf<KInventory>()

    /**
     * Adds amount of chests to the players inventory that are connected by incremented chestId.
     */
    override fun give(player: Player, amount: Int, vararg args: String) {
        var invSize = 9
        if (args.isNotEmpty() && args[0].toIntOrNull() != null && args[0].toInt() in 9..54 step 9) invSize = args[0].toInt()

        val i = ItemStack(item)
        val inv = KInventory(CraftInventoryCustom(this, invSize))
        HibernateUtil.saveEntity(inv, HibernateUtil.Operation.Persist)

        markItem(i, CHEST_KEY, PersistentDataType.INTEGER, inv.id)
        inventoryMap.add(inv)

        i.amount = amount
        player.inventory.addItem(i)
    }

    override fun rightClick(e: PlayerInteractEvent) {
        e.isCancelled = true

        val ci: Int = readBlock(e.clickedBlock!!, CHEST_KEY, PersistentDataType.INTEGER)
        val inv = inventoryMap.find { it.id == ci } ?: return
        e.player.openInventory(inv.inventory)
        HibernateUtil.saveEntity(inv, HibernateUtil.Operation.Merge)
    }

    override fun getInventory(): Inventory { throw Exception("Unreachable. ConnectedChest inventories are stored in inventoryMap.") }
}

@Entity
class KInventory(
    @Column(columnDefinition = "CLOB") @Convert(converter = InventoryConverter::class)
    var inventory: Inventory) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int = 0
}