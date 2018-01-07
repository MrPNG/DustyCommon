package br.com.dusty.dcommon.listener.mechanics

import org.bukkit.Material.INK_SACK
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType.ENCHANTING

object InventoryClickListener: Listener {

	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		if (event.inventory.type == ENCHANTING && event.currentItem?.type == INK_SACK) event.isCancelled = true
	}
}
