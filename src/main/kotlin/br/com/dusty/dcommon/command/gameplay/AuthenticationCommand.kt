package br.com.dusty.dcommon.command.gameplay

import br.com.dusty.dcommon.Config
import br.com.dusty.dcommon.clan.Clans
import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.Tasks
import br.com.dusty.dcommon.util.protocol.Protocols
import br.com.dusty.dcommon.util.text.*
import br.com.dusty.dcommon.util.web.WebAPI
import com.avaje.ebean.validation.factory.EmailValidatorFactory
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.mindrot.jbcrypt.BCrypt
import java.lang.NullPointerException
import java.util.*

object AuthenticationCommand: PlayerCustomCommand(EnumRank.YOUTUBER, "login", "registerAll", "registrar") {

	val USAGE_LOGIN = Text.NEGATIVE_PREFIX + "Uso: /login ".basic() + "<email> <senha>".negative()
	val USAGE_REGISTER = Text.NEGATIVE_PREFIX + "Uso: /registrar ".basic() + "<email> <confirmarEmail> <senha> <confirmarSenha>".negative()

	val ALREADY_AUTHENTICATED = Text.NEGATIVE_PREFIX + "Você ".basic() + "já".negative() + " está ".basic() + "autenticado".negative() + "!".basic()

	val WRONG_EMAIL_PASSWORD = Text.NEGATIVE_PREFIX + "O ".basic() + "email".negative() + " ou a ".basic() + "senha".negative() + " está ".basic() + "incorreto(a)".negative() + "!".basic()
	val LOGIN_FAIL = Text.NEGATIVE_PREFIX + "Não".negative() + " foi possível fazer ".basic() + "login".negative() + " no servidor!".basic()
	val LOGIN_SUCCESS = Text.POSITIVE_PREFIX + "Você fez ".basic() + "login".positive() + " no servidor!".basic()

	val INVALID_EMAIL = Text.NEGATIVE_PREFIX + "O ".basic() + "email".negative() + " digitados é ".basic() + "inválido".negative() + "!".basic()
	val DIFFERENT_EMAILS = Text.NEGATIVE_PREFIX + "Os ".basic() + "emails".negative() + " digitados são ".basic() + "diferentes".negative() + "!".basic()
	val DIFFERENT_PASSWORDS = Text.NEGATIVE_PREFIX + "As ".basic() + "senhas".negative() + " digitadas são ".basic() + "diferentes".negative() + "!".basic()
	val EMAIL_ALREADY_IN_USE = Text.NEGATIVE_PREFIX + "Esse email ".basic() + "já".negative() + " está em ".basic() + "uso".negative() + "!".basic()
	val REGISTER_FAIL = Text.NEGATIVE_PREFIX + "Não".negative() + " foi possível fazer o ".basic() + "registro".negative() + " no servidor!".basic()
	val REGISTER_SUCCESS = Text.POSITIVE_PREFIX + "Agora você está ".basic() + "registrado".positive() + " no servidor!".basic()

	val KICK_FULL_MESSAGE = "O servidor está cheio!\n\n".negative() + "Compre ".basic() + "PRO".color(TextColor.GOLD) + " ou um ".basic() + "Slot Reservado".color(TextColor.GOLD) + " no site ".basic() + "loja.dusty.com.br".color(
			TextColor.GOLD) + " e entre agora!".basic()

	val SALT = BCrypt.gensalt(12)

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = Gamers.gamer(sender)

