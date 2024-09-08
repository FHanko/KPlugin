package com.github.fhanko.exampleplugin

import com.github.fhanko.exampleplugin.gui.GiveGUI
import com.github.fhanko.exampleplugin.items.CurrencyItem
import com.github.fhanko.items.ItemBase
import com.github.fhanko.items.toItemArg
import com.github.fhanko.persistence.HibernateUtil
import com.github.fhanko.util.mm
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.FloatArgument
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import java.math.BigDecimal
import java.math.RoundingMode

object Commands {
    fun registerGive() {
        CommandAPICommand("givekp").executesPlayer(PlayerCommandExecutor { p, _ ->
            p.openInventory(GiveGUI.inventory)
        }).register()

        CommandAPICommand("givekp")
            .withArguments(listOf(IntegerArgument("id"), IntegerArgument("amount")))
            .withOptionalArguments(StringArgument("options"))
            .executesPlayer(PlayerCommandExecutor { p, a ->
                val args = ((a.getOrDefault(2, "") as String).split(" ")).map { it.toItemArg() }.toTypedArray()
                ItemBase.give(p, a[0] as Int, a[1] as Int, *args)
            }).register()
    }

    fun registerBal() {
        CommandAPICommand("bal").executesPlayer(PlayerCommandExecutor { p, _ ->
            p.sendMessage(mm.deserialize("<green>${EconomyCard.getCard(p).balance}$"))
        }).register()

        CommandAPICommand("withdraw").withArguments(listOf(FloatArgument("amount"))).executesPlayer(PlayerCommandExecutor { p, a ->
            val cash = a.get("amount").toString().toBigDecimal()
            if (cash < BigDecimal(1)) {
                p.sendMessage(mm.deserialize("<red>You can not withdraw that amount."))
                return@PlayerCommandExecutor
            }
            if (EconomyCard.getCard(p).balance >= cash) {
                HibernateUtil.execute {
                    EconomyCard.getCard(p).addBalance(-cash)
                    CurrencyItem.give(p, 1, cash.setScale(2, RoundingMode.DOWN).toString().toItemArg())
                }
            }
            else { p.sendMessage(mm.deserialize("<red>Your balance is insufficient.")) }
        }).register()
    }
}