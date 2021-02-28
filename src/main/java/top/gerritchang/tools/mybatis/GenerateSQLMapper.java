package top.gerritchang.tools.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface GenerateSQLMapper {

    @Select("select distinct owner ID,owner TEXT from ALL_COL_COMMENTS")
    List<Map> showAllOracleSchemas();

    @Select("show tables")
    List<Map> showAllMySQLTable();

    @Select("select table_name Tables from all_tables where owner=#{schema}")
    List<Map> showAllOracleTable(String schema);

    @Select("select column_name,column_comment,data_type,column_key" +
            "        from information_schema.columns" +
            "        where table_name=#{table_name} and table_schema=#{table_schema}")
    List<Map> getMySQLTableColumns(Map map);

    @Select("select A.COLUMN_NAME,A.DATA_TYPE from all_tab_cols A where A.OWNER = #{table_schema} and A.table_name=#{table_name}")
    List<Map> getOracleTableColumns(Map map);
}
