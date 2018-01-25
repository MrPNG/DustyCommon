package br.com.dusty.dcommon.command.gameplay

import br.com.dusty.dcommon.clan.Clan
import br.com.dusty.dcommon.clan.SimpleClan
import br.com.dusty.dcommon.clan.SimplePrimitiveClan
import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumChat
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.stdlib.addUuidDashes
import br.com.dusty.dcommon.util.stdlib.clearFormatting
import br.com.dusty.dcommon.util.text.*
import br.com.dusty.dcommon.util.web.MojangAPI
import br.com.dusty.dcommon.util.web.WebAPI
import com.google.common.collect.HashMultimap
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object ClanCommand: PlayerCustomCommand(EnumRank.MOD, "clan") {

	val HELP = arrayOf(Text.NEUTRAL_PREFIX + "Comandos para clans:\n".basic(),
	                   Text.NEUTRAL_PREFIX + "/clan ".basic() + "criar <nomeDoClan> <tagDoClan>".neutral() + ": Criar um clan com um nome (máximo de 16 caracteres) e uma tag (máximo de 3 caracteres)".basic(),
	                   Text.NEUTRAL_PREFIX + "/clan ".basic() + "lider <nomeDoJogador>".neutral() + ": Transferir a liderança do seu clan para outro membro".basic(),
	                   Text.NEUTRAL_PREFIX + "/clan ".basic() + "convidar <nomeDoJogador>".neutral() + ": Convidar um jogador para o seu clan".basic(),
	                   Text.NEUTRAL_PREFIX + "/clan ".basic() + "aceitar <nomeDoLider>".neutral() + ": Aceitar um convite feito por um líder de um clan".basic(),
	                   Text.NEUTRAL_PREFIX + "/clan ".basic() + "remover <nomeDoJogador>".neutral() + ": Remover um jogador do seu clan".basic(),
	                   Text.NEUTRAL_PREFIX + "/clan ".basic() + "sair".neutral() + ": Sair do seu clan atual".basic(),
	                   Text.NEUTRAL_PREFIX + "/clan ".basic() + "chat".neutral() + ": Entrar no/Sair do chat do seu clan".basic()).joinToString("\n")

	val USAGE = Text.NEGATIVE_PREFIX + "Para informações sobre esse comando, use /clan ".basic() + "help".basic()

	val WAIT_MORE = Text.NEGATIVE_PREFIX + "Você deve ".basic() + "aguardar".negative() + " para ".basic() + "usar".negative() + " esse comando ".basic() + "novamente".negative() + "!".basic()
	val PLAYER_NOT_FOUND = Text.NEGATIVE_PREFIX + "Não".negative() + " há um jogador online com o nome ".basic() + "%s".negative() + "!".basic()

	val USAGE_CREATE = Text.NEGATIVE_PREFIX + "Uso: /clan criar ".basic() + "<nomeDoClan> <tagDoClan>".basic()
	val NAME_TOO_LONG = Text.NEGATIVE_PREFIX + "O ".basic() + "nome".negative() + " do seu clan deve conter, no máximo, ".basic() + "16".negative() + " caracteres!".basic()
	val TAG_TOO_LONG = Text.NEGATIVE_PREFIX + "A ".basic() + "tag".negative() + " do seu clan deve conter, no máximo, ".basic() + "3".negative() + " caracteres!".basic()
	val EXIT_OLD_CLAN = Text.NEGATIVE_PREFIX + "Você deve ".basic() + "sair".negative() + " do seu clan ".basic() + "antigo".negative() + " antes de ".basic() + "criar".negative() + " um novo!".basic()
	val CREATE_CLAN_FAIL = Text.NEGATIVE_PREFIX + "Não".negative() + " foi possível ".basic() + "criar".negative() + " o clan!".basic()
	val CLAN_CREATED = Text.POSITIVE_PREFIX + "Você ".basic() + "criou".positive() + " o clan ".basic() + "%s".positive() + " (".basic() + "%s".positive() + ")!".basic()

	val USAGE_LEADER = Text.NEGATIVE_PREFIX + "Uso: /clan lider ".basic() + "<nomeDoJogador>".basic()
	val ALREADY_LEADER = Text.NEGATIVE_PREFIX + "Você ".basic() + "já".negative() + " é o ".basic() + "líder".negative() + " do seu ".basic() + "clan".negative() + "!".basic()
	val SET_LEADER_FAIL = Text.NEGATIVE_PREFIX + "Não".negative() + " foi possível alterar o ".basic() + "líder".negative() + " do clan!".basic()
	val YOU_PROMOTED_SOMEONE = Text.POSITIVE_PREFIX + "Você ".basic() + "promoveu".positive() + " o jogador ".basic() + "%s".positive() + " como líder do seu ".basic() + "clan".positive() + "!".basic()
	val YOU_WERE_PROMOTED = Text.POSITIVE_PREFIX + "Você foi ".basic() + "promovido".positive() + " pelo jogador ".basic() + "%s".positive() + " para líder do seu ".basic() + "clan".positive() + "!".basic()

	val USAGE_INVITE = Text.NEGATIVE_PREFIX + "Uso: /clan convidar ".basic() + "<nomeDoJogador>".basic()
	val HAS_NO_CLAN = Text.NEGATIVE_PREFIX + "Você ".basic() + "não".negative() + " faz parte de um ".basic() + "clan".negative() + "!".basic()
	val CLAN_IS_FULL = Text.NEGATIVE_PREFIX + "Seu clan ".basic() + "já".negative() + " está ".basic() + "lotado".negative() + "!".basic()
	val IS_NOT_LEADER = Text.NEGATIVE_PREFIX + "Você ".basic() + "não".negative() + " é o ".basic() + "líder".negative() + " do seu ".basic() + "clan".negative() + "!".basic()
	val INVITE_YOURSELF = Text.NEGATIVE_PREFIX + "Você ".basic() + "não".negative() + " precisa convidar ".basic() + "você".negative() + " mesmo para o seu próprio ".basic() + "clan".negative() + "!".basic()
	val WAIT_INVITE = Text.NEGATIVE_PREFIX + "Você deve ".basic() + "aguardar".negative() + " antes de ".basic() + "convidar".negative() + " esse jogador ".basic() + "novamente".negative() + "!".basic()
	val ALREADY_HAS_CLAN = Text.NEGATIVE_PREFIX + "Esse jogador ".basic() + "já".negative() + " faz parte de um ".basic() + "clan".negative() + "!".basic()
	val ALREADY_ON_CLAN = Text.NEGATIVE_PREFIX + "Esse jogador ".basic() + "já".negative() + " está no seu ".basic() + "clan".negative() + "!".basic()
	val YOU_INVITED_SOMEONE = Text.POSITIVE_PREFIX + "Você ".basic() + "convidou".positive() + " o jogador ".basic() + "%s".positive() + " para o seu ".basic() + "clan".positive() + "!".basic()
	val YOU_WERE_INVITED = Text.POSITIVE_PREFIX + "Você foi ".basic() + "convidado".positive() + " pelo jogador ".basic() + "%s".positive() + " para se juntar ao clan ".basic() + "%s".positive() + " (".basic() + "%s".positive() + ")!".basic() + "\n" + Text.POSITIVE_PREFIX + "Use o comando ".basic() + "/clan aceitar %s".positive() + " para ".basic() + "aceitar".positive() + " o convite!".basic()

	val USAGE_REMOVE = Text.NEGATIVE_PREFIX + "Uso: /clan remover ".basic() + "<nomeDoJogador>".basic()
	val REMOVE_YOURSELF = Text.NEGATIVE_PREFIX + "Você ".basic() + "não".negative() + " pode deixar seu clan ".basic() + "sem".negative() + " um ".basic() + "líder".negative() + "!".basic()
	val NOT_IN_CLAN = Text.NEGATIVE_PREFIX + "Esse jogador ".basic() + "não".negative() + " faz parte do seu ".basic() + "clan".negative() + "!".basic()
	val REMOVE_FROM_CLAN_FAIL = Text.NEGATIVE_PREFIX + "Não".negative() + " foi possível ".basic() + "remover".negative() + " esse jogador do seu clan!".basic()
	val YOU_REMOVED_SOMEONE = Text.NEGATIVE_PREFIX + "Você ".basic() + "removeu".negative() + " o jogador ".basic() + "%s".negative() + " do seu ".basic() + "clan".negative() + "!".basic()
	val YOU_WERE_REMOVED = Text.NEGATIVE_PREFIX + "Você foi ".basic() + "removido".negative() + " do clan ".basic() + "%s".negative() + " (".basic() + "%s".negative() + ")!".basic()

	val USAGE_ACCEPT = Text.NEGATIVE_PREFIX + "Uso: /clan aceitar ".basic() + "<nomeDoLider>".basic()
	val HAS_CLAN = Text.NEGATIVE_PREFIX + "Você ".basic() + "já".negative() + " faz parte de um ".basic() + "clan".negative() + "!".basic()
	val ACCEPT_YOURSELF = Text.NEGATIVE_PREFIX + "Você ".basic() + "não".negative() + " precisa forçar o seu ".basic() + "clan".negative() + " a te ".basic() + "aceitar".negative() + ", basta que ".basic() + "você".positive() + " mesmo esteja contente com quem você ".basic() + "é".positive() + "!".basic()
	val NO_INVITATIONS = Text.NEGATIVE_PREFIX + "Você ".basic() + "não".negative() + " possui nenhum ".basic() + "convite".negative() + " válido desse ".basic() + "jogador".negative() + "!".basic()
	val NEW_CLAN_IS_FULL = Text.NEGATIVE_PREFIX + "Esse clan ".basic() + "já".negative() + " está ".basic() + "lotado".negative() + "!".basic()
	val ENTER_CLAN_FAIL = Text.NEGATIVE_PREFIX + "Não".negative() + " foi possível ".basic() + "entrar".negative() + " nesse clan!".basic()
	val YOU_ENTERED_A_CLAN = Text.POSITIVE_PREFIX + "Você ".basic() + "entrou".positive() + " para o clan ".basic() + "%s".positive() + " (".basic() + "%s".positive() + ")!".basic()
	val SOMEONE_ENTERED_YOUR_CLAN = Text.POSITIVE_PREFIX + "O jogador ".basic() + "%s".positive() + " entrou para o seu ".basic() + "clan".positive() + "!".basic()

	val LEADER_EXIT = Text.NEGATIVE_PREFIX + "Você é o ".basic() + "líder".negative() + " do seu ".basic() + "clan".negative() + ". Para ".basic() + "sair".negative() + ", escolha um novo líder ".basic() + "antes".negative() + " ou seja o ".basic() + "último".negative() + " membro a sair!".basic()
	val YOU_EXITED_A_CLAN = Text.NEGATIVE_PREFIX + "Você ".basic() + "saiu".negative() + " do clan ".basic() + "%s".negative() + " (".basic() + "%s".negative() + ")!".basic()
	val SOMEONE_EXITED_YOUR_CLAN = Text.NEGATIVE_PREFIX + "O jogador ".basic() + "%s".negative() + " saiu do seu ".basic() + "clan".negative() + "!".basic()

	val ENTERED_CLAN_CHAT = Text.POSITIVE_PREFIX + "Agora você ".basic() + "está".positive() + " no chat do seu ".basic() + "clan".positive() + "!".basic()
	val EXITED_CLAN_CHAT = Text.NEGATIVE_PREFIX + "Agora você ".basic() + "não".negative() + " está mais no chat do seu ".basic() + "clan".negative() + "!".basic()

	val AWAITING_API = arrayListOf<Player>()

	val INVITATIONS = HashMultimap.create<Player, ClanInvitation>()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) sender.sendMessage(USAGE)
		else {
			val gamer = Gamers.gamer(sender)

			when (args[0].toLowerCase()) {
				"help"     -> sender.sendMessage(HELP)
				"criar"    -> {
					if (gamer.clan != null) sender.sendMessage(EXIT_OLD_CLAN)
					else when {
						args.size < 3          -> sender.sendMessage(USAGE_CREATE)
						sender in AWAITING_API -> sender.sendMessage(WAIT_MORE)
						else                   -> {
							val name = args.copyOfRange(1, args.size - 1).joinToString(" ")
							val tag = args.last().toUpperCase()

							when {
								name.length > 16 -> sender.sendMessage(NAME_TOO_LONG)
								tag.length > 16  -> sender.sendMessage(TAG_TOO_LONG)
								else             -> {
									val clan = SimpleClan(SimplePrimitiveClan().apply {
										this.uuid = UUID.nameUUIDFromBytes(("Clan:" + name).toByteArray()).toString()
										this.name = name
										this.tag = tag
										this.leader = sender.uniqueId.toString()
										this.members = arrayOf(this.leader)
									})

									AWAITING_API.add(sender)

									val onNext = Consumer<Clan> {
										gamer.clan = clan
										WebAPI.saveProfiles(gamer)

										WebAPI.saveClans(clan)

										sender.sendMessage(CLAN_CREATED.format(clan.name, clan.tag))

										AWAITING_API.remove(sender)
									}

									val onError = Consumer<Throwable> {
										sender.sendMessage(CREATE_CLAN_FAIL)

										AWAITING_API.remove(sender)
									}

									Observable.just(clan).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
								}
							}
						}
					}
				}
				"lider"    -> {
					val clan = gamer.clan

					when {
						clan == null         -> sender.sendMessage(HAS_NO_CLAN)
						clan.leader != gamer -> sender.sendMessage(IS_NOT_LEADER)
						args.size < 2        -> sender.sendMessage(USAGE_LEADER)
						else                 -> {
							val player = Bukkit.getPlayerExact(args[1])

							when (player) {
								null   -> sender.sendMessage(PLAYER_NOT_FOUND.format(args[1]))
								sender -> sender.sendMessage(ALREADY_LEADER)
								else   -> {
									val invitedGamer = Gamers.gamer(player)

									when {
										invitedGamer.clan != clan -> sender.sendMessage(NOT_IN_CLAN)
										sender in AWAITING_API    -> sender.sendMessage(WAIT_MORE)
										else                      -> {
											AWAITING_API.add(sender)

											val onNext = Consumer<Clan> {
												clan.leader = invitedGamer

												WebAPI.saveClans(clan)

												sender.sendMessage(YOU_PROMOTED_SOMEONE.format(player.displayName.clearFormatting()))
												player.sendMessage(YOU_WERE_PROMOTED.format(sender.displayName.clearFormatting()))

												AWAITING_API.remove(sender)
											}

											val onError = Consumer<Throwable> {
												sender.sendMessage(SET_LEADER_FAIL)

												AWAITING_API.remove(sender)
											}

											Observable.just(clan).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
										}
									}
								}
							}
						}
					}
				}
				"convidar" -> {
					val clan = gamer.clan

					when {
						clan == null               -> sender.sendMessage(HAS_NO_CLAN)
						clan.leader != gamer       -> sender.sendMessage(IS_NOT_LEADER)
						args.size < 2              -> sender.sendMessage(USAGE_INVITE)
						clan.rawMembers().size > 4 -> sender.sendMessage(CLAN_IS_FULL)
						else                       -> {
							val player = Bukkit.getPlayerExact(args[1])

							when {
								player == null                                                                               -> sender.sendMessage(PLAYER_NOT_FOUND.format(args[1]))
								player == sender                                                                             -> sender.sendMessage(INVITE_YOURSELF)
								INVITATIONS[sender].any { it.player == player && it.expiresOn > System.currentTimeMillis() } -> sender.sendMessage(WAIT_INVITE)
								else                                                                                         -> {
									val invitedGamer = Gamers.gamer(player)
									val oldClan = invitedGamer.clan

									when {
										oldClan == clan -> sender.sendMessage(ALREADY_ON_CLAN)
										oldClan != null -> sender.sendMessage(ALREADY_HAS_CLAN)
										else            -> {
											sender.sendMessage(YOU_INVITED_SOMEONE.format(player.displayName.clearFormatting()))
											player.sendMessage(YOU_WERE_INVITED.format(sender.displayName.clearFormatting(), clan.name, clan.tag, player.displayName.clearFormatting()))

											INVITATIONS.put(sender, ClanInvitation(player, clan, System.currentTimeMillis() + 60000L))
										}
									}
								}
							}
						}
					}
				}
				"remover"  -> {
					val clan = gamer.clan

					when {
						clan == null           -> sender.sendMessage(HAS_NO_CLAN)
						clan.leader != gamer   -> sender.sendMessage(IS_NOT_LEADER)
						args.size < 2          -> sender.sendMessage(USAGE_REMOVE)
						sender in AWAITING_API -> sender.sendMessage(WAIT_MORE)
						else                   -> {
							val player = Bukkit.getPlayerExact(args[1])

							when (player) {
								null   -> {
									val onNext = Consumer<String> {
										val profile = MojangAPI.profile(it)

										if (profile == null) sender.sendMessage(PLAYER_NOT_FOUND.format(args[1]))
										else {
											val uuid = profile.id.addUuidDashes()

											if (uuid !in clan.rawMembers()) sender.sendMessage(NOT_IN_CLAN)
											else {
												clan.remove(UUID.fromString(uuid))
												WebAPI.saveClans(clan)

												sender.sendMessage(YOU_REMOVED_SOMEONE.format(profile.name))
											}
										}

										AWAITING_API.remove(sender)
									}

									val onError = Consumer<Throwable> {
										sender.sendMessage(REMOVE_FROM_CLAN_FAIL)

										AWAITING_API.remove(sender)
									}

									Observable.just(args[1]).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
								}
								sender -> sender.sendMessage(REMOVE_YOURSELF)
								else   -> {
									val removedGamer = Gamers.gamer(player)

									if (removedGamer !in clan.onlineMembers) {
										sender.sendMessage(NOT_IN_CLAN)
									} else {
										AWAITING_API.add(sender)

										val onNext = Consumer<Clan> {
											clan.remove(gamer)
											WebAPI.saveClans(clan)

											removedGamer.clan = null
											WebAPI.saveProfiles(gamer)

											player.sendMessage(YOU_REMOVED_SOMEONE.format(player.name))

											sender.sendMessage(YOU_WERE_REMOVED.format(clan.name, clan.tag))

											AWAITING_API.remove(sender)
										}

										val onError = Consumer<Throwable> {
											sender.sendMessage(REMOVE_FROM_CLAN_FAIL)

											AWAITING_API.remove(sender)
										}

										Observable.just(clan).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
									}
								}
							}
						}
					}
				}
				"aceitar"  -> {
					val oldClan = gamer.clan

					when {
						oldClan != null -> sender.sendMessage(HAS_CLAN)
						args.size < 2   -> sender.sendMessage(USAGE_ACCEPT)
						else            -> {
							val player = Bukkit.getPlayerExact(args[1])

							when (player) {
								null   -> sender.sendMessage(PLAYER_NOT_FOUND.format(args[1]))
								sender -> sender.sendMessage(ACCEPT_YOURSELF)
								else   -> {
									val clanInvitation = INVITATIONS[player].firstOrNull { it.player == sender && it.expiresOn > System.currentTimeMillis() }

									if (clanInvitation == null) {
										sender.sendMessage(NO_INVITATIONS)
									} else {
										val clan = clanInvitation.clan

										when {
											clan.rawMembers().size > 4 -> sender.sendMessage(NEW_CLAN_IS_FULL)
											sender in AWAITING_API     -> sender.sendMessage(WAIT_MORE)
											else                       -> {
												AWAITING_API.add(sender)

												val onNext = Consumer<Clan> {
													gamer.clan = clan
													WebAPI.saveProfiles(gamer)

													clan.add(gamer)
													WebAPI.saveClans(clan)

													sender.sendMessage(YOU_ENTERED_A_CLAN.format(clan.name, clan.tag))
													clan.leader?.player?.sendMessage(SOMEONE_ENTERED_YOUR_CLAN.format(sender.displayName.clearFormatting()))

													INVITATIONS.entries().forEach { if (it.key == player && it.value.player == sender) INVITATIONS.remove(it.key, it.value) }

													AWAITING_API.remove(sender)
												}

												val onError = Consumer<Throwable> {
													sender.sendMessage(ENTER_CLAN_FAIL)

													AWAITING_API.remove(sender)
												}

												Observable.just(clan).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
											}
										}
									}
								}
							}
						}
					}
				}
				"sair"     -> {
					val clan = gamer.clan

					when {
						clan == null                                       -> sender.sendMessage(HAS_NO_CLAN)
						clan.leader == gamer && clan.rawMembers().size > 1 -> sender.sendMessage(LEADER_EXIT)
						sender in AWAITING_API                             -> sender.sendMessage(WAIT_MORE)
						else                                               -> {
							AWAITING_API.add(sender)

							val onNext = Consumer<Clan> {
								if (clan.leader == gamer) clan.leader = null

								clan.remove(gamer)
								WebAPI.saveClans(clan)

								gamer.clan = null
								WebAPI.saveProfiles(gamer)

								sender.sendMessage(YOU_EXITED_A_CLAN.format(clan.name, clan.tag))
								clan.leader?.player?.sendMessage(SOMEONE_EXITED_YOUR_CLAN.format(sender.displayName.clearFormatting()))

								AWAITING_API.remove(sender)
							}

							val onError = Consumer<Throwable> {
								sender.sendMessage(ENTER_CLAN_FAIL)

								AWAITING_API.remove(sender)
							}

							Observable.just(clan).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
						}
					}

				}
				"chat"     -> {
					when {
						gamer.clan == null          -> sender.sendMessage(HAS_NO_CLAN)
						gamer.chat != EnumChat.CLAN -> {
							gamer.chat = EnumChat.CLAN

							sender.sendMessage(ENTERED_CLAN_CHAT)
						}
						else                        -> {
							gamer.chat = EnumChat.NORMAL

							sender.sendMessage(EXITED_CLAN_CHAT)
						}
					}

				}
				else       -> sender.sendMessage(USAGE)
			}
		}

		return false
	}

	class ClanInvitation(val player: Player, val clan: Clan, val expiresOn: Long) {

		override fun equals(other: Any?) = when {
			this === other                -> true
			javaClass != other?.javaClass -> false
			else                          -> {
				other as ClanInvitation

				when {
					player != other.player -> false
					clan != other.clan     -> false
					else                   -> true
				}
			}
		}

		override fun hashCode(): Int {
			var result = player.hashCode()

			result = 31 * result + clan.hashCode()

			return result
		}

		override fun toString(): String {
			return "ClanInvitation(player=$player, clan=$clan, expiresOn=$expiresOn)"
		}
	}
}
