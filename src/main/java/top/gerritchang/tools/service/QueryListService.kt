package top.gerritchang.tools.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.gerritchang.tools.dao.QueryListDao

@Service
class QueryListService {

    @Autowired
    private var queryListDao = QueryListDao()

    fun getSqlByScriptName(scriptName:String):String {
        val map = queryListDao.getSqlByScriptName(scriptName);
//        try {
//            Method method = queryListDao.getClass().getMethod(map.get("script_method").toString(), String.class);
//            method.invoke(queryListDao, map);
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
        return map.get("script_sql").toString();
    }
}
