package br.com.dusty.dcommon.util.stdlib

import br.com.dusty.dcommon.Main
import org.bukkit.ChatColor
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder
import java.util.*

/**
 * Divide uma [String] em outras (não-formatadas) de tamanho máximo 'max', inserindo-as, em ordem, em uma [ArrayList].
 *
 * @param s
 * @param max
 * @return
 */
fun String.fancySplit(max: Int): ArrayList<String> {
	val arrayList = arrayListOf<String>()

	if (length > max) {
		val fragments = this.clearFormatting().split(" ")

		var i = 0
		while (i < fragments.size) {
			val fragment = StringBuilder()
			fragment.append(fragments[i])

			while (i + 1 < fragments.size && fragment.length + 1 + fragments[i + 1].length <= max) {
				fragment.append(" ")
				fragment.append(fragments[i + 1])
				i++
			}

			arrayList.add(fragment.toString())
			i++
		}
	} else {
		arrayList.add(this)
	}

	return arrayList
}

/**
 * Formata uma [Collection] de [String] por extenso, separando-a por vírgulas.
 *
 * @param collection
 * @return
 */
fun Collection<String>.format(): String {
	val iterator = this.iterator()

	return buildString {
		if (iterator.hasNext()) append(iterator.next())

		while (iterator.hasNext()) append(", ").append(iterator.next())
	}
}

/**
 * Cria uma [String] que indica uma duração de tempo com dias, horas, minutos e segundos.
 *
 * @param millis
 * @return
 */
fun Long.formatPeriod() = PeriodFormatterBuilder().appendDays().appendSuffix("d").appendSeparator(" ").appendHours().appendSuffix("h").appendSeparator(" ").appendMinutes().appendSuffix("min").appendSeparator(
		" ").appendSeconds().appendSuffix("s").toFormatter().print(Period(this))

/**
 * Cria uma [ArrayList] contendo todas as [String]'s do parâmetro 'arrayList' que iniciarem com o parâmetro 'start'.
 *
 * @param start
 * @param arrayList
 * @return
 */
fun ArrayList<String>.sortOut(start: String) = filter { it.startsWith(start, true) }.sorted()

/**
 * Remove qualquer formatação de uma [String], se houver.
 *
 * @param string
 * @return [String] sem formatação.
 */
fun String.clearFormatting() = ChatColor.stripColor(this)

fun String.addUuidDashes() = if (length == 32) replace("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex(), "$1-$2-$3-$4-$5") else this

fun String.removeUuidDashes() = if (length == 36) replace("-", "") else this

fun String.forceSubstring(startIndex: Int, endIndex: Int) = if (lastIndex > endIndex) substring(startIndex, endIndex) else this

fun String.toCamelCase() = split('_').joinToString(" ") { it.toLowerCase().capitalize() }

object Strings {

	val ALPHANUMERIC = "ABCDEFGHIKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray()

	/**
	 * Gera uma [String] alfanumérica aleatória com diferenciação entre maiúsculas e minúsculas.
	 *
	 * @param length
	 * @return
	 */
	fun randomString(length: Int) = buildString { for (i in 0 until length) append(ALPHANUMERIC[Main.RANDOM.nextInt(ALPHANUMERIC.size)]) }
}
