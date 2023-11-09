package io.github.fhanko.kplugin.util

import net.minecraft.util.Mth
import net.minecraft.util.Mth.cos
import net.minecraft.util.Mth.sin
import org.joml.Quaternionf

class Rotation(private val roll: Float, private val pitch: Float, private val yaw: Float) {
    fun toQuaternionDeg(): Quaternionf
    {
        return toQuaternion(Mth.DEG_TO_RAD * roll, Mth.DEG_TO_RAD * pitch, Mth.DEG_TO_RAD * yaw)
    }

    fun toQuaternion(): Quaternionf
    {
        return toQuaternion(roll, pitch, yaw)
    }

    private fun toQuaternion(roll: Float, pitch: Float, yaw: Float): Quaternionf // roll (x), pitch (y), yaw (z), angles are in radians
    {
        // Abbreviations for the various angular functions
        val cr: Float = cos(roll * 0.5f)
        val sr: Float = sin(roll * 0.5f)
        val cp: Float = cos(pitch * 0.5f)
        val sp: Float = sin(pitch * 0.5f)
        val cy: Float = cos(yaw * 0.5f)
        val sy: Float = sin(yaw * 0.5f)
        val q: Quaternionf = Quaternionf()
        q.w = cr * cp * cy + sr * sp * sy
        q.x = sr * cp * cy - cr * sp * sy
        q.y = cr * sp * cy + sr * cp * sy
        q.z = cr * cp * sy - sr * sp * cy
        return q
    }
}