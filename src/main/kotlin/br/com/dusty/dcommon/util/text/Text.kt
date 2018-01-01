package br.com.dusty.dcommon.util.text

import org.bukkit.ChatColor

fun String.clearFormatting(): String = ChatColor.stripColor(this)

fun String.basic() = TextColor.GRAY.toString() + this

fun String.positive() = TextColor.GREEN.toString() + this

fun String.neutral() = TextColor.YELLOW.toString() + this

fun String.negative() = TextColor.RED.toString() + this

fun String.color(color: TextColor) = color.toString() + this

fun String.styles(vararg styles: TextStyle) = styles.joinToString { it.toString() } + this

object Text {

	val POSITIVE_PREFIX = ("» ").positive()

	val NEUTRAL_PREFIX = "≡ ".neutral()

	val NEGATIVE_PREFIX = "» ".negative()
}
