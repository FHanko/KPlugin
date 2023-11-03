package io.github.fhanko.kplugin.items.objects

import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.items.ItemClickable
import io.github.fhanko.kplugin.items.ItemDroppable
import io.github.fhanko.kplugin.items.ItemEquippable
import io.github.fhanko.kplugin.util.Cooldownable
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent

object TestItem: ItemBase(0, Material.DIAMOND, "Test"), ItemEquippable, ItemDroppable, ItemClickable, Cooldownable {
    override fun equip(p: Player, e: ItemEquippable.EquipType) {
        p.sendMessage("Equipped test")
    }

    override fun unequip(p: Player, e: ItemEquippable.EquipType) {
        p.sendMessage("Unequipped test")
    }

    override fun drop(p: Player, i: Item) {
        p.sendMessage("Dropped test")
    }

    override fun getCooldown() = 2500L
    override fun rightClick(e: PlayerInteractEvent) {
        if (useCooldown(e.player, e.item!!.displayName().toString())) {
            e.player.velocity = e.player.location.getDirection().setY(0).normalize().multiply(2).setY(0.3)
        }
    }
}