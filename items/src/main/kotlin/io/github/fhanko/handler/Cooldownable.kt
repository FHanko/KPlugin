package io.github.fhanko.handler

import io.github.fhanko.hash
import io.github.fhanko.mm
import io.github.fhanko.roundTo
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.sql.Timestamp

interface Cooldownable {
    /**
     * Overridden to set cooldown.
     */
    fun getCooldown() = 0L

    /**
     * Returns the message shown to a [Player] when a cooldown with [cooldown] remaining milliseconds is encountered.
     */
    fun cooldownMessage(cooldown: Long) = mm.deserialize("<red>${cooldown.div(1000.0).roundTo(1)}s<reset> cooldown remaining.")

    /**
     * Checks if [player] has no cooldown associated with [key] and places this cooldown or sends [cooldownMessage].
     */
    fun useCooldown(player: Player, key: String): Boolean {
        val persist: Boolean = getCooldown() >= 300000
        val hash = hash("${player.uniqueId}$key")
        val cooldown = CooldownManager.getCooldown(hash) ?: 0
        if (cooldown > 0L) {
            if (cooldownMessage(cooldown) != Component.text("")) player.sendMessage(cooldownMessage(cooldown))
            return false
        }
        CooldownManager.setCooldown(hash, getCooldown())
        return true
    }
}

object CooldownManager {
    private val cooldowns = mutableMapOf<String, Cooldown>()

    fun getCooldown(hash: String): Long? = cooldowns.getOrDefault(hash, null)?.getRemaining()

    fun setCooldown(hash: String, milliseconds: Long) {
        cooldowns[hash] = Cooldown(hash, Timestamp(System.currentTimeMillis().plus(milliseconds)))
    }
}

class Cooldown(val hash: String, var time: Timestamp) {
    fun getRemaining() = time.time.minus(System.currentTimeMillis())
}

