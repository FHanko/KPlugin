package io.github.fhanko.goals

import com.destroystokyo.paper.entity.ai.Goal
import com.jeff_media.morepersistentdatatypes.datatypes.serializable.ConfigurationSerializableDataType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Mob
import java.util.*

abstract class EntityGoal(protected val mob: Mob, val priority: Int): Goal<Mob>, ConfigurationSerializable {
    abstract val type: ConfigurationSerializableDataType<out EntityGoal>
    abstract val primitives: Map<String, Any>
    override fun serialize(): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        primitives.forEach { map[it.key] = it.value }
        return map
    }
}