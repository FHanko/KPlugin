package io.github.fhanko.kplugin.items.objects

import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.items.ItemDroppable
import io.github.fhanko.kplugin.items.ItemEquippable
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.entity.Player

object TestItem: ItemBase(1, Material.DIAMOND, "Test"), ItemEquippable, ItemDroppable {
    override fun equip(p: Player) {
        p.sendMessage("Equipped test")
    }

    override fun unequip(p: Player) {
        p.sendMessage("Unequipped test")
    }

    override fun drop(p: Player, i: Item) {
        p.sendMessage("Dropped test")
    }
}