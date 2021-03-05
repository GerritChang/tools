package top.gerritchang.tools

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import top.gerritchang.tools.websocket.CommonSocket

@SpringBootApplication
open class DemoApplication

fun main(args: Array<String>) {
    val springApplication = SpringApplication(DemoApplication::class.java)
    val configurableApplicationContext: ConfigurableApplicationContext = springApplication.run(*args)
    val socket = CommonSocket()
    socket.setApplicationContext(configurableApplicationContext)
}
