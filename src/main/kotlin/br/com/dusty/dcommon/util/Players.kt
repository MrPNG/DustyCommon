package br.com.dusty.dcommon.util

import br.com.dusty.dcommon.gamer.GamerRegistry

object Players {

	operator fun get(name: String) = GamerRegistry.onlineGamers().firstOrNull { it.displayName == name }?.player
}
