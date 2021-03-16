package top.gerritchang.tools.controller

import com.google.gson.Gson
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import top.gerritchang.tools.utils.HttpUtils
import top.gerritchang.check.utils.MacUtils
import java.net.InetAddress

@RestController
class ActivationController {

    @GetMapping("/activationKey")
    fun activationKey(key:String):HashMap<*,*>?{
        val hashMap:HashMap<String,String> = HashMap()
        val macUtil = MacUtils()
        val mac = macUtil.getSystemMac()
        val osname = System.getProperty("os.name")
        val pcname = InetAddress.getLocalHost().hostName
        hashMap.put("mac",mac)
        hashMap.put("key",key)
        hashMap.put("osname",osname)
        hashMap.put("pcname",pcname)
        val result = HttpUtils.sendGETCommon("http://www.gerritchang.top:8080/activation",hashMap)
        val gson = Gson()
        val resultMap:HashMap<*,*> = gson.fromJson(result,HashMap::class.java)
        return resultMap
    }
}
