package br.com.dusty.dcommon.util

import br.com.dusty.dcommon.util.ItemStacks.BACKGROUND
import br.com.dusty.dcommon.util.ItemStacks.BOWLS
import br.com.dusty.dcommon.util.ItemStacks.BROWN_MUSHROOMS
import br.com.dusty.dcommon.util.ItemStacks.BUTTON_BACK
import br.com.dusty.dcommon.util.ItemStacks.RED_MUSHROOMS
import br.com.dusty.dcommon.util.ItemStacks.SOUP
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.positive
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

fun Inventory.addItemStacks(itemStacks: Array<ItemStack?>) {
	var i = 0

	while (this.getItem(i) != null) i++

	for (itemStack in itemStacks) this.setItem(i++, itemStack)
}

fun Inventory.fillBackground(backButton: Boolean): Inventory {
	for (i in 0 until this.size) this.setItem(i, BACKGROUND)

	if (backButton) this.setItem(0, BUTTON_BACK)

	return this
}

fun Inventory.hasSlot() = any { it == null }

fun PlayerInventory.addItemOrDrop(itemStack: ItemStack) {
	if (hasSlot()) addItem(itemStack) else holder.world.dropItem(holder.location, itemStack)
}

fun PlayerInventory.addItemsOrDrop(vararg itemStacks: ItemStack?) = itemStacks.forEach { if (it != null) addItemOrDrop(it) }

fun Inventory.isEmpty() = firstOrNull { it != null } == null

fun PlayerInventory.isEmpty() = armorContents.firstOrNull { it != null } == null && (this as Inventory).isEmpty()

fun Player.openInventory(player: Player) {
	openInventory(player.inventory)
	sendMessage(Text.POSITIVE_PREFIX + "Você está ".basic() + "vendo".positive() + " o inventário de ".basic() + player.name.positive())
}

fun Player.fillSoups(fullInventory: Boolean) = inventory.apply { (0 .. (if (fullInventory) 35 else 8)).filter { getItem(it) == null }.forEach { setItem(it, SOUP) } }

fun Player.fillRecraft(): Inventory = inventory.apply {
	setItem(13, RED_MUSHROOMS)
	setItem(14, BROWN_MUSHROOMS)
	setItem(15, BOWLS)
}
