package br.com.dusty.dcommon.scoreboard

import br.com.dusty.dcommon.gamer.Gamer

interface Scoreboard {

	var labels: Array<String>

	var labelsOld: Array<String>

	fun create(gamer: Gamer)

	fun update(gamer: Gamer)

	fun update(gamers: Collection<Gamer>)

	fun clear(gamer: Gamer)
}