package io.github.fhanko.kplugin.util.converter

import io.github.fhanko.kplugin.blocks.objects.ConnectedChest
import io.github.fhanko.kplugin.util.YamlUtil
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftInventoryCustom
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

@Converter
class InventoryConverter: AttributeConverter<Inventory, String> {
    override fun convertToDatabaseColumn(attribute: Inventory?): String {
        val strList = mutableListOf<String>()
        strList.add(attribute?.size.toString())
        strList.add(YamlUtil.objToString(attribute?.contents))
        return YamlUtil.objToString(strList)
    }

    override fun convertToEntityAttribute(dbData: String?): Inventory {
        val strList = YamlUtil.stringToObj<MutableList<String>>(dbData)
        val inv = CraftInventoryCustom(ConnectedChest, strList[0].toInt())
        val arr = (YamlUtil.stringToObj<ArrayList<ItemStack?>>(strList[1])).toTypedArray()
        inv.contents = arr
        return inv
    }
}