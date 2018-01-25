package br.com.dusty.dcommon.command

import br.com.dusty.dcommon.command.gameplay.*
import br.com.dusty.dcommon.command.override.StopCommand
import br.com.dusty.dcommon.command.override.TellCommand
import br.com.dusty.dcommon.command.staff.*
import br.com.dusty.dcommon.util.stdlib.getAccessibleField
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap

object Commands {

	val PREFIX = "dusty"

	val COMMAND_MAP: CommandMap

	val CUSTOM_COMMANDS = arrayListOf<CustomCommand>()

	init {
		val field_commandMap = Bukkit.getServer().javaClass.getAccessibleField("commandMap")

		COMMAND_MAP = field_commandMap.get(Bukkit.getServer()) as CommandMap
	}

	fun register(command: CustomCommand) {
		CUSTOM_COMMANDS.add(command)

		COMMAND_MAP.register(PREFIX, command)
	}

	fun registerAll(commands: List<CustomCommand>) {
		CUSTOM_COMMANDS.addAll(commands)

		COMMAND_MAP.registerAll(PREFIX, commands)
	}

	fun registerDefault() {
		registerAll(arrayListOf(
				//Gameplay
				AuthenticationCommand,
				ClanCommand,
				FakeCommand,
				ReportCommand,
				TagCommand,

				//Override
				StopCommand,
				TellCommand,

				//Staff
				AdminCommand,
				ChatCommand,
				ConfigCommand,
				DebugCommand,
				FakeListCommand,
				FlyCommand,
				InvSeeCommand,
				IpCheckCommand,
				ProtocolCommand,
				RamCommand,
				SpyCommand,
				SyncCommand,
				StaffChatCommand,
				VisInvisCommand))
	}
}
