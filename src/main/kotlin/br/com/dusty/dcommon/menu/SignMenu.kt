package br.com.dusty.dcommon.menu

import br.com.dusty.dcommon.Main
import br.com.dusty.dcommon.gamer.Gamer
import br.com.dusty.dcommon.util.protocol.Protocols
import br.com.dusty.dcommon.util.web.HttpClients
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.BlockPosition
import com.comphenix.protocol.wrappers.WrappedBlockData
import com.comphenix.protocol.wrappers.WrappedChatComponent
import org.bukkit.Material

open class SignMenu(val gamer: Gamer, val lines: Array<String>) {

	val player = gamer.player

	val location = player.location.run {
		y = 0.0

		toVector()
	}

	init {
		val packetBlockChange = PacketContainer(PacketType.Play.Server.BLOCK_CHANGE).apply {
			blockPositionModifier.write(0, BlockPosition(location))
			blockData.write(0, WrappedBlockData.createData(Material.SIGN_POST))
		}

		val packetUpdateSign = PacketContainer(PacketType.Play.Server.UPDATE_SIGN).apply {
			blockPositionModifier.write(0, BlockPosition(location))
			chatComponentArrays.write(0,
			                          arrayOf(WrappedChatComponent.fromText(lines[0]),
			                                  WrappedChatComponent.fromText(lines[1]),
			                                  WrappedChatComponent.fromText(lines[2]),
			                                  WrappedChatComponent.fromText(lines[3])))
		}

		val packetOpenSignEditor = PacketContainer(PacketType.Play.Server.OPEN_SIGN_EDITOR).apply {
			blockPositionModifier.write(0, BlockPosition(location))
		}

		Protocols.PROTOCOL_MANAGER!!.run {
			sendServerPacket(player, packetBlockChange)
			sendServerPacket(player, packetUpdateSign)
			sendServerPacket(player, packetOpenSignEditor)

			addPacketListener(object: PacketAdapter(Main.INSTANCE, PacketType.Play.Client.UPDATE_SIGN) {

				override fun onPacketReceiving(event: PacketEvent?) {
					val packetAdapter = this

					event?.run {
						if (packet.blockPositionModifier.read(0).toLocation(player.world) == location) {
							onUpdate(packet.chatComponentArrays.read(0).map { HttpClients.JSON_PARSER.parse(it.json).asJsonObject["text"].asString }.toTypedArray())

							removePacketListener(packetAdapter)
						}
					}
				}
			})
		}
	}

	open fun onUpdate(lines: Array<String>) {}
}
