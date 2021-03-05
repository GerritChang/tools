package top.gerritchang.tools.configuration

import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import top.gerritchang.tools.utils.MacUtils
import top.gerritchang.tools.DemoApplication
import top.gerritchang.tools.utils.HttpUtils

@Component
class ApplicationRunnerImpl:ApplicationRunner {

    @Value("\${server.port}")
    private val port: String? = null

    companion object{
        var logger: Logger = LoggerFactory.getLogger(DemoApplication::class.java)
    }

    override fun run(args: ApplicationArguments?) {
        val map: HashMap<String, String> = HashMap()
        val macUtils = MacUtils()
        map.put("mac", macUtils.getSystemMac())
        val result = HttpUtils.sendGETCommon("http://www.gerritchang.top:8080/check", map)
        if (result.length > 0) {
            val gson = Gson()
            val resultMap: HashMap<*, *> = gson.fromJson(result, HashMap::class.java)
            if ("error".equals(resultMap.get("status"))) {
                logger.info("程序未激活，请激活后使用，地址：http://localhost:$port/activation")
            } else {
                logger.info("程序已启动，地址：http://localhost:$port/setup")
            }
        }else{
            logger.info("无法验证程序是否激活，请检查网络连接")
        }
    }
}
