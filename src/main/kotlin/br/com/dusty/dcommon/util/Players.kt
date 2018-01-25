package br.com.dusty.dcommon.util

import br.com.dusty.dcommon.gamer.Gamers

object Players {

	operator fun get(name: String) = Gamers.gamers().firstOrNull { it.displayName == name }?.player
}
