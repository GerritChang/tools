package top.gerritchang.tools.task;

import top.gerritchang.tools.websocket.UploadWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronTask {

    @Autowired
    private UploadWebSocket webSocket;

    @Scheduled(cron = "0 1/1 * * * ?")
    @Async
    public void ScheduledTask() {
        webSocket.sendMessage();
    }

}
