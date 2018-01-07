package br.com.dusty.dcommon.util.protocol

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import org.bukkit.entity.Player
import us.myles.ViaVersion.api.Via
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

object Protocols {

	val VIA_VERSION_API = Via.getAPI()

	val OBC_PACKAGE = "org.bukkit.craftbukkit."
	val NMS_PACKAGE = "net.minecraft.server."
	val VERSION = "v1_8_R3"

	val PROTOCOL_SUPPORT_PACKAGE = "protocolsupport.api."

	var PROTOCOL_MANAGER: ProtocolManager? = null
		get() {
			if (field == null) field = ProtocolLibrary.getProtocolManager()

			return field
		}

	var constructor_ChatMessage: Constructor<*>

	var method_CraftPlayer_getHandle: Method

	var field_EntityPlayer_ping: Field
	var field_EntityPlayer_playerConnection: Field

	var method_PlayerConnection_sendPacket: Method

	var method_ProtocolSupportAPI_getProtocolVersion: Method
	var method_ProtocolVersion_getId: Method

	init {
		val class_ChatMessage = NMSClass("ChatMessage")
		constructor_ChatMessage = class_ChatMessage.getDeclaredConstructor(String::class.java, Array<Any>::class.java)

		val class_Packet = NMSClass("Packet")

		val class_CraftPlayer = OBCClass("entity.CraftPlayer")
		method_CraftPlayer_getHandle = class_CraftPlayer.getDeclaredMethod("getHandle")

		val class_EntityPlayer = NMSClass("EntityPlayer")
		field_EntityPlayer_ping = class_EntityPlayer.getDeclaredField("ping")
		field_EntityPlayer_playerConnection = class_EntityPlayer.getDeclaredField("playerConnection")

		val class_PlayerConnection = NMSClass("PlayerConnection")
		method_PlayerConnection_sendPacket = class_PlayerConnection.getDeclaredMethod("sendPacket", class_Packet)

		val class_ProtocolSupportAPI = Class.forName(PROTOCOL_SUPPORT_PACKAGE + "ProtocolSupportAPI")
		method_ProtocolSupportAPI_getProtocolVersion = class_ProtocolSupportAPI.getDeclaredMethod("getProtocolVersion", Player::class.java)

		val class_ProtocolVersion = Class.forName(PROTOCOL_SUPPORT_PACKAGE + "ProtocolVersion")
		method_ProtocolVersion_getId = class_ProtocolVersion.getDeclaredMethod("getId")
	}

	fun getAccessibleField(clazz: Class<*>, name: String): Field {
		val field = clazz.getDeclaredField(name)
		field.isAccessible = true

		return field
	}

	fun NMSClass(name: String) = Class.forName(NMS_PACKAGE + VERSION + "." + name)

	fun OBCClass(name: String) = Class.forName(OBC_PACKAGE + VERSION + "." + name)

	fun chatMessage(s: String): Any = constructor_ChatMessage.newInstance(s, arrayOfNulls<Any>(0))

	fun ping(player: Player) = field_EntityPlayer_ping[method_CraftPlayer_getHandle.invoke(player)] as Int? ?: -1

	fun protocolVersion(player: Player) = method_ProtocolVersion_getId.invoke(method_ProtocolSupportAPI_getProtocolVersion.invoke(null, player)) as Int? ?: -1

	fun sendPacket(object_Packet: Any, vararg players: Player) {
		players.asSequence().map { method_CraftPlayer_getHandle.invoke(it) }.map { field_EntityPlayer_playerConnection[it] }.forEach {
			method_PlayerConnection_sendPacket.invoke(it, object_Packet)
		}
	}
}
