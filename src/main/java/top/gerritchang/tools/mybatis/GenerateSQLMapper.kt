package top.gerritchang.tools.mybatis

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface GenerateSQLMapper {

    @Select("select distinct owner ID,owner TEXT from ALL_COL_COMMENTS")
    fun showAllOracleSchemas(): List<Map<String, String>?>?

    @Select("show tables")
    fun showAllMySQLTable(): List<Map<String, String>?>?

    @Select("select table_name Tables from all_tables where owner=#{schema}")
    fun showAllOracleTable(schema: String?): List<Map<String, String>?>?

    @Select("select column_name,column_comment,data_type,column_key" +
            "        from information_schema.columns" +
            "        where table_name=#{table_name} and table_schema=#{table_schema}")
    fun getMySQLTableColumns(map: Map<String, String>?): List<Map<String, String>?>?

    @Select("select A.COLUMN_NAME,A.DATA_TYPE from all_tab_cols A where A.OWNER = #{table_schema} and A.table_name=#{table_name}")
    fun getOracleTableColumns(map: Map<String, String>?): List<Map<String, String>?>?
}
