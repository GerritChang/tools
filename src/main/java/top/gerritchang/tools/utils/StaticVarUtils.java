package top.gerritchang.tools.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StaticVarUtils {

    //创建一个线程安全的map用于存储数据
    public static ConcurrentHashMap<String, LinkedList<Map<String, Object>>> concurrentHashMap = new ConcurrentHashMap<>();

    public static void putNewValueToMap(String username, Map newValueMap,int count) {
        if (concurrentHashMap.containsKey(username)) {
            LinkedList<Map<String, Object>> list = concurrentHashMap.get(username);
            list.add(newValueMap);
        } else {
            LinkedList<Map<String, Object>> list = new LinkedList<>();
            Map map = new HashMap();
            map.put("count", count);
            list.add(map);
            list.add(newValueMap);
            concurrentHashMap.put(username, list);
        }
    }

    /**
     * 清除username下的临时数据
     *
     * @param username
     * @return
     */
    public static boolean removeValueFromMap(String username) {
        //判断map中是否包含某用户名的key
        if (concurrentHashMap.containsKey(username)) {
            //移除该键值对
            concurrentHashMap.remove(username);
            //移除之后返回成功
            return true;
        }
        //不包含该key的话直接返回false
        return false;
    }

}
