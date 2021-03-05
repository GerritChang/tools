package top.gerritchang.tools.utils

import java.net.InetAddress
import java.net.NetworkInterface

class MacUtils {
    fun getSystemMac(): String {
        return try {
            val OSName = System.getProperty("os.name")     //Get Operating System Name
            if (OSName.contains("Windows")) {
                getMAC4Windows()                           // Call if OS is Windows
            } else {
                var mac = getMAC4Linux("eth0")
                if (mac == null) {
                    mac = getMAC4Linux("eth1")
                    if (mac == null) {
                        mac = getMAC4Linux("eth2")
                        if (mac == null) {
                            mac = getMAC4Linux("usb0")
                        }
                    }
                }
                mac
            }
        } catch (E: Exception) {
            System.err.println("System Mac Exp : " + E.message).toString()
        }
    }

    /**
     * Method for get MAc of Linux Machine
     */
    fun getMAC4Linux(name: String): String {
        return try {
            val network = NetworkInterface.getByName(name)
            val mac = network.hardwareAddress
            val sb = StringBuilder()
            for (i in mac.indices) {
                sb.append(String.format("%02X%s", mac[i], if (i < mac.size - 1) "-" else ""))
            }
            sb.toString()
        } catch (E: Exception) {
            System.err.println("System Linux MAC Exp : " + E.message).toString()
        }
    }

    /**
     * Method for get Mac Address of Windows Machine
     */
    fun getMAC4Windows(): String {
        return try {
            val addr = InetAddress.getLocalHost()
            val network = NetworkInterface.getByInetAddress(addr)
            val mac = network.hardwareAddress
            val sb = StringBuilder()
            for (i in mac.indices) {
                sb.append(String.format("%02X%s", mac[i], if (i < mac.size - 1) "-" else ""))
            }
            sb.toString()
        } catch (E: Exception) {
            System.err.println("System Windows MAC Exp : " + E.message).toString()
        }
    }
}
