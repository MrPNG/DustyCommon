package br.com.dusty.dcommon.listener.mechanics

import org.bukkit.Material.INK_SACK
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

object InventoryOpenListener: Listener {

	val LAPIS_LAZULI = ItemStack(INK_SACK, 64, 4.toShort())

	@EventHandler
	fun onInventoryOpen(event: InventoryOpenEvent) {
		if (event.inventory.type == InventoryType.ENCHANTING) {
			val inventory = event.inventory
			val itemStack = inventory.getItem(1)

			if (itemStack == null || itemStack.amount != 64) inventory.setItem(1, LAPIS_LAZULI)
		}
	}
}
