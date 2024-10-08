package com.github.fhanko.persistence.converter

import com.github.fhanko.persistence.YamlUtil
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.bukkit.craftbukkit.inventory.CraftInventoryCustom
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

@Converter
class InventoryConverter: AttributeConverter<Inventory, String> {
    override fun convertToDatabaseColumn(attribute: Inventory?): String {
        val strList = mutableListOf<String>()
        attribute ?: return ""
        strList.add(attribute.holder!!::class.java.name)
        strList.add(attribute.size.toString())
        strList.add(YamlUtil.objToString(attribute.contents))
        return YamlUtil.objToString(strList)
    }

    override fun convertToEntityAttribute(dbData: String?): Inventory {
        val strList = YamlUtil.stringToObj<MutableList<String>>(dbData)
        val holder = Class.forName(strList[0]).kotlin.objectInstance as InventoryHolder
        val inv = CraftInventoryCustom(holder, strList[1].toInt())
        val arr = (YamlUtil.stringToObj<ArrayList<ItemStack?>>(strList[2])).toTypedArray()
        inv.contents = arr
        return inv
    }
}