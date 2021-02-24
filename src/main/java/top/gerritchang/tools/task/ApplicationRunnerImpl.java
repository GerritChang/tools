package top.gerritchang.tools.task;

import top.gerritchang.tools.DemoApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Value("${server.port}")
    private String port;

    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("程序已启动，地址：http://localhost:" + port + "/index");
    }
}
