package com.github.fhanko.exampleplugin.items

import com.github.fhanko.exampleplugin.EconomyCard
import com.github.fhanko.items.itemhandler.ClickHandler
import com.github.fhanko.items.ItemArgument
import com.github.fhanko.items.ItemBase
import com.github.fhanko.items.ItemData
import com.github.fhanko.items.toFloat
import com.github.fhanko.util.mm
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
        val cash = args.getOrNull(0).toFloat(5.0f).float
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