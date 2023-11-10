package io.github.fhanko.kplugin.zones.handler

import org.bukkit.entity.Player

interface EnterHandler {
    /**
     * Called when a player enters this Zone
     */
    fun enter(p: Player) { }

    /**
     * Called when a player leaves this Zone
     */
    fun leave(p: Player) { }
}