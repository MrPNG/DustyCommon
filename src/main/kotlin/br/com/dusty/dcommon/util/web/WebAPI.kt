package br.com.dusty.dcommon.util.web

import br.com.dusty.dcommon.clan.Clan
import br.com.dusty.dcommon.gamer.Gamer
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.message.BasicNameValuePair
import java.net.URLEncoder
import java.util.*

object WebAPI {

	var url = ""

	fun verifyEmail(email: String) = !HttpClients.JSON_PARSER.parse(HttpGet("$url?type=verifyemail&email=$email").response()).asJsonObject["status"].asBoolean

	fun createAccount(uuid: UUID,
	                  username: String,
	                  email: String,
	                  password: String) = HttpClients.JSON_PARSER.parse(HttpGet("$url?type=createaccount&data=" + URLEncoder.encode("[{\"uuid\":\"$uuid\",\"username\":\"$username\",\"email\":\"$email\",\"password\":\"$password\"}]",
	                                                                                                                                "UTF-8")).response()).asJsonObject["status"].asInt

	fun login(email: String) = HttpClients.JSON_PARSER.parse(HttpGet("$url?type=login&data=" + URLEncoder.encode("{\"email\":\"$email\"}", "UTF-8")).response()).asJsonObject.run {
		get("uuid").asString to get("password").asString
	}

	fun updateUsername(uuid: UUID, username: String) = HttpClients.JSON_PARSER.parse(HttpGet("$url?type=updateuser&data=" + URLEncoder.encode("{\"uuid\":\"$uuid\",\"username\":\"$username\"}",
	                                                                                                                                          "UTF-8")).response())

	fun loadProfile(uuid: UUID) = HttpGet("$url?type=perfil&uuidusty=$uuid").response()

	fun saveProfiles(vararg gamers: Gamer) = HttpPost(url).setEntities(BasicNameValuePair("type", "salvarperfil"),
	                                                                   BasicNameValuePair("data", HttpClients.GSON.toJson(gamers.map { it.toPrimitiveGamer() }))).response()

	fun loadClan(uuid: UUID) = HttpGet("$url?type=team&uuid=$uuid").response()

	fun saveClans(vararg clans: Clan) = HttpPost(url).setEntities(BasicNameValuePair("type", "salvartime"),
	                                                              BasicNameValuePair("data", HttpClients.GSON.toJson(clans.map { it.primitiveClan }))).response()

	fun bug(string: String) = HttpGet("https://api.dusty.com.br/reportbug.php?bug=$string").response()

	fun report(name: String, reporter: String, reason: String) = HttpGet("https://api.dusty.com.br/report.php?player=$name&reportby=$reporter&reason=${URLEncoder.encode(reason, "UTF-8")}").response()
}
