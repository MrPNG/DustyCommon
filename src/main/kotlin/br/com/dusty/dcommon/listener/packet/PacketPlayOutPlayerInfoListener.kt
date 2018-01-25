package br.com.dusty.dcommon.listener.packet

import br.com.dusty.dcommon.Main
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.protocol.Protocols
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent

object PacketPlayOutPlayerInfoListener {

	fun registerListener() {
		Protocols.PROTOCOL_MANAGER!!.addPacketListener(object: PacketAdapter(Main.INSTANCE, PacketType.Play.Server.PLAYER_INFO) {

			override fun onPacketSending(event: PacketEvent?) {
				event?.run {
					if (!Gamers.gamer(player).isAuthenticated) event.isCancelled = true
					else packet.playerInfoDataLists.write(0, packet.playerInfoDataLists.read(0).filter { Gamers.GAMER_BY_UUID[it.profile.uuid]?.isAuthenticated == true })
				}
			}
		})
	}
}
