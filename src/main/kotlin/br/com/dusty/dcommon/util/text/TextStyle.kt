package br.com.dusty.dcommon.util.text

import org.bukkit.ChatColor

enum class TextStyle constructor(val code: Int, chatColor: ChatColor) {

	MAGIC(0x10, ChatColor.MAGIC),
	BOLD(0x11, ChatColor.BOLD),
	STRIKETHROUGH(0x12, ChatColor.STRIKETHROUGH),
	UNDERLINE(0x13, ChatColor.UNDERLINE),
	ITALIC(0x14, ChatColor.ITALIC),
	RESET(0x15, ChatColor.RESET);

	private val STRING = chatColor.toString()

	override fun toString() = STRING
}
