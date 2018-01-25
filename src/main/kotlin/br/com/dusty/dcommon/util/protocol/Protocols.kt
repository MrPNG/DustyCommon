package br.com.dusty.dcommon.util.protocol

import br.com.dusty.dcommon.util.stdlib.Reflections.NMSClass
import br.com.dusty.dcommon.util.stdlib.Reflections.OBCClass
import br.com.dusty.dcommon.util.stdlib.getAccessibleField
import br.com.dusty.dcommon.util.stdlib.setFinal
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import net.minecraft.server.v1_8_R3.LocaleLanguage
import org.bukkit.entity.Player
import protocolsupport.api.ProtocolSupportAPI
import us.myles.ViaVersion.api.Via
import java.util.*

object Protocols {

	val VIA_VERSION_API = Via.getAPI()

	var PROTOCOL_MANAGER: ProtocolManager? = null
		get() {
			if (field == null) field = ProtocolLibrary.getProtocolManager()

			return field
		}

	val class_ChatMessage = NMSClass("ChatMessage")
	val constructor_ChatMessage = class_ChatMessage.getDeclaredConstructor(String::class.java, Array<Any>::class.java)

	val class_Packet = NMSClass("Packet")

	val class_GameProfile = Class.forName("com.mojang.authlib.GameProfile")
	val field_GameProfile_id = class_GameProfile.getAccessibleField("id")

	val class_CraftPlayer = OBCClass("entity.CraftPlayer")
	val method_CraftPlayer_getHandle = class_CraftPlayer.getDeclaredMethod("getHandle")

	val class_Entity = NMSClass("Entity")
	val field_Entity_uniqueID = class_Entity.getAccessibleField("uniqueID")

	val class_EntityHuman = NMSClass("EntityHuman")
	val field_EntityHuman_bH = class_EntityHuman.getAccessibleField("bH")

	val class_EntityPlayer = NMSClass("EntityPlayer")
	val field_EntityPlayer_locale = class_EntityPlayer.getDeclaredField("locale")
	val field_EntityPlayer_ping = class_EntityPlayer.getDeclaredField("ping")
	val field_EntityPlayer_playerConnection = class_EntityPlayer.getDeclaredField("playerConnection")

	val class_PlayerConnection = NMSClass("PlayerConnection")
	val method_PlayerConnection_sendPacket = class_PlayerConnection.getDeclaredMethod("sendPacket", class_Packet)

	fun changeUuid(player: Player, uuid: UUID) {
		val entityPlayer = method_CraftPlayer_getHandle.invoke(player)

		field_Entity_uniqueID.set(entityPlayer, uuid)

		val gameProfile_EntityHuman = field_EntityHuman_bH[entityPlayer]
		field_GameProfile_id.setFinal(gameProfile_EntityHuman, uuid)

		/*val gamer = Gamers.gamer(player)

		val gameProfileNew = WrappedGameProfile.fromPlayer(player)
		val ping = Protocols.ping(player)
		val gameMode = EnumWrappers.NativeGameMode.fromBukkit(if (player.gameMode == null) GameMode.ADVENTURE else player.gameMode)

		val packetPlayOutPlayerInfoAdd = PacketContainer(PacketType.Play.Server.PLAYER_INFO).apply {
			playerInfoAction.write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER)
			playerInfoDataLists.write(0, arrayListOf(PlayerInfoData(gameProfileNew, ping, gameMode, WrappedChatComponent.fromText(player.name))))
		}

		val packetPlayOutNamedEntitySpawn = PacketContainer(PacketType.Play.Server.NAMED_ENTITY_SPAWN).apply {
			integers.write(0, player.entityId)
			uuiDs.write(0, player.uniqueId)
			integers.write(1, (player.location.x * 32.0).toInt())
			integers.write(2, (player.location.y * 32.0).toInt())
			integers.write(3, (player.location.z * 32.0).toInt())
			bytes.write(0, (player.location.yaw * 256.0 / 360.0).toByte())
			bytes.write(1, (player.location.pitch * 256.0 / 360.0).toByte())
			integers.write(4, 0)
			dataWatcherModifier.write(0, WrappedDataWatcher.getEntityWatcher(player))
		}

		Protocols.PROTOCOL_MANAGER!!.run {
			Gamers.gamers().filter { it.shouldSee(gamer) }.forEach {
				val otherPlayer = it.player

				sendServerPacket(otherPlayer, packetPlayOutPlayerInfoAdd)
				sendServerPacket(otherPlayer, packetPlayOutNamedEntitySpawn)
			}
		}*/
	}

	fun chatMessage(s: String): Any = constructor_ChatMessage.newInstance(s, arrayOfNulls<Any>(0))

	fun translate(string: String, player: Player): String {
		val entityPlayer = method_CraftPlayer_getHandle.invoke(player)
		val locale = field_EntityPlayer_locale[entityPlayer]

		return string
	}

	fun ping(player: Player) = field_EntityPlayer_ping[method_CraftPlayer_getHandle.invoke(player)] as Int? ?: -1

	fun protocolVersion(player: Player) = ProtocolSupportAPI.getProtocolVersion(player).id as Int? ?: -1

	fun sendPacket(object_Packet: Any, vararg players: Player) {
		players.asSequence().map { method_CraftPlayer_getHandle.invoke(it) }.map { field_EntityPlayer_playerConnection[it] }.forEach {
			method_PlayerConnection_sendPacket.invoke(it, object_Packet)
		}
	}
}
