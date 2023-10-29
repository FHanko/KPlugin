package io.github.fhanko.kplugin.items

import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
class ItemListener: Listener {
    companion object {
        private val KEY = "kplugin_item"
        private val actionMap = mutableMapOf<Int, (PlayerInteractEvent) -> Unit>()

        fun registerAction(id: Int, action: (PlayerInteractEvent) -> Unit) {
            actionMap[id] = action;
        }

        fun mapItem(item: ItemStack, id: Int) {
            val meta = item.itemMeta;
            meta.persistentDataContainer.set(NamespacedKey.fromString(KEY)!!, PersistentDataType.INTEGER, id)
            item.setItemMeta(meta)
        }
    }

    @EventHandler
    fun onItemUse(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        val itemID = e.item?.itemMeta?.persistentDataContainer?.get(NamespacedKey.fromString(KEY)!!, PersistentDataType.INTEGER)
        e.player.sendMessage("item ${itemID}")
        val act = actionMap.get(itemID) ?: return
        act(e)
    }
}