package com.github.fhanko.exampleplugin.blocks

import com.github.fhanko.blocks.BlockBase
import com.github.fhanko.blocks.BlockData
import com.github.fhanko.gui.InventoryHandler
import com.github.fhanko.persistence.converter.InventoryConverter
import com.github.fhanko.items.itemhandler.ClickHandler
import com.github.fhanko.items.ItemArgument
import com.github.fhanko.items.toInt
import jakarta.persistence.*
import org.bukkit.Material
import org.bukkit.craftbukkit.inventory.CraftInventoryCustom
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import com.github.fhanko.persistence.HibernateUtil

/**
 * Connected chest are a set of chests that all share the same content at possibly different locations.
 */
object ConnectedChest: BlockBase(5, Material.CHEST, "Connected Chest"), ClickHandler, InventoryHandler {
    private val invId = BlockData(PersistentDataType.INTEGER, "invId")
    /**
     * Adds amount of chests to the players inventory that are connected by incremented chestId.
     */
    override fun instance(amount: Int, vararg args: ItemArgument): ItemStack {
        var invSize = args.getOrNull(0).toInt(9).int
        if (invSize !in 9..54 step 9) invSize = 9

        val i = ItemStack(item)
        val inv = KInventory(CraftInventoryCustom(this, invSize))
        HibernateUtil.saveEntity(inv, HibernateUtil.Operation.Persist)

        invId.set(i, inv.id)

        i.amount = amount
        return i
    }

    private val invList = mutableSetOf<KInventory>()
    override fun rightClickBlock(e: PlayerInteractEvent) {
        e.isCancelled = true

        val ci: Int = invId.getBlock(e.clickedBlock!!) ?: return
        val inv = HibernateUtil.loadEntity(KInventory::class.java, ci) ?: return
        e.player.openInventory(inv.inventory)
        invList.add(inv)
    }

    override fun inventoryClose(e: InventoryCloseEvent) {
        val inv = invList.find { it.inventory == e.inventory } ?: return
        HibernateUtil.saveEntity(inv, HibernateUtil.Operation.Merge)
        invList.remove(inv)
    }
    
    override fun getInventory(): Inventory { throw Exception("Unreachable. ConnectedChest holds multiple inventories.") }
}

@Entity
class KInventory(
    @Column(columnDefinition = "CLOB") @Convert(converter = InventoryConverter::class) var inventory: Inventory) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int = 0
}