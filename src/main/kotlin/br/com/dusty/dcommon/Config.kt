package br.com.dusty.dcommon

import com.google.gson.GsonBuilder
import org.bukkit.Bukkit
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

object Config {

	val CONFIG_DIR = File(Bukkit.getWorldContainer(), "config").apply { if (!exists()) mkdirs() }
	val CONFIG_FILE = File(CONFIG_DIR, "common_config.json")

	val GSON = GsonBuilder().setPrettyPrinting().create()

	var data = Data()

	init {
		if (!CONFIG_DIR.exists()) CONFIG_DIR.mkdirs()
	}

	fun loadData() {
		if (CONFIG_FILE.exists()) data = GSON.fromJson(FileReader(CONFIG_FILE), data.javaClass)

		saveData()
	}

	fun saveData() {
		if (!CONFIG_FILE.exists()) CONFIG_FILE.createNewFile()

		PrintWriter(CONFIG_FILE).use { it.println(GSON.toJson(data)) }
	}

	data class Data(var slots: Int = 100, var serverStatus: EnumServerStatus = EnumServerStatus.OFFLINE)

	enum class EnumServerStatus {

		ONLINE,
		OFFLINE,
		MAINTENANCE
	}
}
