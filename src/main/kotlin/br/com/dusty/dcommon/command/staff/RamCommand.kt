package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.neutral
import org.bukkit.entity.Player
import java.text.DecimalFormat

object RamCommand: PlayerCustomCommand(EnumRank.ADMIN, "ram") {

	val MESSAGE = Text.NEUTRAL_PREFIX + "Memória RAM:".basic() + "\nEm uso: ".neutral() + "%sMB (%s% do total)".basic() + "\nLivre: ".neutral() + "%sMB (%s% do total)".basic() + "\nTotal: ".neutral() + "%sMB (%s% do máximo)".basic() + "\nMáximo: ".neutral() + "%sMB".basic()

	val MB = 1024.0 * 1024.0
	val RUNTIME = Runtime.getRuntime()
	val DECIMAL_FORMAT = DecimalFormat("#.##")

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val totalMemory = Math.floor(RUNTIME.totalMemory() / MB)
		val freeMemory = Math.floor(RUNTIME.freeMemory() / MB)
		val usedMemory = totalMemory - freeMemory
		val maxMemory = Math.floor(RUNTIME.maxMemory() / MB)

		sender.sendMessage(MESSAGE.format(usedMemory.toString(),
		                                  DECIMAL_FORMAT.format(usedMemory * 100 / totalMemory),
		                                  freeMemory.toString(),
		                                  DECIMAL_FORMAT.format(freeMemory * 100 / totalMemory),
		                                  totalMemory.toString(),
		                                  DECIMAL_FORMAT.format(totalMemory * 100 / maxMemory),
		                                  maxMemory.toString()))

		return false
	}
}
