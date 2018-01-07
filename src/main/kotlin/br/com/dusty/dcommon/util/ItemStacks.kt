package br.com.dusty.dcommon.util

import br.com.dusty.dcommon.util.stdlib.clearFormatting
import br.com.dusty.dcommon.util.stdlib.fancySplit
import br.com.dusty.dcommon.util.text.TextColor
import br.com.dusty.dcommon.util.text.color
import br.com.dusty.dcommon.util.text.negative
import org.bukkit.*
import org.bukkit.Material.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.material.Dye
import org.bukkit.potion.Potion
import org.bukkit.potion.PotionType

fun ItemStack.rename(name: String): ItemStack {
	val itemMeta = this.itemMeta
	if (itemMeta != null) itemMeta.displayName = name

	this.itemMeta = itemMeta

	return this
}

fun ItemStack.enchant(vararg enchantments: Pair<Enchantment, Int>) = apply { enchantments.forEach { addUnsafeEnchantment(it.first, it.second) } }

fun ItemStack.color(color: Int) = color(Color.fromBGR(color))

fun ItemStack.color(color: Color) = apply {
	if (type.name.startsWith("LEATHER_")) {
		val leatherArmorMeta = itemMeta as LeatherArmorMeta
		leatherArmorMeta.color = color

		itemMeta = leatherArmorMeta
	}
}

fun ItemStack.description(description: List<String>, forceColor: Boolean) = apply {
	val itemMeta = itemMeta
	itemMeta.lore = if (forceColor) description.map { it.clearFormatting().color(TextColor.YELLOW) } else description

	this.itemMeta = itemMeta
}

fun ItemStack.description(description: String, forceColor: Boolean): ItemStack = description(description.fancySplit(32), forceColor)

/**
 * Retorna o **'displayName'** não-formatado de uma [ItemStack], se houver, ou o **name()** de seu [org.bukkit.Material], caso contrário.
 *
 * @param itemStack
 * @return **'displayName'** não-formatado de uma [ItemStack], se houver, ou o **name()** de seu [org.bukkit.Material], caso contrário.
 * Pode, ainda, retornar **null** se a [ItemStack] for 'null'.
 */
fun ItemStack.unformattedDisplayName(): String? {
	val itemMeta = this.itemMeta

	val displayName = if (itemMeta != null && itemMeta.hasDisplayName()) itemMeta.displayName
	else this.type.name

	return displayName.clearFormatting()
}

fun OfflinePlayer.skull() = ItemStack(SKULL_ITEM, 1, SkullType.PLAYER.ordinal.toShort()).also {
	val skullMeta = it.itemMeta as SkullMeta
	skullMeta.owner = name

	it.itemMeta = skullMeta
}

infix fun ItemStack.isSimilarTo(itemStack: ItemStack?) = isSimilar(itemStack)

/**
 * Criação/personalização de [org.bukkit.inventory.ItemStack]
 */
object ItemStacks {

	val SOUP = ItemStack(MUSHROOM_SOUP)
	val BOWL = ItemStack(Material.BOWL)

	val RED_MUSHROOMS = ItemStack(RED_MUSHROOM, 64)
	val BROWN_MUSHROOMS = ItemStack(BROWN_MUSHROOM, 64)
	val BOWLS = ItemStack(Material.BOWL, 64)

	val COCOA_BEAN = ItemStack(INK_SACK, 64, 3.toShort())

	val COMPASS = ItemStack(Material.COMPASS)

	val NO_ARMOR = arrayOfNulls<ItemStack>(4)
	val ARMOR_FULL_IRON: Array<ItemStack?> = arrayOf(ItemStack(IRON_BOOTS), ItemStack(IRON_LEGGINGS), ItemStack(IRON_CHESTPLATE), ItemStack(IRON_HELMET))

	val WOOD_SWORD = ItemStack(Material.WOOD_SWORD)
	val DIAMOND_SWORD = ItemStack(Material.DIAMOND_SWORD)
	val DIAMOND_SWORD_SHARPNESS = ItemStack(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL to 1)

	val DIAMOND_AXE = ItemStack(Material.DIAMOND_AXE)

	val BACKGROUND = ItemStack(STAINED_GLASS_PANE, 1, 1.toShort(), 7.toByte()).rename(" ")

	val BUTTON_BACK = ItemStack(CARPET, 1, 1.toShort(), 14.toByte()).rename("Voltar".negative())

	fun dye(color: DyeColor): ItemStack {
		val dye = Dye()
		dye.color = color

		return dye.toItemStack(1)
	}

	fun potions(amount: Int, extended: Boolean, upgraded: Boolean, potionType: PotionType, splash: Boolean) = Potion(potionType, if (upgraded) 2 else 1, splash, extended).toItemStack(amount)
}
