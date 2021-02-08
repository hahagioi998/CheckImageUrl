package com.wdbc.ciu.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.wdbc.ciu.mapper.CheckImageMapper;
import com.wdbc.ciu.model.Res;
import com.wdbc.ciu.model.Table;
import com.wdbc.ciu.service.CheckImageService;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.wdbc.ciu.util.CheckImageUrl.exist;

@RestController
public class CheckImageController {
    @Autowired
    private CheckImageService checkImageService;
    /**搜素一个连接下的所有数据库（容易导致数据库宕）*/
    @GetMapping("/allDataBase")
    public void allDataBase(HttpServletResponse response) throws IOException {
        //获取一个连接下的所有数据库
        List<String> dataBases=checkImageService.getListDataBase();
        List<Res> result=new ArrayList<>();
        //循环所有数据库
        for(String dataBase:dataBases){
            //排除一些数据库
            if(dataBase.equals("mysql")||dataBase.equals("information_schema")||dataBase.equals("performance_schema")||dataBase.equals("sys")){
                continue;
            }
            //获取数据库下，所有的表与字段(会查出视图)
            List<Table> list=checkImageService.getListTable(dataBase);
            //动态切换数据库
            SqlSessionFactory sqlSessionFactory = null;
            try {
                sqlSessionFactory = getSqlSessionFactory(dataBase);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SqlSession sqlsession = sqlSessionFactory.openSession();
            CheckImageMapper mapper = sqlsession.getMapper(CheckImageMapper.class);
            //当前的表与表主键字段名
            String tempTable=list.get(0).getTableName();
            String primaryKey=mapper.getPrimaryByTableName(tempTable);
            //循环每一条数据
            for(Table table:list){
                //如果表切换，则主键重新获取
                if(!tempTable.equals(table.getTableName())){
                    tempTable=table.getTableName();
                    //排除主键不是id，根据表名查主键
                    primaryKey=mapper.getPrimaryByTableName(table.getTableName());
                }
                //排除视图
                if(table.getTableName().subSequence(0,2).equals("v_")){
                    continue;
                }
                //排除主键不存在
                if(primaryKey==null){
                    continue;
                }
                //排除关键词字段导致的错误，还有很多类似的，根据实际情况，灵活排除
                if(table.getColumnName().equals("read")||table.getColumnName().equals("key")||table.getColumnName().equals("function")){
                    continue;
                }
                //动态连接数据源，查询对应表的字段是否含有url(https://，http://-----.jpg,.png,.gif)
                System.out.println("处理到"+dataBase+"--"+table.getTableName()+"--"+table.getColumnName());
                //处理结果
                List<Res> res=mapper.getUrlByTable(table,primaryKey);
                if(res.size()!=0){
                    System.out.println("查询出"+dataBase+"--"+table.getTableName()+"--"+table.getColumnName());
                    for(Res r:res){
                        r.setColumnName(table.getColumnName());
                        r.setTableName(table.getTableName());
                        r.setDataBase(dataBase);
                        //判断链接是否有效，有的值由几个url用“，”连接的
                        String[] urls=r.getUrl().split(",");
                        for(String url:urls){
                            if(!exist(url)){
                                r.setIsOk("否");
                                r.setUrl(url);
                                result.add(r);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("搜索到"+result.size()+"条数据");
        //文件名
        String filename = "F:\\imageUrl.xlsx";
        EasyExcel.write(filename, Res.class).sheet("图片链接信息").doWrite(result);
    }
    /**搜素指定的一个数据库*/
    @GetMapping("/singleDataBase")
    public void singleDataBase(HttpServletResponse response) throws IOException {

        String dataBase="app_dev";
        List<Res> result=new ArrayList<>();
        //获取数据库下，所有的表与字段，会查出视图
        List<Table> list=checkImageService.getListTable(dataBase);
        // 调用数据库操作方法
        String tempTable=list.get(0).getTableName();
        String primaryKey=checkImageService.getPrimaryByTableName(tempTable);
        for(Table table:list){
            //如果表切换，则主键重新获取
            if(!tempTable.equals(table.getTableName())){
                tempTable=table.getTableName();
                //排除主键不是id，根据表名查主键
                primaryKey=checkImageService.getPrimaryByTableName(table.getTableName());
            }
            //排除视图
            if(table.getTableName().subSequence(0,2).equals("v_")){
                continue;
            }
            //排除主键不存在
            if(primaryKey==null){
                continue;
            }
            //排除关键词字段导致的错误，还有很多类似的，根据实际情况，灵活排除
            if(table.getColumnName().equals("read")||table.getColumnName().equals("key")||table.getColumnName().equals("function")){
                continue;
            }
            //查询对应表的字段是否含有url(https://，http://，.jpg,.png,.gif)
            System.out.println("处理到"+dataBase+"--"+table.getTableName()+"--"+table.getColumnName());
            List<Res> res=checkImageService.getUrlByTable(table,primaryKey);
            if(res.size()!=0){
                System.out.println("查询出"+dataBase+"--"+table.getTableName()+"--"+table.getColumnName());
                for(Res r:res){
                    r.setColumnName(table.getColumnName());
                    r.setTableName(table.getTableName());
                    r.setDataBase(dataBase);
                    //判断链接是否有效,有的值由几个url用，连接的
                    String[] urls=r.getUrl().split(",");
                    for(String url:urls){
                        if(!exist(url)){
                            r.setIsOk("否");
                            r.setUrl(url);
                            result.add(r);
                        }
                    }
                }
            }
        }
        System.out.println(result.size());
        //文件名
        String filename = "F:\\imageUrl.xlsx";
        EasyExcel.write(filename, Res.class).sheet("图片链接信息").doWrite(result);
    }

    public SqlSessionFactory getSqlSessionFactory(String dataBase) throws Exception {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://0.0.0.0:3306/"+dataBase+"?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //配置mapper路径
        Resource[] resources = resolver.getResources("classpath:com/zhj/springboot/mapper/*.xml");
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setMapperLocations(resources);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfiguration(new MybatisConfiguration());
        return sqlSessionFactoryBean.getObject();
    }

}
