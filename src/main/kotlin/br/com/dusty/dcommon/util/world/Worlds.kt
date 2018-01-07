package br.com.dusty.dcommon.util.world

import br.com.dusty.dcommon.gamer.GamerRegistry
import com.sk89q.worldguard.bukkit.WGBukkit
import com.sk89q.worldguard.protection.managers.RegionManager
import org.apache.commons.io.FileUtils
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.entity.Player
import java.io.File

object Worlds {

	var REGION_MANAGER: RegionManager? = null
		get() {
			if (field == null) field = WGBukkit.getPlugin().getRegionManager(WORLD)

			return field
		}

	val WORLDS_DIR = File(Bukkit.getWorldContainer(), "worlds")

	val WORLD
		get() = Bukkit.getWorlds()[0]

	fun exists(name: String) = File(WORLDS_DIR, name.toLowerCase()).exists()

	fun isLoaded(name: String) = Bukkit.getWorld(name) != null

	fun load(name: String, edit: Boolean): World? = when {
		!exists(name) -> null
		edit          -> Bukkit.createWorld(WorldCreator("worlds/" + name))
		else          -> {
			FileUtils.copyDirectory(File(WORLDS_DIR, name), File(Bukkit.getWorldContainer(), name))

			Bukkit.createWorld(WorldCreator(name))
		}
	}

	fun unload(name: String, rollback: Boolean): Boolean {
		val world = Bukkit.getWorld(name) ?: return false

		world.livingEntities.filter { it is Player }.forEach { GamerRegistry.gamer(it as Player).run { if (isCombatTagged()) removeCombatTag(false) } }

		return Bukkit.unloadWorld(world, !rollback)
	}

	fun rollback(name: String) = unload(name, true).apply { if (this) FileUtils.deleteDirectory(File(Bukkit.getWorldContainer(), name)) }
}
