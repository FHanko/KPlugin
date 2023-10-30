package io.github.fhanko.kplugin.items

import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent

interface ItemDroppable: Listener, ItemComparable {
    open fun drop(p: Player, i: Item) {}
    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {
        if (compareId(e.itemDrop.itemStack)) { drop(e.player, e.itemDrop); }
    }

    open fun pickup(p: Player) {}
    @EventHandler
    fun onPickup(e: EntityPickupItemEvent) {
        if (e.entity !is Player) return
        if (compareId(e.item.itemStack)) {
            pickup(e.entity as Player);
        }
    }
}