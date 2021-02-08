package com.wdbc.ciu.service.impl;


import com.wdbc.ciu.mapper.CheckImageMapper;
import com.wdbc.ciu.model.Res;
import com.wdbc.ciu.model.Table;
import com.wdbc.ciu.service.CheckImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckImageServiceImpl implements CheckImageService {

    @Autowired
    private CheckImageMapper checkImageMapper;

    @Override
    public List<Table> getListTable(String dataBase) {
        return checkImageMapper.getListTable(dataBase);
    }


    @Override
    public List<String> getListDataBase() {
        return checkImageMapper.getListDataBase();
    }

    @Override
    public String getPrimaryByTableName(String tempTable) {
        return checkImageMapper.getPrimaryByTableName(tempTable);
    }

    @Override
    public List<Res> getUrlByTable(Table table, String tempId) {
        return checkImageMapper.getUrlByTable(table,tempId);
    }


}
