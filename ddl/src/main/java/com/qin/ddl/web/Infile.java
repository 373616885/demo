package com.qin.ddl.web;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DatabaseDataIO;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author qinjp
 * @date 2020/11/20
 */
public class Infile {

    public static void main(String[] args) throws FileNotFoundException {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://47.185.100.77:3306/zzsim-airport");
        dataSource.setUsername("root");
        dataSource.setPassword("373616885");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        Platform platform = PlatformFactory.createNewPlatformInstance(dataSource);

        File file = ResourceUtils.getFile("classpath:record_table.xml");
        Database database = new DatabaseIO().read(file);

        DatabaseDataIO databaseDataIO = new DatabaseDataIO();
        // 不开启批量导入
        databaseDataIO.setUseBatchMode(false);
        // 重复数据直接忽略
        databaseDataIO.setEnsureFKOrder(true);
        // 遇见错误停止
        databaseDataIO.setFailOnError(true);

        String[] files = {"D:\\data.xml"};

        databaseDataIO.writeDataToDatabase(platform, database, files);

    }
}
