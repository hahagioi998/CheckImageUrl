package com.wdbc.ciu.service;

import com.wdbc.ciu.model.Res;
import com.wdbc.ciu.model.Table;

import java.util.List;

public interface CheckImageService {

     List<Table> getListTable(String dataBase);

     List<String> getListDataBase();

     String getPrimaryByTableName(String tempTable);

     List<Res> getUrlByTable(Table table, String tempId);
}
