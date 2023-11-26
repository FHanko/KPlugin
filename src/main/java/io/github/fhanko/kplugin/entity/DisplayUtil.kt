package io.github.fhanko.kplugin.entity

import org.bukkit.entity.Display
import org.bukkit.entity.Player
import org.joml.Vector3f

object DisplayUtil {
    fun blockFacePlayer(display: Display, player: Player) {
        val t = display.transformation
        t.leftRotation.fromAxisAngleDeg(Vector3f(0f, 1f, 0f), (Math.round((player.yaw) / -90) * 90).toFloat())
        display.transformation = t
    }
}