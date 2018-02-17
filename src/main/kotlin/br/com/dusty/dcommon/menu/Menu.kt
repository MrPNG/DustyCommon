package br.com.dusty.dcommon.menu

import br.com.dusty.dcommon.gamer.Gamer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

open class Menu(val gamer: Gamer, size: Int, title: String): CraftInventoryCustom(gamer.player, size, title) {

	val id = nextId()

	open fun onClick(action: InventoryAction, click: ClickType, currentItem: ItemStack?, slot: Int, slotType: InventoryType.SlotType, hotbarButton: Int) {}

	companion object {

		private var nextId = 0

		fun nextId() = nextId++
	}
}
