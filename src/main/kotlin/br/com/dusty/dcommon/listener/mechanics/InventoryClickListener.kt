package br.com.dusty.dcommon.listener.mechanics

import br.com.dusty.dcommon.menu.Menu
import org.bukkit.Material.INK_SACK
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType.ENCHANTING

object InventoryClickListener: Listener {

	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		event.run {
			when {
				clickedInventory is Menu                                      -> {
					(clickedInventory as Menu).onClick(action, click, currentItem, slot, slotType, hotbarButton)

					isCancelled = true
				}
				inventory is Menu                                             -> isCancelled = true
				inventory.type == ENCHANTING && currentItem?.type == INK_SACK -> isCancelled = true
			}
		}
	}
}
