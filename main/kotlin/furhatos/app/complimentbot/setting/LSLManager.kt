package furhatos.app.complimentbot.setting

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.net.HttpURLConnection
import java.net.URL

object LslManager {

    private const val PC_IP = "10.227.68.26"   // Replace with your computer’s LAN IP
    private const val PC_PORT = 5000

    fun send(marker: Int) {
        GlobalScope.launch {
            try {
                val url = URL("http://$PC_IP:$PC_PORT/lsl?marker=$marker")
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"
                    inputStream.bufferedReader().use { it.readText() } // optional
                }
                println("Sent marker $marker to PC")
            } catch (e: Exception) {
                println("Failed to send marker $marker: ${e.message}")
            }
        }
    }
}