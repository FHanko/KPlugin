package io.github.fhanko

import org.bukkit.Material
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventoryCustom
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.io.Serializable

open class GUI(val size: Int, val title: String): InventoryHandler, Serializable {
    private var guiString = ""
    fun parse(guiString: String) { this.guiString = guiString }
    fun setCharacter(char: Char, item: GUIItem) =
        guiString.forEachIndexed { i, c -> if (char == c) setItem(i % 9, i / 9, item)}

    @Transient protected var craftInventory: Inventory? = CraftInventoryCustom(this, size/9*9, title)
    protected val gui = mutableMapOf<Pair<Int, Int>, GUIItem>()

    fun setItem(slot:Int, item: List<GUIItem>) = item.forEachIndexed { i, g -> setItem((slot + i) % 9, (slot + i) / 9, g) }
    fun setItem(slot:Int, item: GUIItem) = setItem(slot % 9, slot / 9, item)
    fun setItem(x: Int, y: Int, item: GUIItem) {
        getInventory().setItem(y * 9 + x, item.item)
        gui[x to y] = item
    }

    fun removeItem(x: Int, y: Int) {
        getInventory().setItem(y * 9 + x, ItemStack(Material.AIR))
        gui.remove(x to y)
    }

    override fun inventoryClick(e: InventoryClickEvent) {
        e.isCancelled = true
        val loc = (e.slot % 9) to (e.slot / 9)
        if (gui.contains(loc)) {
            gui[loc]!!.action(gui[loc]!!, e.whoClicked as Player)
        }
    }

    override fun getInventory(): Inventory {
        if (craftInventory == null) {
            craftInventory = CraftInventoryCustom(this, size/9*9, title)
            gui.forEach { setItem(it.key.first, it.key.second, it.value) }
        }
        return craftInventory as Inventory
    }
}

data class GUIItem(val item: ItemStack, val action: (GUIItem, Player) -> Unit): Serializable