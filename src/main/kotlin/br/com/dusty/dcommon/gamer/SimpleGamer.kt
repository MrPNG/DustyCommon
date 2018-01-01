package br.com.dusty.dcommon.gamer

import br.com.dusty.dcommon.store.EnumAdvantage
import br.com.dusty.dcommon.util.Tasks
import br.com.dusty.dcommon.util.protocol.EnumProtocolVersion
import br.com.dusty.dcommon.util.text.*
import com.sk89q.worldguard.protection.flags.DefaultFlag
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class SimpleGamer(override val player: Player): Gamer {

	override var displayName = player.displayName

	override var protocolVersion = EnumProtocolVersion.UNKNOWN

	override val advantages = arrayListOf<EnumAdvantage>()

	override var rank = EnumRank.NONE

	override var tag = rank
		set(value) {
			field = value

			val tag = value.format(displayName) + TextStyle.RESET

			player.displayName = tag
			player.playerListName = tag
		}

	override var visibleTo = EnumRank.DEFAULT
		set(visibleTo) {
			if (field == visibleTo) player.sendMessage(Text.NEUTRAL_PREFIX + "Você já está ".basic() + "visível".neutral() + " apenas para ".basic() + visibleTo.toString() + (if (visibleTo.hasNext()) " e acima!".basic() else "!"))
			else {
				field = visibleTo

				hideFromPlayers()

				if (rank.isHigherThanOrEquals(EnumRank.MOD)) player.sendMessage(Text.NEUTRAL_PREFIX + "Agora você está ".basic() + "visível".neutral() + " apenas para ".basic() + visibleTo.toString() + (if (visibleTo.hasNext()) " e acima!".basic() else "!"))
			}
		}

	override var mode = EnumMode.PLAY
		set(mode) {
			if (field != mode) {
				field = mode

				player.sendMessage(Text.NEUTRAL_PREFIX + "Agora você está no modo ".basic() + mode.name.neutral() + "!".basic())

				when (mode) {
					EnumMode.PLAY     -> {
						player.gameMode = GameMode.ADVENTURE

						clear()
						fly(false)

						visibleTo = EnumRank.DEFAULT
					}
				//TODO: Spectator mode
					EnumMode.SPECTATE -> {
						player.gameMode = GameMode.ADVENTURE

						clear()
						fly(false)

						visibleTo = rank
					}
					EnumMode.ADMIN    -> {
						player.gameMode = GameMode.CREATIVE

						fly(true)

						visibleTo = rank

						if (isCombatTagged()) combatPartner!!.kill(this) else removeCombatTag(false)
					}
				}

				//TODO: Scoreboards.update()
			}
		}

	override var chat = if (rank.isHigherThanOrEquals(EnumRank.MOD)) EnumChat.STAFF else EnumChat.NORMAL

	override var invincibility = -1L

	override var noFall = -1L

	override var freeze = -1L

	override var chatPartner: Gamer? = null

	override var chatSpies = arrayListOf<Player>()

	override var combatTask: BukkitTask? = null

	override var combatPartner: Gamer? = null
		set(value) {
			if (value != this) field = value
		}

	override var combatTag = -1L
		set(period) {
			combatTask?.cancel()

			if (period == -1L) field = period
			else {
				field = System.currentTimeMillis() + period

				if (!isCombatTagged()) player.sendMessage(Text.NEGATIVE_PREFIX + "Você ".basic() + "entrou".negative() + " em ".basic() + "combate".negative() + "!".basic())

				combatTask = Tasks.sync(Runnable { if (player.isOnline) removeCombatTag(true) }, period / 50L)
			}

			//TODO: updateScoreboard()
		}

	override fun hasAdvantage(advantage: EnumAdvantage) = advantage in advantages

	override fun shouldSee(gamer: Gamer) = rank.isHigherThanOrEquals(gamer.visibleTo)

	override fun hidePlayers() {
		for (otherGamer in GamerRegistry.onlineGamers()) {
			val otherPlayer = otherGamer.player

			if (!shouldSee(otherGamer)) player.hidePlayer(otherPlayer)
			else player.showPlayer(otherPlayer)
		}
	}

	override fun hideFromPlayers() {
		for (otherGamer in GamerRegistry.onlineGamers()) {
			val otherPlayer = otherGamer.player

			if (!otherGamer.shouldSee(this)) otherPlayer.hidePlayer(player)
			else otherPlayer.showPlayer(player)
		}
	}

	override fun isInvincible() = invincibility > System.currentTimeMillis()

	override fun removeInvincibility() {
		invincibility = -1
	}

	override fun isNoFall() = noFall > System.currentTimeMillis()

	override fun removeNoFall() {
		noFall = -1
	}

	override fun isFrozen() = freeze > System.currentTimeMillis()

	override fun removeFreeze() {
		freeze = -1
	}

	override fun isCombatTagged() = combatTag > System.currentTimeMillis()

	override fun removeCombatTag(announce: Boolean) {
		if (announce && isCombatTagged()) player.sendMessage(Text.POSITIVE_PREFIX + "Você ".basic() + "saiu".positive() + " de ".basic() + "combate".positive() + "!".basic())

		combatTag = -1L
	}

	override fun canInteract() = mode == EnumMode.PLAY && !Worlds.REGION_MANAGER!!.getApplicableRegions(player.location).allows(DefaultFlag.INVINCIBILITY)

	override fun canInteract(gamer: Gamer) = this != gamer && canUse() && gamer.mode == EnumMode.PLAY && gamer.player.canSee(player) && !Worlds.REGION_MANAGER!!.getApplicableRegions(gamer.player.location).allows(
			DefaultFlag.INVINCIBILITY)

	override fun kill(gamer: Gamer) {
		val killer = gamer.player

		player.run {
			playSound(player.location, Sound.ANVIL_LAND, 1F, 1F)
			sendMessage(Text.POSITIVE_PREFIX + "Você ".basic() + "matou".positive() + " o jogador ".basic() + killer.displayName.clearFormatting().positive() + "!".basic())
		}

		removeCombatTag(false)
		combatPartner = null


		killer.run {
			playSound(player.location, Sound.ANVIL_LAND, 1F, 1F)
			sendMessage(Text.NEGATIVE_PREFIX + "Você ".basic() + "foi morto".negative() + " pelo jogador ".basic() + player.displayName.clearFormatting().negative() + "!".basic())
		}

		gamer.run {
			removeCombatTag(false)
			combatPartner = null
		}
	}

	override fun clear() {
		player.run {
			health = player.maxHealth
			saturation = 0F
			foodLevel = 20
			exp = 0F
			level = 0
			fireTicks = 0

			inventory.clear()
			//TODO: inventory.armorContents = Inventories.NO_ARMOR

			activePotionEffects.forEach { removePotionEffect(it.type) }
		}

		removeFreeze()
		removeInvincibility()
		removeCombatTag(false)
	}

	override fun fly(fly: Boolean) {
		player.allowFlight = fly
		player.isFlying = fly
	}

	override fun equals(other: Any?) = when {
		this === other                                      -> true
		javaClass != other?.javaClass                       -> false
		player.uniqueId != (other as Gamer).player.uniqueId -> false
		else                                                -> true
	}

	override fun hashCode() = player.uniqueId.hashCode()
}
