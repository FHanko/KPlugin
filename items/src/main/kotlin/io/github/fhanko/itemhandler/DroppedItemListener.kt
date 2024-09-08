package io.github.fhanko.itemhandler

import io.github.fhanko.Initializable
import io.github.fhanko.dbg
import io.github.fhanko.itemhandler.ItemListener.scheduleCancel
import io.github.fhanko.itemhandler.ItemListener.scheduleTimer
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.util.Vector

/**
 * Event that periodically fires for dropped [item]s in the world.
 *
 * Cancel to remove listening schedule.
 */
class DroppedItemEvent(val item: Item, val dropper: Player, private var cancelled: Boolean = false)
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
            val event = DroppedItemEvent(e.itemDrop, e.player)
            if (!event.callEvent()) scheduleCancel(e.itemDrop.uniqueId.toString())
        })
    }

    @EventHandler
    fun onPickup(e: EntityPickupItemEvent) {
        if (e.remaining == 0) scheduleCancel(e.item.uniqueId.toString())
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onTick(e: DroppedItemEvent) {
        if (e.item.velocity == Vector(0, 0, 0)) e.isCancelled = true
    }
}