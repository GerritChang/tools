package top.gerritchang.tools.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelectDbDataService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> queryDataByParams(String schema,String table,String column){
        List<String> list = jdbcTemplate.queryForList("select "+column+" from "+schema+"."+table,String.class);
        return list;
    }
}
