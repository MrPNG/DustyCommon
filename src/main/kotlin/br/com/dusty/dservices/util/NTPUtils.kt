package br.com.dusty.dservices.util

import org.apache.commons.net.ntp.NTPUDPClient
import org.apache.commons.net.ntp.TimeInfo

import java.io.IOException
import java.net.InetAddress

object NTPUtils {

    private var t: Long = 0

    val time: Long
        get() {
            if (t == 0L) {
                val inetAddress = InetAddress.getByName("a.st1.ntp.br")

                val ntpudpClient = NTPUDPClient()
                val timeInfo = ntpudpClient.getTime(inetAddress)
                t = timeInfo.returnTime
            }
            
            return t
        }
}
