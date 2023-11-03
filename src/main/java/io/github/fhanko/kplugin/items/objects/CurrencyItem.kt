package io.github.fhanko.kplugin.items.objects

import io.github.fhanko.kplugin.KPluginInteractItemEvent
import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.items.ItemClickable
import io.github.fhanko.kplugin.util.PlayerStorage
import io.github.fhanko.kplugin.util.mm
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

private val CURRENCY_KEY = NamespacedKey("kplugin", "currencyitem")
object CurrencyItem: ItemBase(3, Material.PAPER, "$$$", listOf("Use to add to balance")), ItemClickable {
    override fun give(player: Player, amount: Int, vararg args: String) {
        val i = ItemStack(item)
        i.amount = amount
        val im = i.itemMeta
        val cash = if (args.isNotEmpty() && args[0].toFloatOrNull() != null) args[0].toFloat() else 5f
        im.displayName(mm.deserialize("<green>$cash$"))
        i.itemMeta = im
        markItem(i, CURRENCY_KEY, PersistentDataType.FLOAT, cash)

        player.inventory.addItem(i)
    }

    override fun rightClick(e: PlayerInteractEvent) {
        val cash = readItem(e.player.inventory.itemInMainHand, CURRENCY_KEY, PersistentDataType.FLOAT).toBigDecimal()
        PlayerStorage.getCard(e.player)?.addBalance(cash)
        e.player.inventory.itemInMainHand.amount--
        e.player.sendMessage(mm.deserialize("Added <green>$cash$<reset> to your balance."))
    }
}