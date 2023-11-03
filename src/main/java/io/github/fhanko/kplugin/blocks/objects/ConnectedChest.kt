@file:Suppress("unused")
package io.github.fhanko.kplugin.blocks.objects

import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.blocks.BlockClickable
import io.github.fhanko.kplugin.util.HibernateUtil
import io.github.fhanko.kplugin.util.converter.InventoryConverter
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
import javax.persistence.*

private val CHEST_KEY = NamespacedKey("kplugin", "connectedchest")

/**
 * Connected chest are a set of chests that all share the same content at possibly different locations.
 */
object ConnectedChest: BlockBase(1001, Material.CHEST, "Connected Chest"), BlockClickable, InventoryHolder {
    private var inventoryMap: InventoryMap =
        HibernateUtil.loadEntity(InventoryMap::class.java, 0) ?: InventoryMap(0, mutableListOf())

    init {
        HibernateUtil.saveEntity(inventoryMap, HibernateUtil.Operation.SaveOrUpdate)
    }

    private fun chestId(): Int {
        return inventoryMap.inventory.maxOfOrNull { it.invid }?.plus(1) ?: 0
    }

    /**
     * Adds amount of chests to the players inventory that are connected by incremented chestId.
     */
    override fun give(player: Player, amount: Int, vararg args: String) {
        val i = ItemStack(item)
        val cid = chestId()
        markItem(i, CHEST_KEY, PersistentDataType.INTEGER, cid)
        var invSize = 9
        if (args.isNotEmpty() && args[0].toIntOrNull() != null && args[0].toInt() in 9..54 step 9) invSize = args[0].toInt()
        val inv = KInventory(cid, inventoryMap,CraftInventoryCustom(this, invSize))
        inventoryMap.inventory.add(inv)
        HibernateUtil.saveEntity(inv, HibernateUtil.Operation.Save)

        i.amount = amount
        player.inventory.addItem(i)
    }

    override fun rightClick(e: PlayerInteractEvent) {
        e.isCancelled = true

        val ci: Int = readBlock(e.clickedBlock!!, CHEST_KEY, PersistentDataType.INTEGER)
        e.player.openInventory(inventoryMap.inventory.find { it.invid == ci }?.inventory ?: return)
    }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        if (e.inventory.holder == this) {
            HibernateUtil.saveEntity(inventoryMap.inventory.find { it.inventory == e.inventory }!!, HibernateUtil.Operation.Update)
        }
    }

    override fun getInventory(): Inventory { throw Exception("Unreachable. ConnectedChest inventories are stored in inventoryMap.") }
}

@Entity
class InventoryMap(
    @Id var id: Int = 0,
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "invmap")
    val inventory: MutableList<KInventory>)

@Entity
class KInventory(
    @Id var invid: Int = 0,
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "mapid") var invmap: InventoryMap,
    @Column(columnDefinition = "CLOB") @Convert(converter = InventoryConverter::class)
    var inventory: Inventory)