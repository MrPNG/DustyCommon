package br.com.dusty.dcommon.util.web

import com.google.gson.reflect.TypeToken
import org.apache.http.client.methods.HttpGet

object MojangAPI {

	val PROFILES_TYPE_TOKEN = object: TypeToken<Collection<Profile>>() {}.type

	val URL_PROFILES_BY_UUID = "https://api.mojang.com/user/profiles/%s/names"
	val URL_PROFILE_BY_NAME = "https://api.mojang.com/users/profiles/minecraft/%s"

	fun profile(string: String) = if (string.length > 16) HttpGet(URL_PROFILES_BY_UUID.format(string)).response()?.run {
		(HttpClients.GSON.fromJson(this, PROFILES_TYPE_TOKEN) as ArrayList<Profile>).last()
	}
	else HttpGet(URL_PROFILE_BY_NAME.format(string)).response()?.run { HttpClients.GSON.fromJson(this, Profile::class.java) }

	fun profiles(string: String) = HttpGet(URL_PROFILES_BY_UUID + string).response()?.run { HttpClients.GSON.fromJson(this, PROFILES_TYPE_TOKEN) as ArrayList<Profile> }

	data class Profile(val id: String, val name: String, val changedToAt: Long, val legacy: Boolean, val demo: Boolean)
}
