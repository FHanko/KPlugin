package com.github.fhanko.exampleplugin.items

import com.github.fhanko.items.ItemBase
import com.github.fhanko.items.itemhandler.ClickHandler
import com.github.fhanko.items.itemhandler.Cooldownable
import com.github.fhanko.items.itemhandler.DropHandler
import com.github.fhanko.items.itemhandler.EquipHandler
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
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

    override fun pickup(e: EntityPickupItemEvent) {
        val p = e.entity as Player
        p.sendMessage("Picked up test")
    }

    override fun getCooldown() = 3000L
    override fun rightClick(e: PlayerInteractEvent) {
        if (useCooldown(e.player)) {
            e.player.velocity = e.player.location.getDirection().setY(0).normalize().multiply(2).setY(0.3)
        }
    }
}