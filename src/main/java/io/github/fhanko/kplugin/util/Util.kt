package io.github.fhanko.kplugin.util

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.math.BigInteger
import java.security.MessageDigest

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

fun copyPdc(pdcSource: PersistentDataContainer, pdcTarget: PersistentDataContainer) {
    pdcSource.keys.forEach { k ->
        val dataType = pdcDataType(pdcSource, k) ?: return
        val data = pdcSource.get(k, dataType) ?: return
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