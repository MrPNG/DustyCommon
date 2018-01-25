package br.com.dusty.dcommon.command.gameplay

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.Players
import br.com.dusty.dcommon.util.Tasks
import br.com.dusty.dcommon.util.stdlib.clearFormatting
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import br.com.dusty.dcommon.util.text.positive
import br.com.dusty.dcommon.util.web.WebAPI
import com.google.common.collect.HashMultimap
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*

object ReportCommand: PlayerCustomCommand(EnumRank.DEFAULT, "dustyreport") {

	val USAGE = Text.NEGATIVE_PREFIX + "Uso: /report ".basic() + "<jogador> <motivo>".negative()

	val NO_ONLINE_PLAYER = Text.NEGATIVE_PREFIX + "Não".negative() + " há um jogador online com o nome \"".basic() + "%s".negative() + "\"!".basic()
	val ALREADY_REPORTED = Text.NEGATIVE_PREFIX + "Você ".basic() + "já".negative() + " reportou o jogador ".basic() + "%s".negative() + "!".basic()
	val REPORTED_PLAYER = Text.POSITIVE_PREFIX + "Você ".basic() + "reportou".positive() + " o jogador ".basic() + "%s".positive() + " por ".basic() + "%s".positive() + "!".basic()
	val REPORT = Text.NEGATIVE_PREFIX + "[".basic() + "Report".negative() + "]: \n".basic() + "  Jogador".negative() + ": %s\n".basic() + "  Motivo".negative() + ": %s \n".basic() + "  Por".negative() + ": %s".basic()

	val COMPLETIONS = arrayListOf("ff", "forcefield", "killaura", "sneak", "fly", "flight", "speed", "velocity", "reach", "gc", "ghostclient", "spider")

	val REPORTS_BY_UUID = HashMultimap.create<UUID, UUID>()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.size < 2) {
			sender.sendMessage(USAGE)
		} else {
			val player = Players[args[0]]

			when {
				player == null                                                  -> sender.sendMessage(NO_ONLINE_PLAYER.format(args[0]))
				REPORTS_BY_UUID.containsEntry(sender.uniqueId, player.uniqueId) -> sender.sendMessage(ALREADY_REPORTED.format(player.displayName.clearFormatting()))
				else                                                            -> {
					val reason = args.copyOfRange(1, args.size).joinToString(separator = " ")

					sender.sendMessage(REPORTED_PLAYER.format(player.displayName.clearFormatting(), reason))

					val message = REPORT.format(player.name, sender.name, reason)

					Gamers.gamers().forEach {
						if (it.rank.isHigherThanOrEquals(EnumRank.MOD)) {
							val staffPlayer = it.player

							staffPlayer.playSound(staffPlayer.location, Sound.CHICKEN_HURT, 1F, 1F)
							staffPlayer.sendMessage(message)
						}
					}

					REPORTS_BY_UUID.put(sender.uniqueId, player.uniqueId)

					Tasks.sync(Runnable { REPORTS_BY_UUID.remove(sender.uniqueId, player.uniqueId) }, 1200L)

					Tasks.async(Runnable { println(WebAPI.report(player.name, sender.name, reason)) })
				}
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = if (args.size == 1) Gamers.gamers().filter {
		sender.canSee(it.player) && it.displayName.startsWith(args[0], true)
	}.map { it.displayName }.toMutableList()
	else COMPLETIONS.filter { it.startsWith(args.last(), true) }.toMutableList()
}
