package top.gerritchang.tools.websocket;

import com.alibaba.druid.support.json.JSONUtils;
import top.gerritchang.tools.utils.StaticVarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/uploadByWs/{username}")
public class UploadWebSocket {

    @Value("${spring.cache.type}")
    private String cacheType;

    @Autowired
    private RedisTemplate redisTemplate;

    private static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap();

    /**
     * websocket连接建立时的方法
     * @param session
     * @param username
     */
    @OnOpen
    public void OnOpen(Session session, @PathParam("username") String username) {
        webSocketMap.put(username, session);
        this.sendMessage("连接已建立");
    }

    /**
     * websocket连接关闭时调用的方法
     * @param session
     * @param username
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        webSocketMap.remove(username);
    }

    /**
     * websocket连接出错时调用的方法
     * @param throwable
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * websocket连接有消息传送时调用的方法
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        this.sendMessage(message);
    }

    /**
     * 有参数的websocket推送消息
     * @param message
     */
    public void sendMessage(String message) {
        try {
            if (!webSocketMap.isEmpty()) {
                ConcurrentHashMap.KeySetView<String, Session> keySetView = webSocketMap.keySet();
                Iterator<String> iterator = keySetView.iterator();
                while (iterator.hasNext()) {
                    webSocketMap.get(iterator.next()).getBasicRemote().sendText(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 无参数的websocket推送消息
     */
    public void sendMessage() {
        try {
            if (!webSocketMap.isEmpty()) {
                ConcurrentHashMap.KeySetView<String, Session> keySetView = webSocketMap.keySet();
                Iterator<String> iterator = keySetView.iterator();
                while (iterator.hasNext()) {
                    String username = iterator.next();
                    boolean flag;
                    switch (cacheType){
                        case "redis":
                            flag = this.checkCountsFromRedis(username);
                            break;
                        case "none":
                            flag = this.checkCountsFromStaticVar(username);
                            break;
                        default:
                            flag = this.checkCountsFromStaticVar(username);
                            break;
                    }
                    if (flag){
                        Map resultMap = new HashMap();
                        resultMap.put("status",true);
                        resultMap.put("message","文件上传成功");
                        webSocketMap.get(username).getBasicRemote().sendText(JSONUtils.toJSONString(resultMap));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证文件时否全部上传完成
     * @param username
     * @return
     */
    private boolean checkCountsFromRedis(String username) {
        if (redisTemplate.hasKey(username)) {
            Set<String> set = redisTemplate.opsForHash().keys(username);
            int number = Integer.parseInt(redisTemplate.opsForHash().get(username, "count").toString());
            if ((number + 1) == set.size()) {
                redisTemplate.delete(username);
                return true;
            }
        }
        return false;
    }

    /**
     * 验证文件是否全部上传完成
     * @param username
     * @return
     */
    private boolean checkCountsFromStaticVar(String username){
        if (StaticVarUtils.concurrentHashMap.containsKey(username)){
            List<Map<String,Object>> list = StaticVarUtils.concurrentHashMap.get(username);
            if (Math.addExact(Integer.parseInt(list.get(0).get("count").toString()),1) == list.size()){
                StaticVarUtils.removeValueFromMap(username);
                return true;
            }
        }
        return false;
    }
}
