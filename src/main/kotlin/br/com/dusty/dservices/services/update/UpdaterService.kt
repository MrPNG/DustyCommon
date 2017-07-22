package br.com.dusty.dservices.services.update

import br.com.dusty.dservices.Main
import br.com.dusty.dservices.util.MessageUtils
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
        buildsDir.mkdirs()

        val buildFiles = buildsDir.listFiles()!!.filter { !it.isDirectory && it.name.endsWith(".jar") }

        val pluginFiles = pluginsDir.listFiles()!!.filter { !it.isDirectory && it.name.endsWith(".jar") }

        var updated = false

        pluginFiles@
        for (pluginFile in pluginFiles)
            for (buildFile in buildFiles)
                if (pluginFile.name == buildFile.name) {
                    if (!checkHash(pluginFile, buildFile)) {
                        Main.LOGGER
                                .info(MessageUtils.PREFIX + "Found an update for plugin file \"" + pluginFile
                                        .name + "\", updating...")

                        FileUtils.forceDelete(pluginFile)
                        FileUtils.moveFile(buildFile, pluginFile)

                        updated = true
                        Main.LOGGER
                                .info(MessageUtils.PREFIX + "Updated plugin file \"" + pluginFile.name + "\"")
                    } else {
                        FileUtils.forceDelete(buildFile)

                        Main.LOGGER
                                .info(MessageUtils.PREFIX + "No updates found for plugin file \"" + pluginFile
                                        .name + "\"")
                    }
                    continue@pluginFiles
                }

        return updated
    }

    private fun checkHash(pluginFile: File, buildFile: File): Boolean {
        val pluginHash = DigestUtils.md5(FileInputStream(pluginFile))
        val buildHash = DigestUtils.md5(FileInputStream(buildFile))

        return Arrays.equals(pluginHash, buildHash)
    }
}
