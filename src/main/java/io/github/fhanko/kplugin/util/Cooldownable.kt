package io.github.fhanko.kplugin.util

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

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
        val cooldown = CooldownManager.getCooldown(hash, persist) ?: 0
        if (cooldown > 0L) {
            if (cooldownMessage(cooldown) != Component.text("")) player.sendMessage(cooldownMessage(cooldown))
            return false
        }
        CooldownManager.setCooldown(hash, getCooldown(), persist)
        return true
    }
}