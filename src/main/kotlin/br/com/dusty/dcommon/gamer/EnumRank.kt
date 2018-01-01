package br.com.dusty.dcommon.gamer

import br.com.dusty.dcommon.util.text.TextColor
import br.com.dusty.dcommon.util.text.TextColor.*
import br.com.dusty.dcommon.util.text.TextStyle
import br.com.dusty.dcommon.util.text.TextStyle.ITALIC
import br.com.dusty.dcommon.util.text.color
import br.com.dusty.dcommon.util.text.styles
import java.lang.Integer.MAX_VALUE

/**
 * Enum que contém todos os [EnumRank] do servidor associados a um valor numérico 'int' que identifica a hierarquia entre eles,
 * usada nos métodos da própria classe.
 */
enum class EnumRank(private val level: Int = 0, private val color: TextColor = WHITE, private vararg val styles: TextStyle = arrayOf()) {

	NONE(-1),
	DEFAULT(0, GRAY),
	//	VIP(2, TextColor.GREEN),
	MVP(3, BLUE),
	PRO(4, GOLD),
	//	PRO_YOUTUBER(6, TextColor.AQUA),
	YOUTUBER(6, AQUA, ITALIC),
	MOD(8, DARK_PURPLE),
	MODPLUS(9, DARK_PURPLE, ITALIC),
	ADMIN(MAX_VALUE, RED, ITALIC),
	OWNER(MAX_VALUE, DARK_RED, ITALIC);

	private val STRING = format(name.replace("_", " "))

	val PREFIX = format("")

	fun isHigherThan(rank: EnumRank) = level > rank.level

	fun isHigherThanOrEquals(rank: EnumRank) = level >= rank.level

	fun isLowerThan(rank: EnumRank) = level < rank.level

	fun isLowerThanOrEquals(rank: EnumRank) = level <= rank.level

	fun hasNext() = level < MAX_VALUE

	fun next() = values().firstOrNull { it.level > level } ?: NONE

	fun hasPrev() = level > 0

	fun prev() = values().firstOrNull { it.level < level } ?: NONE

	fun format(string: String) = string.color(color).styles(*styles)

	override fun toString() = STRING

	companion object {

		private val BY_LEVEL = values().associate { it.level to it }

		operator fun get(level: Int) = BY_LEVEL[level] ?: NONE

		operator fun get(name: String) = values().firstOrNull { it.name == name.toUpperCase() } ?: NONE
	}
}
