package top.gerritchang.tools.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class QueryListDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map getSqlByScriptName(String scriptName) {
        return jdbcTemplate.queryForMap("select script_sql,script_method from form_script where script_name = '"
                + scriptName + "'");
    }

    public List<Map> query(String script_sql) {
        return jdbcTemplate.queryForList(script_sql, Map.class);
    }
}
