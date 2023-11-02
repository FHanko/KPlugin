package io.github.fhanko.kplugin.util

import ca.spottedleaf.dataconverter.converters.datatypes.DataType
import org.bukkit.entity.Player
import org.checkerframework.checker.units.qual.Length
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object PlayerStorage {
    private val playerList = HibernateUtil.loadEntity(PlayerData::class.java, 0) ?: PlayerData()

    init {
        HibernateUtil.saveEntity(playerList, HibernateUtil.Operation.SaveOrUpdate)
    }

    fun register(p: Player) {
        if (!playerList.playerData.containsKey(p.identity().uuid())) {
            playerList.playerData[p.identity().uuid()] =
                PlayerCard(p.identity().uuid(), p.name, BigDecimal(0), playerList)
            HibernateUtil.saveEntity(playerList.playerData[p.identity().uuid()]!!, HibernateUtil.Operation.Save)
        }
    }

    fun getCard(p: Player): PlayerCard? = playerList.playerData[p.identity().uuid()]

    fun updateCard(p: Player) = HibernateUtil.saveEntity(playerList.playerData[p.identity().uuid()]!!, HibernateUtil.Operation.Update)
}

@Entity
class PlayerData() {
    @Id val id = 0
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "playerData")
    @MapKey(name = "uuid")
    val playerData : MutableMap<UUID, PlayerCard> = HashMap<UUID, PlayerCard>()
}

@Entity class PlayerCard(@Id @Type(type="uuid-char") val uuid: UUID, @Column val name: String, @Column var balance: BigDecimal,
                         @ManyToOne val playerData: PlayerData) {
    fun addBalance(value: BigDecimal) {
        balance = balance.add(value)
        HibernateUtil.saveEntity(this, HibernateUtil.Operation.Update)
    }
}