package top.gerritchang.tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import top.gerritchang.check.websocket.CommonSocket;

@SpringBootApplication
@ComponentScan({"top.gerritchang.check","top.gerritchang.tools"})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DemoApplication.class);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
        CommonSocket socket = new CommonSocket();
        socket.setApplicationContext(configurableApplicationContext);
    }
}
