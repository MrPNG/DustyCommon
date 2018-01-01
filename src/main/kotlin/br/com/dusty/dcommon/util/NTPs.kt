package br.com.dusty.dcommon.util

import org.apache.commons.net.ntp.NTPUDPClient
import java.net.InetAddress

object NTPs {

	var time = 0L
		get() {
			if (field == 0L) field = NTPUDPClient().getTime(InetAddress.getByName("a.st1.ntp.br")).returnTime

			return field
		}
}
