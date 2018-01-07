package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.Config
import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import br.com.dusty.dcommon.util.text.positive
import org.bukkit.entity.Player

object ConfigCommand: PlayerCustomCommand(EnumRank.ADMIN, "config") {

	val USAGE = Text.NEGATIVE_PREFIX + "Uso: /config ".basic() + "<slots> <númeroDeSlots>".negative()

	val SLOTS_CHANGED = Text.POSITIVE_PREFIX + "Agora o número de ".basic() + "\'slots\'".positive() + " do servidor é ".basic() + "%s".positive() + "!".basic()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		when {
			args.size < 2 -> sender.sendMessage(USAGE)
			else          -> when (args[0].toLowerCase()) {
				"slots" -> {
					val slots = args[1].toIntOrNull() ?: 0

					sender.sendMessage(SLOTS_CHANGED.format(slots))

					with(Config) {
						data.slots = slots

						saveData()
					}
				}
			}
		}

		return false
	}
}
