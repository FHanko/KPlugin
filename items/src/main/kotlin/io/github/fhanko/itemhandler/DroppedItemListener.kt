package io.github.fhanko.itemhandler

import io.github.fhanko.Initializable
import io.github.fhanko.dbg
import io.github.fhanko.itemhandler.ItemListener.scheduleCancel
import io.github.fhanko.itemhandler.ItemListener.scheduleTimer
import org.bukkit.entity.Item
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent

/**
 * Event that periodically fires for dropped [item]s in the world.
 */
class DroppedItemEvent(val item: Item, private var cancelled: Boolean = false)
    : Event(), Cancellable {
    companion object {
        private val HANDLER_LIST = HandlerList()
        @JvmStatic fun getHandlerList(): HandlerList = HANDLER_LIST
    }
    override fun getHandlers(): HandlerList = HANDLER_LIST
    override fun isCancelled() = cancelled
    override fun setCancelled(cancel: Boolean) { cancelled = cancel }
}

object DroppedItemListener: Listener, Initializable {
    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {
        scheduleTimer(e.itemDrop.uniqueId.toString(), 5, 300, {
            val event = DroppedItemEvent(e.itemDrop)
            if (!event.callEvent()) scheduleCancel(e.itemDrop.uniqueId.toString())
        })
    }

    @EventHandler
    fun onPickup(e: EntityPickupItemEvent) {
        if (e.remaining == 0) scheduleCancel(e.item.uniqueId.toString())
    }
}