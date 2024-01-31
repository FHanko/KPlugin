package io.github.fhanko

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Display
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class MaterialDisplay(location: Location, material: Material, size: Vector, brightness: Int = -1) {
    val display: ItemDisplay
    init {
        display = location.world.spawn(location, ItemDisplay::class.java)
        display.itemStack = ItemStack(material)
        display.isPersistent = false
        val t = display.transformation
        t.scale.set(size.x, size.y, size.z)
        display.transformation = t
        display.teleportDuration = 1
        if (brightness != -1) display.brightness = Display.Brightness(brightness, brightness)
    }

    fun remove() {
        display.remove()
    }
}