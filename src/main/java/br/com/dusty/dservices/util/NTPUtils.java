package br.com.dusty.dservices.util;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;

public class NTPUtils {
	
	private static long t = 0;
	
	public static long getTime() throws IOException {
		if(t == 0){
			InetAddress inetAddress = InetAddress.getByName("a.st1.ntp.br");
			
			NTPUDPClient ntpudpClient = new NTPUDPClient();
			TimeInfo timeInfo = ntpudpClient.getTime(inetAddress);
			t = timeInfo.getReturnTime();
		}
		return t;
	}
}
