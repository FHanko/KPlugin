package io.github.fhanko

import net.kyori.adventure.text.Component
import net.minecraft.world.item.ItemStack
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventoryCustom
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

open class GUI(size: Int, title: Component): InventoryHandler {
    private var guiString = ""
    fun parse(guiString: String) { this.guiString = guiString }
    fun setCharacter(char: Char, item: GUIItem) =
        guiString.forEachIndexed { i, c -> if (char == c) setItem(i % 9, i / 9, item)}

    protected var inventory = CraftInventoryCustom(this, size/9*9, title)
    protected val gui = mutableMapOf<Pair<Int, Int>, GUIItem>()

    fun setItem(slot:Int, item: List<GUIItem>) = item.forEachIndexed { i, g -> setItem((slot + i) % 9, (slot + i) / 9, g) }
    fun setItem(slot:Int, item: GUIItem) = setItem(slot % 9, slot / 9, item)
    fun setItem(x: Int, y: Int, item: GUIItem) {
        inventory.inventory.setItem(y * 9 + x, item.getNMSItem())
        gui[x to y] = item
    }

    fun removeItem(x: Int, y: Int) {
        inventory.inventory.setItem(y * 9 + x, CraftItemStack.asNMSCopy(org.bukkit.inventory.ItemStack(Material.AIR)))
        gui.remove(x to y)
    }

    override fun inventoryClick(e: InventoryClickEvent) {
        e.isCancelled = true
        val loc = (e.slot % 9) to (e.slot / 9)
        if (gui.contains(loc)) {
            gui[loc]!!.action(gui[loc]!!, e.whoClicked as Player)
        }
    }

    override fun getInventory(): Inventory = inventory
}

class GUIItem(val item: org.bukkit.inventory.ItemStack, val action: (GUIItem, Player) -> Unit) {
    fun getNMSItem(): ItemStack = CraftItemStack.asNMSCopy(item)
}