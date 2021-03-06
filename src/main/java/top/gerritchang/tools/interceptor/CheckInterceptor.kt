package top.gerritchang.tools.interceptor

import com.google.gson.Gson
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import top.gerritchang.tools.utils.KeyUtils
import top.gerritchang.tools.utils.MacUtils
import top.gerritchang.tools.utils.HttpUtils
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class CheckInterceptor: HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (KeyUtils.key.size <= 0) {
            val map: HashMap<String, String> = HashMap()
            val macUtils = MacUtils()
            map.put("mac", macUtils.getSystemMac())
            val result = HttpUtils.sendGETCommon("http://www.gerritchang.top:8080/check", map)
            val gson = Gson()
            val resultMap: HashMap<*, *> = gson.fromJson(result, HashMap::class.java)
            if ("error".equals(resultMap.get("status"))) {
                response.sendRedirect("activation")
                return true
            }
            KeyUtils.insertKey(resultMap.get("key").toString())
        }
        return true
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
    }
}
