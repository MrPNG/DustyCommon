package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumChat
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.GamerRegistry
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import br.com.dusty.dcommon.util.text.positive
import org.bukkit.entity.Player

object StaffChatCommand: PlayerCustomCommand(EnumRank.MOD, "staffchat", "sc") {

	val ENTERED_STAFF_CHAT = Text.POSITIVE_PREFIX + "Agora você ".basic() + "está".positive() + " no chat da ".basic() + "staff".positive() + "!".basic()
	val EXITED_STAFF_CHAT = Text.NEGATIVE_PREFIX + "Agora você ".basic() + "não".negative() + " está mais no chat da ".basic() + "staff".negative() + "!".basic()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = GamerRegistry.gamer(sender)

		if (gamer.chat != EnumChat.STAFF) {
			gamer.chat = EnumChat.STAFF

			sender.sendMessage(ENTERED_STAFF_CHAT)
		} else {
			gamer.chat = EnumChat.NORMAL

			sender.sendMessage(EXITED_STAFF_CHAT)
		}

		return false
	}
}
