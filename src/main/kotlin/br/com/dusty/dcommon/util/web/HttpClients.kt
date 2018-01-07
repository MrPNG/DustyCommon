package br.com.dusty.dcommon.util.web

import br.com.dusty.dcommon.util.web.HttpClients.HTTP_CLIENT
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.apache.http.NameValuePair
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils

fun HttpPost.setEntities(vararg pairs: NameValuePair) = this.apply {
	val list = arrayListOf<NameValuePair>()
	list.addAll(pairs)

	entity = UrlEncodedFormEntity(list)
}

fun HttpGet.response() = HTTP_CLIENT.execute(this).entity?.run { EntityUtils.toString(this) }

fun HttpPost.response() = HTTP_CLIENT.execute(this).entity?.run { EntityUtils.toString(this) }

object HttpClients {

	val HTTP_CLIENT = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(10000).build()).build()

	val GSON = Gson()
	val JSON_PARSER = JsonParser()
}
