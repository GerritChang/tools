package top.gerritchang.tools.websocket;

import com.google.gson.Gson;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.gerritchang.tools.service.QueryListService;
import top.gerritchang.tools.utils.HttpUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/foreach/{username}")
public class ForeachHttpSocket {

    private static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap();
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    private static ApplicationContext applicationContext;
    public static void setApplicationContext(ApplicationContext applicationContext) {
        ForeachHttpSocket.applicationContext = applicationContext;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){
        webSocketMap.put(username,session);
        this.sendMessage(this.SUCCESS,"连接已成功建立");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username){
        webSocketMap.remove(username);
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message){
        Gson gson = new Gson();
        Map paramsMap = gson.fromJson(message, Map.class);
        String result = HttpUtils.sendGETCommon("",null);
        this.sendMessage(this.SUCCESS,"第"+"次请求结果为："+result);
    }

    public void sendMessage(String status,String message) {
        try {
            if (!webSocketMap.isEmpty()) {
                ConcurrentHashMap.KeySetView<String, Session> keySetView = webSocketMap.keySet();
                Iterator<String> iterator = keySetView.iterator();
                while (iterator.hasNext()) {
                    Map sendMap = new ConcurrentHashMap();
                    sendMap.put("status",status);
                    sendMap.put("message",message);
                    Gson gson = new Gson();
                    webSocketMap.get(iterator.next()).getBasicRemote().sendText(gson.toJson(sendMap));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
