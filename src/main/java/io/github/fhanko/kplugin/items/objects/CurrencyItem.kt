package io.github.fhanko.kplugin.items.objects

import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.items.handler.ClickHandler
import io.github.fhanko.kplugin.util.EconomyCard
import io.github.fhanko.kplugin.util.mm
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

private val CURRENCY_KEY = NamespacedKey("kplugin", "currencyitem")
object CurrencyItem: ItemBase(3, Material.PAPER, "$$$", listOf("Use to add to balance")), ClickHandler {
    override fun instance(amount: Int, vararg args: String): ItemStack {
        val i = ItemStack(item)
        i.amount = amount
        val cash = if (args.isNotEmpty() && args[0].toFloatOrNull() != null) args[0].toFloat() else 5f
        setText(i, mm.deserialize("<green>$cash$"), listOf())
        markItem(i, CURRENCY_KEY, PersistentDataType.FLOAT, cash)
        return i
    }

    override fun rightClick(e: PlayerInteractEvent) {
        val cash = readItem(e.player.inventory.itemInMainHand, CURRENCY_KEY, PersistentDataType.FLOAT)!!.toBigDecimal()
        EconomyCard.getCard(e.player).addBalance(cash)
        e.player.inventory.itemInMainHand.amount--
        e.player.sendMessage(mm.deserialize("Added <green>$cash$<reset> to your balance."))
    }
}