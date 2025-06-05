package com.example.drawai.api

import okhttp3.Dns
import java.net.Inet6Address
import java.net.InetAddress

class CustomDns : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        return try {
            Dns.SYSTEM.lookup(hostname).filterNot { it is Inet6Address }
        } catch (e: Exception) {
            Dns.SYSTEM.lookup(hostname)
        }
    }
}