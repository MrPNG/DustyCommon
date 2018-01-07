package br.com.dusty.dcommon.clan

class SimplePrimitiveClan: PrimitiveClan {

	override var uuid: String = ""

	override var name: String = ""

	override var tag: String = ""

	override var leader: String = ""

	override var members: Array<String> = arrayOf()

	override fun equals(other: Any?) = when {
		this === other                              -> true
		javaClass != other?.javaClass               -> false
		uuid != (other as SimplePrimitiveClan).uuid -> false
		else                                        -> true
	}

	override fun hashCode() = uuid.hashCode()
}
