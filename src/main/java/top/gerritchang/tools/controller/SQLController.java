package top.gerritchang.tools.controller;

import com.alibaba.fastjson.JSON;
import top.gerritchang.tools.service.GenerateSQLService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class SQLController {

    @Value("${spring.datasource.url}")
    private String URL;

    @Resource
    private GenerateSQLService generateSQLService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/getAllSchemas")
    public Map getAllSchemas() {
        List<Map> list = new ArrayList<>();
        Map result = new HashMap();
        if (URL.toLowerCase().indexOf("mysql") != -1) {
            Map resultMap = new HashMap();
            String schema = this.getDataBaseName();
            resultMap.put("ID", schema);
            resultMap.put("TEXT", schema);
            list.add(resultMap);
        } else {
            list = generateSQLService.showAllOracleSchemas();
        }
        result.put("schema", list);
        return result;
    }

    @GetMapping("/showAllTable")
    public Map showAllTable(String schema) {
        if (URL.toLowerCase().indexOf("mysql") != -1) {
            return this.showAllMySQLTable(schema);
        } else {
            return this.showAllOracleTable(schema);
        }
    }

    @PostMapping("/sqlTest")
    public Map sqlTest(String sql) {
        Map result = new HashMap();
        String[] sqls = sql.split("\n");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < sqls.length; i++) {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sqls[i]);
            stringBuffer
                    .append("第")
                    .append(i + 1)
                    .append("个SQL的查询结果为：\n")
                    .append(JSON.toJSON(list))
                    .append(" ")
                    .append("\n");
        }
        result.put("state", "success");
        result.put("result", stringBuffer.toString());
        return result;
    }

    @GetMapping("/getTableColumns")
    public List<Map> getTableColumnsList(String table_name, String table_schema) {
        Map params = new HashMap();
        params.put("table_name", table_name);
        params.put("table_schema", table_schema);
        List<Map> list = null;
        if (URL.toLowerCase().indexOf("mysql") != -1) {
            list = generateSQLService.getMySQLTableColumns(params);
        } else {
            list = generateSQLService.getOracleTableColumns(params);
        }
        List<Map> result = list.stream().filter(item -> {
            item.put("ID", item.get("COLUMN_NAME"));
            item.put("TEXT", item.get("COLUMN_NAME"));
            return true;
        }).collect(Collectors.toList());
        return result;
    }

    @GetMapping("/generateInsertSQL")
    public Map generateInsertSQL(String table_name, String table_schema, String primaryKey, String uniKey) {
        if (URL.toLowerCase().indexOf("mysql") != -1) {
            return this.generateMySQLInsertSQL(table_name, table_schema, primaryKey, uniKey);
        } else {
            return this.generateOracleInsertSQL(table_name, table_schema, primaryKey, uniKey);
        }
    }

    private Map generateMySQLInsertSQL(String table_name, String table_schema, String primaryKey, String uniKey) {
        Map params = new HashMap();
        params.put("table_name", table_name);
        params.put("table_schema", table_schema);
        List<Map> list = generateSQLService.getMySQLTableColumns(params);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer keyBuffer = new StringBuffer();
        StringBuffer columnBuffer = new StringBuffer();
        StringBuffer valueBuffer = new StringBuffer();
        stringBuffer.append("insert into ").append(table_name).append("(").append("\n");
        if ("".equals(uniKey)) {
            for (Map map : list) {
                if (!primaryKey.equals(map.get("COLUMN_NAME").toString())) {
                    String column_name = map.get("COLUMN_NAME").toString();
                    columnBuffer.append("<if test=\"").append(column_name).append(" != null").append("\">\n")
                            .append(",").append(column_name).append("\n")
                            .append("</if>").append("\n");
                    valueBuffer.append("<if test=\"").append(column_name).append(" != null").append("\">\n")
                            .append(",").append("#{").append(column_name).append("}").append("\n")
                            .append("</if>").append("\n");
                }
            }
            stringBuffer.append(primaryKey).append("\n")
                    .append(columnBuffer).append(") values (").append("\n")
                    .append(" #{").append(primaryKey).append("}").append("\n").append(valueBuffer).append(")");
        } else if (!"".equals(uniKey)) {
            List<String> uniKeyList = JSON.parseObject(uniKey, List.class);
            for (Map map : list) {
                boolean flag = false;
                for (String string : uniKeyList) {
                    if (string.equals(map.get("COLUMN_NAME").toString())) {
                        flag = true;
                    }
                }
                if (!flag) {
                    String column_name = map.get("COLUMN_NAME").toString();
                    columnBuffer.append("<if test=\"").append(column_name).append(" != null").append("\">\n")
                            .append(",").append(column_name).append("\n")
                            .append("</if>").append("\n");
                    valueBuffer.append("<if test=\"").append(column_name).append(" != null").append("\">\n")
                            .append(",").append("#{").append(column_name).append("}").append("\n")
                            .append("</if>").append("\n");
                }
            }
            for (int i = 0; i < uniKeyList.size(); i++) {
                if (i != 0) {
                    stringBuffer.append(",");
                    keyBuffer.append(",");
                }
                stringBuffer.append(uniKeyList.get(i)).append("\n");
                keyBuffer.append(" #{").append(uniKeyList.get(i)).append("}").append("\n");
            }
            stringBuffer.append(columnBuffer).append(") values (").append("\n")
                    .append(keyBuffer).append(valueBuffer).append(")");
        }
        Map map = new HashMap();
        map.put("SQL", stringBuffer.toString());
        map.put("message", "插入SQL生成成功");
        return map;
    }

    private Map generateOracleInsertSQL(String table_name, String table_schema, String primaryKey, String uniKey) {
        Map params = new HashMap();
        params.put("table_name", table_name);
        params.put("table_schema", table_schema);
        List<Map> list = generateSQLService.getOracleTableColumns(params);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer keyBuffer = new StringBuffer();
        StringBuffer columnBuffer = new StringBuffer();
        StringBuffer valueBuffer = new StringBuffer();
        stringBuffer.append("insert into ").append(table_name).append("(").append("\n");
        if ("".equals(uniKey)) {
            for (Map map : list) {
                if (!primaryKey.equals(map.get("COLUMN_NAME").toString())) {
                    String column_name = map.get("COLUMN_NAME").toString();
                    columnBuffer.append("<if test=\"").append(column_name).append(" != null").append("\">\n")
                            .append(",").append(column_name).append("\n")
                            .append("</if>").append("\n");
                    valueBuffer.append("<if test=\"").append(column_name).append(" != null").append("\">\n")
                            .append(",").append("#{").append(column_name).append("}").append("\n")
                            .append("</if>").append("\n");
                }
            }
            stringBuffer.append(primaryKey).append("\n")
                    .append(columnBuffer).append(") values (").append("\n")
                    .append(" #{").append(primaryKey).append("}").append("\n").append(valueBuffer).append(")");
        } else if (!"".equals(uniKey)) {
            List<String> uniKeyList = JSON.parseObject(uniKey, List.class);
            for (Map map : list) {
                boolean flag = false;
                for (String string : uniKeyList) {
                    if (string.equals(map.get("COLUMN_NAME").toString())) {
                        flag = true;
                    }
                }
                if (!flag) {
                    String column_name = map.get("COLUMN_NAME").toString();
                    columnBuffer.append("<if test=\"").append(column_name).append(" != null").append("\">\n")
                            .append(",").append(column_name).append("\n")
                            .append("</if>").append("\n");
                    valueBuffer.append("<if test=\"").append(column_name).append(" != null").append("\">\n")
                            .append(",").append("#{").append(column_name).append("}").append("\n")
                            .append("</if>").append("\n");
                }
            }
            for (int i = 0; i < uniKeyList.size(); i++) {
                if (i != 0) {
                    stringBuffer.append(",");
                    keyBuffer.append(",");
                }
                stringBuffer.append(uniKeyList.get(i)).append("\n");
                keyBuffer.append(" #{").append(uniKeyList.get(i)).append("}").append("\n");
            }
            stringBuffer.append(columnBuffer).append(") values (").append("\n")
                    .append(keyBuffer).append(valueBuffer).append(")");
        }
        Map map = new HashMap();
        map.put("SQL", stringBuffer.toString());
        map.put("message", "插入SQL生成成功");
        return map;
    }

    @GetMapping("/generateUpdateSQL")
    public Map generateUpdateSQL(String table_name, String table_schema, String primaryKey, String uniKey) {
        String result = null;
        if ("".equals(uniKey) || null == uniKey) {
            result = this.getPrimaryKeySQL(table_name, table_schema, primaryKey);
        } else {
            result = this.getUniKeySQL(table_name, table_schema, uniKey);
        }
        Map map = new HashMap();
        map.put("SQL", result);
        map.put("message", "插入SQL生成成功");
        return map;
    }

    @PostMapping("/oneKey")
    public Map oneKey(String rows, String connectType) {
        List<Map> list = JSON.parseObject(rows, List.class);
        String result;
        switch (connectType) {
            case "0":
                result = this.getSingleTableSQL(list);
                break;
            case "4":
                result = this.getCartesianProductSQL(list);
                break;
            default:
                result = null;
                break;
        };
        Map resultMap = new HashMap();
        resultMap.put("SQL", result);
        resultMap.put("message", "SQL生成成功");
        return resultMap;
    }

    /**
     * 查询MySQL的所有表名
     *
     * @param database
     * @return
     */
    private Map showAllMySQLTable(String database) {
        List<Map> list = generateSQLService.showAllMySQLTable();
        List<Map> data = new ArrayList<>();
        Iterator<Map> iterator = list.iterator();
        while (iterator.hasNext()) {
            Map m = iterator.next();
            Map map = new HashMap();
            map.put("Tables", m.get("Tables_in_" + database));
            data.add(map);
        }
        Map resultMap = new HashMap();
        resultMap.put("rows", data);
        resultMap.put("total", data.size());
        return resultMap;
    }

    /**
     * 查询Oracle下的所有表名
     *
     * @param schema
     * @return
     */
    private Map showAllOracleTable(String schema) {
        List<Map> list = generateSQLService.showAllOracleTable(schema);
        Map resultMap = new HashMap();
        Iterator<Map> iterator = list.iterator();
        List<Map> data = new ArrayList<>();
        while (iterator.hasNext()) {
            Map m = iterator.next();
            Map map = new HashMap();
            map.put("Tables", m.get("TABLES"));
            data.add(map);
        }
        resultMap.put("rows", data);
        resultMap.put("total", data.size());
        return resultMap;
    }

    /**
     * 生成单表查询SQL
     *
     * @param SQL
     * @return
     */
    private String getSingleTableSQL(List<Map> SQL) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map map : SQL) {
            stringBuffer
                    .append("select ")
                    .append(this.getTableColumns(map.get("Tables").toString()))
                    .append(" from ")
                    .append(map.get("Tables"))
                    .append(";")
                    .append("\n");
        }
        return stringBuffer.toString();
    }

    /**
     * 生成笛卡尔积的SQL
     *
     * @param SQL
     * @return
     */
    private String getCartesianProductSQL(List<Map> SQL) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select * from ");
        for (Map map : SQL) {
            stringBuffer.append(map.get("Tables")).append(",");
        }
        return stringBuffer.substring(0, stringBuffer.length() - 1);
    }

    /**
     * 获取表的所有列
     *
     * @param table_name
     * @return
     */
    private String getTableColumns(String table_name) {
        Map params = new HashMap();
        params.put("table_name", table_name);
        params.put("table_schema", this.getDataBaseName());
        List<Map> list = generateSQLService.getMySQLTableColumns(params);
        StringBuffer stringBuffer = new StringBuffer();
        for (Map map : list) {
            stringBuffer.append(map.get("COLUMN_NAME")).append(",");
        }
        return stringBuffer.substring(0, stringBuffer.length() - 1);
    }

    /**
     * 生成普通主键SQL
     *
     * @param table_name
     * @param primaryKey
     * @return
     */
    private String getPrimaryKeySQL(String table_name, String table_schema, String primaryKey) {
        StringBuffer stringBuffer = new StringBuffer();
        List<Map> list = this.getTableColumnsList(table_name, table_schema);
        stringBuffer
                .append("update ")
                .append(table_name)
                .append("\n")
                .append("set ")
                .append(primaryKey)
                .append("=#{")
                .append(primaryKey)
                .append("}\n");
        for (Map map : list) {
            String column_name = map.get("COLUMN_NAME").toString();
            if (!primaryKey.equals(column_name)) {
                stringBuffer.append("<if test=\"").append(column_name).append(" != null \">\n")
                        .append(",").append(column_name).append("=#{").append(column_name).append("}\n")
                        .append("</if>\n");
            }
        }
        stringBuffer.append("where ").append(primaryKey).append("=#{").append(primaryKey).append("}");
        return stringBuffer.toString();
    }

    /**
     * 联合主键的插入SQL
     *
     * @param table_name
     * @param uniKey
     * @return
     */
    private String getUniKeySQL(String table_name, String table_schema, String uniKey) {
        List<String> list = JSON.parseObject(uniKey, List.class);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer whereBuffer = new StringBuffer();
        List<Map> columnsList = this.getTableColumnsList(table_name, table_schema);
        stringBuffer
                .append("update ")
                .append(table_name)
                .append("\n")
                .append("set ");
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                stringBuffer.append(",");
                whereBuffer.append("and ");
            }
            stringBuffer
                    .append(list.get(i))
                    .append("=#{")
                    .append(list.get(i))
                    .append("}\n");
            whereBuffer
                    .append(list.get(i))
                    .append("=#{")
                    .append(list.get(i))
                    .append("}\n");
        }
        for (Map map : columnsList) {
            boolean flag = false;
            String column_name = map.get("COLUMN_NAME").toString();
            for (String str : list) {
                if (str.equals(column_name)) {
                    flag = true;
                    continue;
                }
            }
            if (!flag) {
                stringBuffer.append("<if test=\"").append(column_name).append(" != null\">\n")
                        .append(",").append(column_name).append("=#{").append(column_name).append("}\n")
                        .append("</if>\n");
            }
        }
        stringBuffer.append("where ").append(whereBuffer);
        return stringBuffer.toString();
    }

    /**
     * 获取数据库名
     *
     * @return
     */
    private String getDataBaseName() {
        URL = URL.replaceAll("//", "");
        String[] tempArr = URL.split("/");
        // ? 匹配问号需要使用[?]
        String[] temp2Arr = tempArr[1].split("[?]");
        String dataBaseName = temp2Arr[0];
        return dataBaseName;
    }

}
