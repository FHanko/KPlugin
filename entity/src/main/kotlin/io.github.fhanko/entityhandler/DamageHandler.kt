package io.github.fhanko.entityhandler

import org.bukkit.event.entity.EntityDamageEvent

/**
 * Implementable for subclasses of EntityBase to override on damage function.
 */
interface DamageHandler {
    fun damage(e: EntityDamageEvent) { }
}

interface NotDamageableHandler: DamageHandler {
    override fun damage(e: EntityDamageEvent) { e.isCancelled = true }
}