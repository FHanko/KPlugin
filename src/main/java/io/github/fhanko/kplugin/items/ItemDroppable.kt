package io.github.fhanko.kplugin.items

import io.github.fhanko.kplugin.KPluginPlayerDropItemEvent
import io.github.fhanko.kplugin.KPluginPlayerPickupItemEvent
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

interface ItemDroppable: Listener, ItemComparable {
    fun drop(p: Player, i: Item) { }
    @EventHandler
    fun onDrop(kpe: KPluginPlayerDropItemEvent) {
        val e = kpe.baseEvent
        if (compareId(e.itemDrop.itemStack)) { drop(e.player, e.itemDrop); }
    }

    fun pickup(p: Player) {}
    @EventHandler
    fun onPickup(kpe: KPluginPlayerPickupItemEvent) {
        val e = kpe.baseEvent
        if (e.entity !is Player) return
        if (compareId(e.item.itemStack)) {
            pickup(e.entity as Player)
        }
    }
}