package io.github.fhanko.kplugin.util

import org.bukkit.entity.Player

interface Cooldownable {
    fun getCooldown() = 0L
    fun cooldownMessage(cooldown: Long) = mm.deserialize("<red>${cooldown.div(1000.0).roundTo(1)}s<reset> cooldown remaining.")

    fun useCooldown(p: Player, key: String): Boolean {
        val hash = hash(key)
        val cooldown = CooldownCard.getCard(p).getCooldown(hash) ?: 0
        if (cooldown > 0L) {
            p.sendMessage(cooldownMessage(cooldown))
            return false
        }
        CooldownCard.getCard(p).setCooldown(hash, getCooldown())
        return true
    }
}