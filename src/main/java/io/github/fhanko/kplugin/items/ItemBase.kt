package io.github.fhanko.kplugin.items

import io.github.fhanko.kplugin.KPlugin
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent
import io.papermc.paper.event.player.PlayerStopUsingItemEvent
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Bukkit.getServer
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

@Suppress("LeakingThis")
abstract class ItemBase(material: Material, name: String, description: List<String> = listOf()): Listener {
    companion object {
        private val KEY = NamespacedKey.fromString("kplugin_item")!!
        private var itemCount = 0

        /**
         * Adds given item to given Player.
         */
        fun give(player: Player, item: ItemBase, amount: Int = 1) {
            val i = item.item;
            i.amount = amount
            player.inventory.addItem(i);
        }

        /**
         * Marks an Item with a PDC ID.
         */
        fun mapItem(item: ItemStack, id: Int) {
            val meta = item.itemMeta;
            meta.persistentDataContainer.set(KEY, PersistentDataType.INTEGER, id)
            item.setItemMeta(meta)
        }
    }

    protected var item: ItemStack;
    private var id: Int;

    init {
        item = ItemStack(material)
        val meta = item.itemMeta;
        meta.displayName(Component.text(name))
        meta.lore(description.map { Component.text(it) })
        item.setItemMeta(meta)
        id = itemCount++;
        mapItem(item, id);
        Bukkit.getPluginManager().registerEvents(this, getServer().pluginManager.getPlugin("KPlugin")!!)
    }

    private fun compareId(other: ItemStack?) =
        other?.itemMeta?.persistentDataContainer?.get(KEY, PersistentDataType.INTEGER) == id

    /**
     * EventHandlers call overridable event functions
     */

    open fun onClick(e: PlayerInteractEvent) {}
    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (compareId(e.item)) onClick(e);
    }

    open fun onEquip(e: PlayerItemHeldEvent) {}
    @EventHandler
    open fun onHeld(e: PlayerItemHeldEvent) {
        if (compareId(e.player.inventory.getItem(e.newSlot))) onEquip(e)
    }
}