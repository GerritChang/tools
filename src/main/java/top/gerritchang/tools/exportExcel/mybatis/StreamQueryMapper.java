package top.gerritchang.tools.exportExcel.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;
import java.util.Map;

@Mapper
public interface StreamQueryMapper {

    List<Map> streamQuery(ResultHandler<Map> handler);
}
