@file:Suppress("unused")
package io.github.fhanko

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.bukkit.entity.Player
import java.math.BigDecimal
import java.util.*

@MappedSuperclass
open class PlayerCard(
    @Id val uuid: UUID,
    @Column val name: String)

@Entity
class EconomyCard(uuid: UUID, name: String): PlayerCard(uuid, name) {
    @Column
    var balance: BigDecimal = BigDecimal(0)
        private set
    fun addBalance(value: BigDecimal) { balance = balance.add(value); }
    companion object {
        fun getCard(player: Player): EconomyCard =
            HibernateUtil.loadOrPersistDefault(EconomyCard(player.uniqueId, player.name), player.uniqueId)
    }
}