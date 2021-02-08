package com.wdbc.ciu.mapper;

import com.wdbc.ciu.model.Res;
import com.wdbc.ciu.model.Table;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CheckImageMapper {
    List<Table> getListTable(@Param(value = "dataBase")String dataBase);

    List<Res> getUrlByTable(@Param(value = "table") Table table, @Param(value = "tempId") String tempId);

    List<String> getListDataBase();

    String getPrimaryByTableName(@Param(value = "tableName")String tableName);
}