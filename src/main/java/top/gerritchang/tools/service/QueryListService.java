package top.gerritchang.tools.service;

import top.gerritchang.tools.dao.QueryListDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QueryListService {

    @Autowired
    private QueryListDao queryListDao;

    public String getSqlByScriptName(String scriptName) {
        Map map = queryListDao.getSqlByScriptName(scriptName);
//        try {
//            Method method = queryListDao.getClass().getMethod(map.get("script_method").toString(), String.class);
//            method.invoke(queryListDao, map);
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
        return map.get("script_sql").toString();
    }
}
