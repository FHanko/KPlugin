package io.github.fhanko.items

import io.github.fhanko.*
import io.github.fhanko.handler.ClickHandler
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object CurrencyItem: ItemBase(3, Material.PAPER, "$$$", listOf("Use to add to balance")), ClickHandler {
    private val cashAmount = ItemData(PersistentDataType.FLOAT, "cashAmount")
    /**
     * arg0 = cashAmount as Float
     */
    override fun instance(amount: Int, vararg args: ItemArgument): ItemStack {
        val i = ItemStack(item)
        i.amount = amount
        val cash = args.getOrNull(0)?.float ?: 5f
        setText(i, mm.deserialize("<green>$cash$"), listOf())
        cashAmount.set(i, cash)
        return i
    }

    override fun rightClick(e: PlayerInteractEvent) {
        val cash = cashAmount.get(e.player.inventory.itemInMainHand)!!.toBigDecimal()
        EconomyCard.getCard(e.player).addBalance(cash)
        e.player.inventory.itemInMainHand.amount--
        e.player.sendMessage(mm.deserialize("Added <green>$cash$<reset> to your balance."))
    }
}