		if (gamer.isAuthenticated) sender.sendMessage(ALREADY_AUTHENTICATED)
		else when (alias) {
			"login"                 -> {
				if (args.size < 2) sender.sendMessage(USAGE_LOGIN)
				else {
					val email = args[0].toLowerCase()
					val password = args[1]

					val onNext = Consumer<String> {
						val pair = WebAPI.login(it)

						if (BCrypt.checkpw(password, pair.second)) {
							gamer.isAuthenticated = true

							val uuid = UUID.fromString(pair.first)

							Gamers.GAMER_BY_UUID.remove(sender.uniqueId)
							Gamers.GAMER_BY_UUID[uuid] = gamer

							Protocols.changeUuid(sender, uuid)

							val primitiveGamer = Gamers.primitiveGamer(WebAPI.loadProfile(uuid), uuid)

							if (primitiveGamer == null) throw NullPointerException()
							else {
								Gamers.PRIMITIVE_GAMER_BY_UUID[uuid] = primitiveGamer

								if (primitiveGamer.clan.isNotEmpty()) {
									val clan = UUID.fromString(primitiveGamer.clan)

									if (clan !in Clans.PRIMITIVE_CLAN_BY_UUID) {
										val primitiveClan = Clans.primitiveClan(WebAPI.loadClan(clan))

										if (primitiveClan == null) throw NullPointerException()
										else Clans.PRIMITIVE_CLAN_BY_UUID[clan] = primitiveClan
									}
								}
							}

							gamer.fromPrimitiveGamer(primitiveGamer)

							when {
								Gamers.onlineGamers() > Config.data.slots                                                                   -> sender.kickPlayer(KICK_FULL_MESSAGE)
								Config.data.serverStatus == Config.EnumServerStatus.ONLINE || gamer.rank.isHigherThanOrEquals(EnumRank.MOD) -> {
									sender.sendMessage(LOGIN_SUCCESS)

									Tasks.sync(Runnable { Bukkit.getPluginManager().callEvent(PlayerJoinEvent(sender, null)) })
								}
								else                                                                                                        -> sender.sendMessage(LOGIN_FAIL)
							}

							WebAPI.updateUsername(uuid, sender.name)
						} else sender.sendMessage(WRONG_EMAIL_PASSWORD)
					}

					val onError = Consumer<Throwable> { sender.sendMessage(LOGIN_FAIL) }

					Observable.just(email).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
				}
			}
			"registerAll", "registrar" -> {
				if (args.size < 4) sender.sendMessage(USAGE_REGISTER)
				else {
					val email = args[0].toLowerCase()
					val confirmEmail = args[1].toLowerCase()
					val password = args[2]
					val confirmPassword = args[3]

					when {
						!EmailValidatorFactory.EMAIL.isValid(email) -> sender.sendMessage(INVALID_EMAIL)
						email != confirmEmail                       -> sender.sendMessage(DIFFERENT_EMAILS)
						password != confirmPassword                 -> sender.sendMessage(DIFFERENT_PASSWORDS)
						else                                        -> {
							val uuid = UUID.nameUUIDFromBytes(email.toByteArray())

							val onNext = Consumer<Array<Any>> {
								if (WebAPI.verifyEmail(email)) sender.sendMessage(EMAIL_ALREADY_IN_USE)
								else {
									val status = WebAPI.createAccount(it[0] as UUID, it[1] as String, it[2] as String, it[3] as String)

									if (status == 1) {
										gamer.isAuthenticated = true

										Gamers.GAMER_BY_UUID.remove(sender.uniqueId)
										Gamers.GAMER_BY_UUID[uuid] = gamer

										Protocols.changeUuid(sender, uuid)

										sender.sendMessage(REGISTER_SUCCESS)

										when {
											Gamers.gamers().filter { it.isAuthenticated && it.mode != EnumMode.ADMIN }.size > Config.data.slots -> sender.kickPlayer(KICK_FULL_MESSAGE)
											Config.data.serverStatus == Config.EnumServerStatus.ONLINE                                          -> Tasks.sync(Runnable {
												sender.sendMessage(LOGIN_SUCCESS)

												Bukkit.getPluginManager().callEvent(PlayerJoinEvent(sender, null))
											})
											else                                                                                                -> sender.sendMessage(LOGIN_FAIL)
										}
									} else sender.sendMessage(REGISTER_FAIL)
								}
							}

							val onError = Consumer<Throwable> { sender.sendMessage(REGISTER_FAIL) }

							Observable.just(arrayOf(uuid, sender.name, email, BCrypt.hashpw(password, SALT))).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
						}
					}
				}
			}
		}

		return false
	}
}
