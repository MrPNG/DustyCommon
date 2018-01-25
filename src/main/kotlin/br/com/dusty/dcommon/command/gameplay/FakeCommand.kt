package br.com.dusty.dcommon.command.gameplay

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.Tasks
import br.com.dusty.dcommon.util.stdlib.clearFormatting
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import br.com.dusty.dcommon.util.text.positive
import br.com.dusty.dcommon.util.web.MojangAPI
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.bukkit.entity.Player

object FakeCommand: PlayerCustomCommand(EnumRank.YOUTUBER, "fake", "unfake") {

	val USAGE = Text.NEGATIVE_PREFIX + "Uso: /fake ".basic() + "<nome>".negative()

	val NICK_TOO_LONG = Text.NEGATIVE_PREFIX + "O nick ".basic() + "%s".negative() + " é muito ".basic() + "longo".negative() + "!".basic()
	val NICK_ALREADY_EXISTS = Text.NEGATIVE_PREFIX + "Já".negative() + " existe um jogador com o nick ".basic() + "%s".negative() + "!".basic()
	val NICK_CHANGED = Text.POSITIVE_PREFIX + "Agora seu ".basic() + "nick".positive() + " é ".basic() + "%s".positive() + "!".basic()
	val FAIL = Text.NEGATIVE_PREFIX + "Não".negative() + " foi ".basic() + "possível".negative() + " alterar o seu ".basic() + "nick".negative() + "!".basic()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = Gamers.gamer(sender)

		when (alias) {
			"fake"   -> {
				if (args.isEmpty()) sender.sendMessage(USAGE)
				else {
					val name = args[0].clearFormatting()

					when {
						name.length > 16 -> sender.sendMessage(NICK_TOO_LONG.format(name))
						Gamers.gamers().any {
							it.player.name == name || it.displayName.equals(name, true)
						}                -> sender.sendMessage(NICK_ALREADY_EXISTS.format(name))
						else             -> {
							val onNext = Consumer<String> {
								if (MojangAPI.profile(it) != null) sender.sendMessage(NICK_ALREADY_EXISTS.format(it))
								else {
									gamer.run {
										displayName = it

										Tasks.sync(Runnable { refreshTag() })
									}

									sender.sendMessage(NICK_CHANGED.format(it))
								}
							}

							val onError = Consumer<Throwable> {
								sender.sendMessage(FAIL)
							}

							Observable.just(name).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
						}
					}
				}
			}
			"unfake" -> {
				val name = sender.name

				gamer.run {
					displayName = name

					refreshTag()
				}

				sender.sendMessage(NICK_CHANGED.format(name))
			}
		}

		return false
	}
}
