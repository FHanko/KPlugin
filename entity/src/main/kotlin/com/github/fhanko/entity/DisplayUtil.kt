package com.github.fhanko.entity

import com.github.fhanko.util.then
import com.github.fhanko.util.to3f
import net.minecraft.util.Mth
import org.bukkit.entity.Display
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.joml.Quaternionf

object DisplayUtil {
    fun blockFacePlayer(display: Display, player: Player) {
        val q = qDegree(Vector(0f, 1f, 0f), (Math.round((player.yaw) / -90) * 90).toFloat())
        setRotation(display, q)
    }

    /**
     * Sets the rotation of [display] to [quaternion].
     */
    fun setRotation(display: Display, quaternion: Quaternionf) {
        val t = display.transformation
        t.leftRotation.set(quaternion)
        display.transformation = t
    }

    fun applyRotation(display: Display, quaternion: Quaternionf) {
        val t = display.transformation
        t.leftRotation.set(t.leftRotation then quaternion)
        display.transformation = t
    }

    /**
     * Sets the translation of [display] to [vector].
     */
    fun setTranslation(display: Display, vector: Vector) {
        val t = display.transformation
        t.translation.set(vector.to3f())
        display.transformation = t
    }

    /**
     * Represents a rotation from [vectorFrom] to a point on [vectorTo].
     */
    fun qVector(vectorFrom: Vector, vectorTo: Vector): Quaternionf = Quaternionf().rotationTo(vectorFrom.to3f(), vectorTo.to3f())

    /**
     * Represents a rotation along [vector] by [angle] degrees.
     */
    fun qDegree(vector: Vector, angle: Float): Quaternionf = Quaternionf().rotateAxis(angle * Mth.DEG_TO_RAD, vector.to3f())
}