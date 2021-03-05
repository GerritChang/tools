package top.gerritchang.tools.task

import com.google.gson.Gson
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import top.gerritchang.tools.utils.MacUtils
import top.gerritchang.tools.utils.HttpUtils

@Component
open class CronTask {

    @Scheduled(cron = "0 0 1/1 * * ?")
    @Async
    open fun ScheduledTask() {
        val map: HashMap<String, String> = HashMap()
        val macUtils = MacUtils()
        map.put("mac", macUtils.getSystemMac())
        val result = HttpUtils.sendGETCommon("http://www.gerritchang.top:8080/check", map)
        if (result.length > 0) {
            val gson = Gson()
            val resultMap: HashMap<*, *> = gson.fromJson(result, HashMap::class.java)
            if ("error".equals(resultMap.get("status"))) {
                System.exit(1)
            }
        }else{
            System.exit(1)
        }
    }
}
