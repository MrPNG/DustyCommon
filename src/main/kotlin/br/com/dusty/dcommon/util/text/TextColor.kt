package br.com.dusty.dcommon.util.text

import org.bukkit.ChatColor

enum class TextColor constructor(val code: Int, chatColor: ChatColor) {

	BLACK(0x0, ChatColor.BLACK),
	DARK_BLUE(0x1, ChatColor.DARK_BLUE),
	DARK_GREEN(0x2, ChatColor.DARK_GREEN),
	DARK_AQUA(0x3, ChatColor.DARK_AQUA),
	DARK_RED(0x4, ChatColor.DARK_RED),
	DARK_PURPLE(0x5, ChatColor.DARK_PURPLE),
	GOLD(0x6, ChatColor.GOLD),
	GRAY(0x7, ChatColor.GRAY),
	DARK_GRAY(0x8, ChatColor.DARK_GRAY),
	BLUE(0x9, ChatColor.BLUE),
	GREEN(0xA, ChatColor.GREEN),
	AQUA(0xB, ChatColor.AQUA),
	RED(0xC, ChatColor.RED),
	LIGHT_PURPLE(0xD, ChatColor.LIGHT_PURPLE),
	YELLOW(0xE, ChatColor.YELLOW),
	WHITE(0xF, ChatColor.WHITE);

	private val STRING = chatColor.toString()

	override fun toString() = STRING
}
