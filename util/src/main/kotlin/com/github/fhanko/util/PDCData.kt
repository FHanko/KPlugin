package com.github.fhanko.util

import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType

/**
 * Wrapper for [PersistentDataHolder] [PersistentDataContainer] setters and getters.
 */
abstract class PDCData<H: PersistentDataHolder, T : Any>(private val type: PersistentDataType<out Any, T>, name: String) {
    private val key = NamespacedKey(PluginInstance.instance, name)

    /**
     * Sets holders [PersistentDataContainer] data represented by this [PDCData].
     */
    fun set(holder: H?, value: T) = holder?.persistentDataContainer?.set(key, type, value)

    /**
     * Gets holders [PersistentDataContainer] data represented by this [PDCData].
     */
    fun get(holder: H?): T? = holder?.persistentDataContainer?.get(key, type)

    /**
     * Removes data represented by this [PDCData] from holders [PersistentDataContainer].
     */
    fun remove(holder: H?) = holder?.persistentDataContainer?.remove(key)
}

class WorldData<T : Any>(type: PersistentDataType<out Any, T>, name: String):
    PDCData<World, T>(type, name)

class EntityData<T : Any>(type: PersistentDataType<out Any, T>, name: String):
    PDCData<Entity, T>(type, name)