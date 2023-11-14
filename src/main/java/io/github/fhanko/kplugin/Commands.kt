package io.github.fhanko.kplugin

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.FloatArgument
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import io.github.fhanko.kplugin.gui.objects.GiveGUI
import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.items.objects.CurrencyItem
import io.github.fhanko.kplugin.util.EconomyCard
import io.github.fhanko.kplugin.util.HibernateUtil
import io.github.fhanko.kplugin.util.mm
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
                ItemBase.give(p, a[0] as Int, a[1] as Int, *((a.getOrDefault(2, "") as String).split(" ")).toTypedArray())
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
                    CurrencyItem.give(p, 1, cash.setScale(2, RoundingMode.DOWN).toString())
                }
            }
            else { p.sendMessage(mm.deserialize("<red>Your balance is insufficient.")) }
        }).register()
    }
}