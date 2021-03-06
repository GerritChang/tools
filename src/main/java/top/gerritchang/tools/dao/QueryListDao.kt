package top.gerritchang.tools.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class QueryListDao {

    @Autowired
    private lateinit var jdbcTemplate:JdbcTemplate

    fun getSqlByScriptName(scriptName: String): Map<String, *> {
        return jdbcTemplate.queryForMap("select script_sql,script_method from form_script where script_name = '"
                + scriptName + "'")
    }

    fun query(script_sql: String): List<Map<*, *>?> {
        return jdbcTemplate.queryForList(script_sql, MutableMap::class.java)
    }

}
