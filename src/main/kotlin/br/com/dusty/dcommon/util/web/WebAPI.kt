package br.com.dusty.dcommon.util.web

import br.com.dusty.dcommon.clan.Clan
import br.com.dusty.dcommon.gamer.Gamer
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.message.BasicNameValuePair
import java.util.*

object WebAPI {

	var url = ""

	fun loadProfile(uuid: UUID) = HttpGet(url + "?type=perfil&uuid=" + uuid).response()

	fun saveProfiles(vararg gamers: Gamer) = HttpPost(url).setEntities(BasicNameValuePair("type", "salvarperfil"),
	                                                                   BasicNameValuePair("dataperfil", HttpClients.GSON.toJson(gamers.map { it.primitiveGamer }))).response()

	fun loadClan(uuid: UUID) = HttpGet(url + "?type=perfilclan&uuid=" + uuid.toString()).response()

	fun saveClans(vararg clans: Clan) = HttpPost(url).setEntities(BasicNameValuePair("type", "salvarclan"),
	                                                              BasicNameValuePair("dataclan", HttpClients.GSON.toJson(clans.map { it.primitiveClan }))).response()

	fun bug(string: String) = HttpGet("http://api.dusty.com.br/reportbug.php?bug=$string").response()

	fun report(name: String, reporter: String, reason: String) = HttpGet("http://api.dusty.com.br/report.php?player=$name&reportby=$reporter&reason=${reason.replace(" ", "%20")}").response()
}
