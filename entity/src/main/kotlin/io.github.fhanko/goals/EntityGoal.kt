package io.github.fhanko.goals

import com.destroystokyo.paper.entity.ai.Goal
import com.jeff_media.morepersistentdatatypes.datatypes.serializable.ConfigurationSerializableDataType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Mob
import java.util.*

/**
 * MobGoals are not persisted by default. Instead, make them [ConfigurationSerializable] by implementing this class
 * and add them to the entity PDC. They can then be loaded back in an EntityAddToWorldEvent.
 *
 * EntityGoals need to be registered in the ConfigurationSerialization.
 */
abstract class EntityGoal(protected val mob: Mob, val priority: Int): Goal<Mob>, ConfigurationSerializable {
    abstract val primitives: Map<String, Any>
    override fun serialize() = primitives
}