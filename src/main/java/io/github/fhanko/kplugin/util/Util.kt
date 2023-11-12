package io.github.fhanko.kplugin.util

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector
import org.joml.Vector3f
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.math.roundToInt

infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

private val PRIMITIVE_DATA_TYPES = arrayOf(
    PersistentDataType.BYTE,
    PersistentDataType.SHORT,
    PersistentDataType.INTEGER,
    PersistentDataType.LONG,
    PersistentDataType.FLOAT,
    PersistentDataType.DOUBLE,
    PersistentDataType.STRING,
    PersistentDataType.BYTE_ARRAY,
    PersistentDataType.INTEGER_ARRAY,
    PersistentDataType.LONG_ARRAY,
    PersistentDataType.TAG_CONTAINER_ARRAY,
    PersistentDataType.TAG_CONTAINER
)

fun pdcDataType(pdc: PersistentDataContainer, key: NamespacedKey): PersistentDataType<*, *>? {
    for (type in PRIMITIVE_DATA_TYPES) {
        if (pdc.has(key, type)) return type
    }
    return null
}

/**
 * Copies all persistent data from pdcSource to pdcTarget. Does not override keys.
 */
fun copyPdc(pdcSource: PersistentDataContainer, pdcTarget: PersistentDataContainer) {
    pdcSource.keys.forEach { k ->
        val dataType = pdcDataType(pdcSource, k) ?: return
        val data = pdcSource.get(k, dataType) ?: return
        if (!pdcTarget.has(k))
            pdcTarget.setAlt(k, dataType, data)
    }
}

fun <T : Any> PersistentDataContainer.setAlt(key: NamespacedKey, type: PersistentDataType<*, T>, data: Any) =
    set(key, type, type.complexType.cast(data))

var mm = MiniMessage.miniMessage()

fun hash(input:String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}

fun Double.roundTo(n : Int) : Double {
    return "%.${n}f".format(this).toDouble()
}

fun Location.rem(mod: Int): Vector {
    return Vector(x % mod, y % mod, z % mod)
}

fun Vector.to3f(): Vector3f {
    return Vector3f(this.x.toFloat(), this.y.toFloat(), this.z.toFloat())
}

fun Double.rangeTo(to: Double): IntRange {
    return this.roundToInt() .. to.roundToInt()
}

val random = Random()