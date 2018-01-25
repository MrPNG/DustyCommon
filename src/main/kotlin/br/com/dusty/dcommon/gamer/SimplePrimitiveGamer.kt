package br.com.dusty.dcommon.gamer

import br.com.dusty.dcommon.Config

open class SimplePrimitiveGamer: PrimitiveGamer {

	override var uuid = ""

	override var id = -1

	override var clan = ""

	override fun toJson() = Config.GSON.toJson(this)
}
