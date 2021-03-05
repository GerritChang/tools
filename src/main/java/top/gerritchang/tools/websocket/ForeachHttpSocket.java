package top.gerritchang.tools.websocket;

import com.google.gson.Gson;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.gerritchang.tools.service.SelectDbDataService;
import top.gerritchang.tools.utils.HttpUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/foreach/{username}")
public class ForeachHttpSocket {

    private static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap();
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    private static ApplicationContext applicationContext;
    private SelectDbDataService selectDbDataService;
    public static void setApplicationContext(ApplicationContext applicationContext) {
        ForeachHttpSocket.applicationContext = applicationContext;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){
        webSocketMap.put(username,session);
        this.sendMessage(this.SUCCESS,"连接已成功建立");
    }

    @OnClose
    public void onClose(@PathParam("username") String username){
        webSocketMap.remove(username);
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message){
        System.out.println(message);
        Gson gson = new Gson();
        List<Map> list = gson.fromJson(message, List.class);
        Iterator<Map> iterator = list.iterator();
        String url="";
        List<String> dbList = new ArrayList<>();
        String foreachkey = "";
        int type = 0;
        int startnumber = 0;
        int endnumber = 1;
        char startalphabet = 'a';
        char endalphabet = 'z';
        Map otherParams = new HashMap();
        while (iterator.hasNext()){
            Map map = iterator.next();
            if (map.size() == 1){
                url = map.get("key").toString();
            }else{
                if (map.containsKey("staticnumber")){
                    otherParams.put(map.get("key").toString(),map.get("staticnumber").toString());
                    continue;
                }
                if (map.containsKey("schema")){
                    foreachkey = map.get("key").toString();
                    selectDbDataService = applicationContext.getBean(SelectDbDataService.class);
                    dbList = selectDbDataService.queryDataByParams(map.get("schema").toString(),
                            map.get("table").toString(),map.get("database").toString());
                    type = 1;
                }else if (map.containsKey("startnumber")){
                    foreachkey = map.get("key").toString();
                    startnumber = Integer.parseInt(map.get("startnumber").toString());
                    endnumber = Integer.parseInt(map.get("endnumber").toString());
                    type = 2;
                }else if (map.containsKey("startalphabet")){
                    foreachkey = map.get("key").toString();
                    startalphabet = ((char) map.get("startalphabet"));
                    endalphabet = ((char) map.get("endalphabet"));
                    type = 3;
                }
            }
        }
        this.executeForeach(type,url,dbList,foreachkey,startnumber,endnumber,startalphabet,endalphabet,otherParams);
    }

    private void executeForeach(int type,String url,List<String> list,String foreachkey,int startnumber,
            int endnumber, char startalphabet, char endalphabet,Map otherParams){
        switch (type){
            case 1:
                for (int i = 0; i < list.size(); i++) {
                    otherParams.put(foreachkey,list.get(i));
                    String result = HttpUtils.sendGETCommon(url,otherParams);
                    this.sendMessage(this.SUCCESS,"第"+i+"次请求结果为："+result);
                }
                break;
            case 2:
                for (int i = startnumber; i < endnumber; i++) {
                    otherParams.put(foreachkey,String.valueOf(i));
                    String result = HttpUtils.sendGETCommon(url,otherParams);
                    this.sendMessage(this.SUCCESS,"第"+i+"次请求结果为："+result);
                }
                break;
            case 3:
                for (char i = startalphabet; i < endalphabet; i++) {
                    otherParams.put(foreachkey,String.valueOf(i));
                    String result = HttpUtils.sendGETCommon(url,otherParams);
                    this.sendMessage(this.SUCCESS,"第"+i+"次请求结果为："+result);
                }
                break;
        }

    }

    private void sendMessage(String status,String message) {
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
