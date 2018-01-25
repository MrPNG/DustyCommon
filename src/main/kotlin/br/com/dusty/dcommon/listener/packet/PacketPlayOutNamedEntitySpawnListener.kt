package br.com.dusty.dcommon.listener.packet

import br.com.dusty.dcommon.Main
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.protocol.Protocols
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent

object PacketPlayOutNamedEntitySpawnListener {

	fun registerListener() {
		Protocols.PROTOCOL_MANAGER!!.addPacketListener(object: PacketAdapter(Main.INSTANCE, PacketType.Play.Server.NAMED_ENTITY_SPAWN) {

			override fun onPacketSending(event: PacketEvent?) {
				event?.run {
					if (!Gamers.gamer(player).isAuthenticated || Gamers.GAMER_BY_UUID[packet.uuiDs.read(0)]?.isAuthenticated != true) event.isCancelled = true
				}
			}
		})
	}
}
