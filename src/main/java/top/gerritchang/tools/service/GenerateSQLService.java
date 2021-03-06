package top.gerritchang.tools.service;

import top.gerritchang.tools.mybatis.GenerateSQLMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class GenerateSQLService {

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    public List<Map> showAllMySQLTable(){
        GenerateSQLMapper generateSQLMapper = sqlSessionTemplate.getMapper(GenerateSQLMapper.class);
        return generateSQLMapper.showAllMySQLTable();
    }

    public List<Map> showAllOracleSchemas(){
        GenerateSQLMapper generateSQLMapper = sqlSessionTemplate.getMapper(GenerateSQLMapper.class);
        return generateSQLMapper.showAllOracleSchemas();
    }

    public List<Map> showAllOracleTable(String schema){
        GenerateSQLMapper generateSQLMapper = sqlSessionTemplate.getMapper(GenerateSQLMapper.class);
        return generateSQLMapper.showAllOracleTable(schema);
    }

    public List<Map> getMySQLTableColumns(Map map){
        GenerateSQLMapper generateSQLMapper = sqlSessionTemplate.getMapper(GenerateSQLMapper.class);
        return generateSQLMapper.getMySQLTableColumns(map);
    }

    public List<Map> getOracleTableColumns(Map map){
        GenerateSQLMapper generateSQLMapper = sqlSessionTemplate.getMapper(GenerateSQLMapper.class);
        return generateSQLMapper.getOracleTableColumns(map);
    }

}
