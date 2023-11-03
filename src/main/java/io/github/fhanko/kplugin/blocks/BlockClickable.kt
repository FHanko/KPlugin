package io.github.fhanko.kplugin.blocks

import io.github.fhanko.kplugin.KPluginInteractBlockEvent
import io.github.fhanko.kplugin.util.PlayerStorage
import io.github.fhanko.kplugin.util.hash
import io.github.fhanko.kplugin.util.mm
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

interface BlockClickable: Listener, BlockComparable {
    fun leftClick(e: PlayerInteractEvent) { }
    fun rightClick(e: PlayerInteractEvent) { }
    fun getCooldown() = 0L
    @EventHandler
    fun onInteract(e: KPluginInteractBlockEvent) {
        if (compareBlockId(e.clickedBlock)) {
            if (e.hand != EquipmentSlot.HAND) return
            if (getCooldown() > 0) {
                val hash = hash(e.item?.displayName().toString())
                val cooldown = PlayerStorage.getCard(e.player)?.getCooldown(hash) ?: 0
                if (cooldown > 0L) {
                    e.player.sendMessage(mm.deserialize("<red>${cooldown.div(1000.0)}s<reset> cooldown remaining."))
                    return
                }
                else PlayerStorage.getCard(e.player)?.setCooldown(hash, getCooldown())
            }
            if (e.action.isLeftClick) leftClick(e)
            if (e.action.isRightClick) rightClick(e)
        }
    }
}