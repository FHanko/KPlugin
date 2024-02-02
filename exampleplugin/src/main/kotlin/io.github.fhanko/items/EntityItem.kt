package io.github.fhanko.items

import io.github.fhanko.ItemBase
import io.github.fhanko.entities.TestEntity
import io.github.fhanko.itemhandler.ClickHandler
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

object EntityItem : ItemBase(8, Material.STICK, "Entity stick"), ClickHandler {
    override fun rightClick(e: PlayerInteractEvent) {
        TestEntity.spawn(e.player.location)
    }
}