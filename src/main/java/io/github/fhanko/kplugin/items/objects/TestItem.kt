package io.github.fhanko.kplugin.items.objects

import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.items.handler.ClickHandler
import io.github.fhanko.kplugin.items.handler.DropHandler
import io.github.fhanko.kplugin.items.handler.EquipHandler
import io.github.fhanko.kplugin.util.Cooldownable
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

object TestItem: ItemBase(0, Material.DIAMOND, "Test"), EquipHandler, DropHandler, ClickHandler, Cooldownable {
    override fun equip(p: Player, e: EquipHandler.EquipType) {
        p.sendMessage("Equipped test")
    }

    override fun unequip(p: Player, e: EquipHandler.EquipType) {
        p.sendMessage("Unequipped test")
    }

    override fun drop(e: PlayerDropItemEvent) {
        e.player.sendMessage("Dropped test")
    }

    override fun getCooldown() = 300000L
    override fun rightClick(e: PlayerInteractEvent) {
        if (useCooldown(e.player, e.item!!.displayName().toString())) {
            e.player.velocity = e.player.location.getDirection().setY(0).normalize().multiply(2).setY(0.3)
        }
    }
}