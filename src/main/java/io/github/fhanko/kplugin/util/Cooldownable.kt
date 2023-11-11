package io.github.fhanko.kplugin.util

import org.bukkit.entity.Player

interface Cooldownable {
    fun getCooldown() = 0L
    fun cooldownMessage(cooldown: Long) = mm.deserialize("<red>${cooldown.div(1000.0).roundTo(1)}s<reset> cooldown remaining.")

    fun useCooldown(p: Player, key: String): Boolean {
        val persist: Boolean = getCooldown() >= 300000
        val hash = hash("${p.uniqueId}$key")
        val cooldown = CooldownManager.getCooldown(hash, persist) ?: 0
        if (cooldown > 0L) {
            p.sendMessage(cooldownMessage(cooldown))
            return false
        }
        CooldownManager.setCooldown(hash, getCooldown(), persist)
        return true
    }
}