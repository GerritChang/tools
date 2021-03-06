package top.gerritchang.tools.utils

import java.util.concurrent.ConcurrentHashMap

open class KeyUtils {

    companion object{
        var key: ConcurrentHashMap<String, String> = ConcurrentHashMap()

        fun insertKey(key:String):Boolean{
            if (!KeyUtils.key.containsKey("key")){
                KeyUtils.key.put("key",key)
                return true
            }
            return false
        }

        fun removeKey(key: String):Boolean{
            if (KeyUtils.key.containsKey("key")){
                KeyUtils.key.remove("key")
                return true
            }
            return false
        }
    }
}
