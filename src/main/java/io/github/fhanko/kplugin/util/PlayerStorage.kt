@file:Suppress("unused")
package io.github.fhanko.kplugin.util

import org.bukkit.entity.Player
import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

object PlayerStorage {
    private val playerList = HibernateUtil.loadEntity(PlayerData::class.java, 0) ?: PlayerData()

    init {
        HibernateUtil.saveEntity(playerList, HibernateUtil.Operation.SaveOrUpdate)
    }

    fun register(p: Player) {
        if (!playerList.playerData.containsKey(p.identity().uuid())) {
            playerList.playerData[p.identity().uuid()] =
                PlayerCard(p.identity().uuid(), p.name, BigDecimal(0), playerList)
            playerList.playerData[p.identity().uuid()]?.update()
        }
    }

    fun getCard(p: Player): PlayerCard? = playerList.playerData[p.identity().uuid()]
}

@Entity
class PlayerData {
    @Id val id = 0
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "playerData")
    @MapKey(name = "uuid")
    val playerData : MutableMap<UUID, PlayerCard> = HashMap<UUID, PlayerCard>()
}

@Entity @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
class PlayerCard(
    @Id @Type(type="uuid-char") val uuid: UUID,
    @Column val name: String,
    @Column var balance: BigDecimal,
    @ManyToOne val playerData: PlayerData) {
    fun update() = HibernateUtil.saveEntity(this, HibernateUtil.Operation.SaveOrUpdate)

    fun addBalance(value: BigDecimal) {
        balance = balance.add(value)
        update()
    }
}