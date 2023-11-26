package io.github.fhanko.kplugin.entity

import io.github.fhanko.kplugin.util.Schedulable
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class MaterialDisplay(location: Location, material: Material, size: Vector): Schedulable {
    val display: ItemDisplay
    init {
        display = location.world.spawn(location, ItemDisplay::class.java)
        display.itemStack = ItemStack(material)
        display.isPersistent = false
        val t = display.transformation
        t.scale.set(size.x, size.y, size.z)
        display.transformation = t
    }

    fun remove() {
        display.remove()
    }
}