package io.github.fhanko.kplugin.entity

import org.bukkit.entity.Display
import org.bukkit.entity.Player
import org.joml.Vector3f

object DisplayUtil {
    fun facePlayer(display: Display, player: Player) {
        val t = display.transformation
        val angle = (360 - player.yaw + 180)
        val angleSnapped = (Math.round(angle / 90) * 90) + 180
        t.leftRotation.fromAxisAngleDeg(Vector3f(0f, 1f, 0f), angleSnapped.toFloat())
        display.transformation = t
    }
}