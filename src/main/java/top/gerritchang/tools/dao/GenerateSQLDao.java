package top.gerritchang.tools.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenerateSQLDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List queryTableColumns(){
//        jdbcTemplate.queryForList();
        return null;
    }
}
