package br.com.dusty.dcommon.command

import br.com.dusty.dcommon.command.gameplay.ClanCommand
import br.com.dusty.dcommon.command.gameplay.FakeCommand
import br.com.dusty.dcommon.command.gameplay.ReportCommand
import br.com.dusty.dcommon.command.gameplay.TagCommand
import br.com.dusty.dcommon.command.override.StopCommand
import br.com.dusty.dcommon.command.override.TellCommand
import br.com.dusty.dcommon.command.staff.*
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import java.util.*

object Commands {

	/**
	 * Prefixo de todos os comandos encontrado ao pressionar 'TAB' no chat.
	 */
	val PREFIX = "dusty"

	val COMMAND_MAP: CommandMap

	/**
	 * [ArrayList] que contém todos os [CustomCommand] a serem/já registrados pelo plugin.
	 */
	val CUSTOM_COMMANDS = arrayListOf<CustomCommand>()

	init {
		val field_commandMap = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
		field_commandMap.isAccessible = true
		COMMAND_MAP = field_commandMap.get(Bukkit.getServer()) as CommandMap
	}

	/**
	 * Registra todos os [CustomCommand] da [ArrayList] CUSTOM_COMMANDS.
	 */
	fun registerAll() {
		//Usage: CUSTOM_COMMANDS.add(FooCommand)

		//Staff
		CUSTOM_COMMANDS.add(AdminCommand)
		CUSTOM_COMMANDS.add(ChatCommand)
		CUSTOM_COMMANDS.add(ConfigCommand)
		CUSTOM_COMMANDS.add(DebugCommand)
		CUSTOM_COMMANDS.add(FakeListCommand)
		CUSTOM_COMMANDS.add(FlyCommand)
		CUSTOM_COMMANDS.add(InvSeeCommand)
		CUSTOM_COMMANDS.add(IpCheckCommand)
		CUSTOM_COMMANDS.add(ProtocolCommand)
		CUSTOM_COMMANDS.add(RamCommand)
		CUSTOM_COMMANDS.add(SpyCommand)
		CUSTOM_COMMANDS.add(SyncCommand)
		CUSTOM_COMMANDS.add(StaffChatCommand)
		CUSTOM_COMMANDS.add(VisInvisCommand)

		//Gameplay
		CUSTOM_COMMANDS.add(ClanCommand)
		CUSTOM_COMMANDS.add(FakeCommand)
		CUSTOM_COMMANDS.add(ReportCommand)
		CUSTOM_COMMANDS.add(TagCommand)

		//Overwrite
		CUSTOM_COMMANDS.add(StopCommand)
		CUSTOM_COMMANDS.add(TellCommand)

		COMMAND_MAP.registerAll(PREFIX, CUSTOM_COMMANDS as List<Command>?)
	}
}
