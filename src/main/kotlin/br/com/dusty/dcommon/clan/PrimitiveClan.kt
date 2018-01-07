package br.com.dusty.dcommon.clan

interface PrimitiveClan {

	var uuid: String

	var name: String

	var tag: String

	var leader: String

	var members: Array<String>
}