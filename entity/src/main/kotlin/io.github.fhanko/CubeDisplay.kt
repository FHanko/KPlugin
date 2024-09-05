package io.github.fhanko

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Display
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.util.BoundingBox

class CubeDisplay(private val l1: Location, private val l2: Location) {
    private val display: ItemDisplay
    init {
        val box = BoundingBox.of(l1, l2)
        display = l1.world.spawn(box.center.toLocation(l1.world), ItemDisplay::class.java)
        display.setItemStack(ItemStack(Material.GLASS))
        display.isGlowing = true
        display.glowColorOverride = Color.GREEN
        display.brightness = Display.Brightness(15,15)
        display.isPersistent = false
        val t = display.transformation
        t.scale.set(box.widthX, box.height, box.widthZ)
        display.transformation = t
    }

    fun remove() {
        display.remove()
    }
}