package com.github.fhanko.items.itemhandler

import com.github.fhanko.util.hash
import com.github.fhanko.util.mm
import com.github.fhanko.util.roundTo
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
     * Checks if [player] has no cooldown associated with this object and places this cooldown or sends [cooldownMessage].
     */
    fun useCooldown(player: Player): Boolean {
        val hash = "${player.uniqueId}${hashCode()}"
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

