package br.com.dusty.dcommon.util.protocol

import br.com.dusty.dcommon.util.text.TextColor
import br.com.dusty.dcommon.util.text.TextColor.*

enum class EnumProtocolSafety constructor(val color: TextColor) {

	EXCELLENT(DARK_GREEN),
	GOOD(GREEN),
	REGULAR(YELLOW),
	BAD(RED),
	TERRIBLE(DARK_RED)
}
