package br.com.dusty.dcommon.services.update

import br.com.dusty.dcommon.Main
import br.com.dusty.dcommon.util.Messages
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.bukkit.Bukkit
import java.io.File
import java.io.FileInputStream
import java.util.*

object UpdaterService {

	fun updatePlugins(): Boolean {
		val rootDir = Bukkit.getWorldContainer()

		val pluginsDir = File(rootDir, "plugins")
		val buildsDir = File(rootDir, "builds")

		if (!buildsDir.exists()) buildsDir.mkdirs()

		val buildFiles = buildsDir.listFiles().filter { it.name.endsWith(".jar") }
		val pluginFiles = pluginsDir.listFiles().filter { it.name.endsWith(".jar") }

		var updated = false

		for (pluginFile in pluginFiles) {
			val buildFile = buildFiles.firstOrNull { it.name == pluginFile.name && !it.matches(pluginFile) } ?: continue

			Main.LOGGER.info(Messages.PREFIX + "Found an update for plugin file \"" + pluginFile.name + "\", updating...")

			FileUtils.forceDelete(pluginFile)
			FileUtils.moveFile(buildFile, pluginFile)

			updated = true

			Main.LOGGER.info(Messages.PREFIX + "Updated plugin file \"" + pluginFile.name + "\"")
		}

		return updated
	}

	fun File.matches(file: File) = Arrays.equals(DigestUtils.md5(FileInputStream(this)), DigestUtils.md5(FileInputStream(file)))
}
