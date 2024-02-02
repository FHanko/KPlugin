package io.github.fhanko

import com.destroystokyo.paper.entity.ai.Goal
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.persistence.PersistentDataType

@Suppress("LeakingThis")
abstract class EntityBase(val id: Int, val type: EntityType) {
    protected val key = EntityData(PersistentDataType.INTEGER, "Id")

    companion object {
        @JvmStatic protected val idKey = NamespacedKey(PluginInstance.instance, "Id")
        val entityMap = mutableMapOf<Int, EntityBase>()

        fun get(entity: Entity?): EntityBase? {
            val id = entity?.persistentDataContainer?.get(idKey, PersistentDataType.INTEGER) ?: return null
            return entityMap[id]
        }
    }

    init {
        entityMap[id] = this
    }

    protected val mobGoals = mutableMapOf<Goal<Mob>, Int>()

    fun spawn(location: Location) {
        val entity = location.world.spawnEntity(location, type)
        entity.isPersistent = true
        if (entity is Mob) {
            Bukkit.getMobGoals().removeAllGoals(entity)
            mobGoals.map { Bukkit.getMobGoals().addGoal(entity, it.value, it.key) }
        }
        key.set(entity, id)
    }
}