package com.wdbc.ciu.model;

import com.alibaba.excel.annotation.ExcelProperty;

public class Res {
    @ExcelProperty(value = "数据库名",index = 0)
    private String dataBase;
    @ExcelProperty(value = "表名",index = 1)
    private String tableName;
    @ExcelProperty(value = "所在表主键",index = 2)
    private String id;
    @ExcelProperty(value = "字段名",index = 3)
    private String columnName;
    @ExcelProperty(value = "图片地址",index = 4)
    private String url;
    @ExcelProperty(value = "是否可访问",index = 5)
    private String isOk;

    public String getIsOk() {
        return isOk;
    }

    public void setIsOk(String isOk) {
        this.isOk = isOk;
    }

    public String getDataBase() {
        return dataBase;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
