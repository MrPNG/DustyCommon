package br.com.dusty.dcommon.command

import br.com.dusty.dcommon.gamer.EnumRank
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

open class PlayerCustomCommand(rank: EnumRank, vararg aliases: String): CustomCommand(rank, *aliases) {

	override fun execute(sender: CommandSender, alias: String, args: Array<String>) = if (sender is Player && testPermission(sender)) execute(sender, alias, args) else true

	/**
	 * Método invocado quando este comando é executado por um [Player] autorizado ([testPermission]).
	 *
	 * @param sender [org.bukkit.entity.Player].
	 * @param alias  '/[alias]' que foi usado.
	 * @param args   Argumentos do comando, vindos da separação por espaços do comando enviado (i.e. '/[alias] [arg1] [arg2] [...]').
	 * @return **true**, se algo deu errado/não foi autorizado, **false** se tudo ocorreu como previsto.
	 */
	open fun execute(sender: Player, alias: String, args: Array<String>) = false

	override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>) = if (sender is Player && testPermission(sender)) tabComplete(sender, alias, args) else arrayListOf()

	open fun tabComplete(sender: Player, alias: String, args: Array<String>): MutableList<String>? = arrayListOf()
}
