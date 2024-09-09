package com.github.fhanko.exampleplugin.items

import com.github.fhanko.exampleplugin.entities.TestEntity
import com.github.fhanko.items.ItemBase
import com.github.fhanko.items.itemhandler.ClickHandler
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

object EntityItem : ItemBase(8, Material.STICK, "Entity stick"), ClickHandler {
    override fun rightClick(e: PlayerInteractEvent) {
        TestEntity.spawn(e.player.location)
    }
}