package top.gerritchang.tools.websocket

import com.google.gson.Gson
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Component
import top.gerritchang.tools.service.QueryListService
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import javax.websocket.*
import javax.websocket.server.PathParam
import javax.websocket.server.ServerEndpoint

@Component
@ServerEndpoint("/commonSocket/{username}")
class CommonSocket {
    private val webSocketMap: ConcurrentHashMap<String, Session> = ConcurrentHashMap()
    private val SUCCESS = "success"
    private val ERROR = "error"

    companion object {
        private lateinit var applicationContext: ApplicationContext
    }
    private var queryListService = QueryListService()
    fun setApplicationContext(applicationContext: ApplicationContext) {
        CommonSocket.applicationContext = applicationContext
    }
    @OnOpen
    fun onOpen(session: Session, @PathParam("username") username: String) {
        webSocketMap[username] = session
        sendMessage(SUCCESS, "连接已成功建立")
    }

    @OnClose
    fun onClose(session: Session, @PathParam("username") username: String) {
        webSocketMap.remove(username)
    }

    @OnError
    fun onError(throwable: Throwable) {
        throwable.printStackTrace()
    }

    @OnMessage
    fun onMessage(message: String?) {
        val gson = Gson()
        val paramsMap: Map<*, *> = gson.fromJson(message, MutableMap::class.java)
        if (paramsMap.containsKey("scriptName")) {
            val scriptName = paramsMap["scriptName"].toString()
            queryListService = applicationContext.getBean(QueryListService::class.java)
            val sql: String = queryListService.getSqlByScriptName(scriptName)
            sendMessage(SUCCESS, sql)
        } else {
            sendMessage(ERROR, "错误！脚本ID为必填项")
        }
    }

    fun sendMessage(status: String, message: String) {
        try {
            if (!webSocketMap.isEmpty()) {
                val keySetView = webSocketMap.keys
                val iterator: Iterator<String?> = keySetView.iterator()
                while (iterator.hasNext()) {
                    val sendMap: MutableMap<String, String> = ConcurrentHashMap()
                    sendMap["status"] = status
                    sendMap["message"] = message
                    val gson = Gson()
                    webSocketMap[iterator.next()]!!.basicRemote.sendText(gson.toJson(sendMap))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
