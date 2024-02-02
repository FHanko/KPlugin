package io.github.fhanko

import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

/**
 * Wrapper for [Entity] [PersistentDataContainer].
 */
class EntityData<T : Any>(private val type: PersistentDataType<out Any, T>, private val name: String) {
    private val key = NamespacedKey(PluginInstance.instance, name)

    /**
     * Sets entities [PersistentDataContainer] data represented by this [EntityData].
     */
    fun set(entity: Entity?, value: T) {
        entity?.persistentDataContainer?.set(key, type, value)
    }

    /**
     * Gets entities [PersistentDataContainer] data represented by this [EntityData].
     */
    fun get(entity: Entity?): T? = entity?.persistentDataContainer?.get(key, type)

    /**
     * Removes data represented by this [EntityData] from entities [PersistentDataContainer].
     */
    fun remove(entity: Entity?) {
        entity?.persistentDataContainer?.remove(key)
    }
}