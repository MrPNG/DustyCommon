package br.com.dusty.dcommon.util.web

import org.apache.http.client.methods.HttpGet

object MinecraftAPI {

	val URL_SIMPLE_PROFILE_BY_UUID = "https://use.gameapis.net/mc/player/name/"
	val URL_SIMPLE_PROFILE_BY_NAME = "https://use.gameapis.net/mc/player/uuid/"
	val URL_PROFILE_BY_UUID = "https://use.gameapis.net/mc/player/profile/"
	val URL_PROFILE_BY_NAME = "https://use.gameapis.net/mc/player/profile/"

	fun simpleProfile(string: String) = HttpGet((if (string.length > 16) URL_SIMPLE_PROFILE_BY_UUID else URL_SIMPLE_PROFILE_BY_NAME) + string).response()?.run {
		HttpClients.GSON.fromJson(this, SimpleProfile::class.java)
	}

	fun profile(string: String) = HttpGet((if (string.length > 16) URL_PROFILE_BY_UUID else URL_PROFILE_BY_NAME) + string).response()?.run {
		HttpClients.GSON.fromJson(this, Profile::class.java)
	}

	data class SimpleProfile(val name: String, val id: String, val uuid_formatted: String)

	data class Profile(val id: String,
	                   val uuid_formatted: String,
	                   val name: String,
	                   val properties: Properties,
	                   val properties_decoded: PropertiesDecoded,
	                   val expiresAt: Long,
	                   val expiresAtHR: String)

	data class Properties(val name: String, val value: String, val signature: String)

	data class PropertiesDecoded(val timestamp: Long, val profileId: String, val profileName: String, val signatureRequired: Boolean, val textures: Textures)

	data class Textures(val SKIN: Texture, val CAPE: Texture)

	data class Texture(val url: String)
}
