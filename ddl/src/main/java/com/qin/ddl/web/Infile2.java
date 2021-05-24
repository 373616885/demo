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

/**
 * @author qinjp
 * @date 2020/11/20
 */
public class Infile2 {

    public static void main(String[] args) throws FileNotFoundException {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:p6spy:mysql://47.185.100.77:3306/zzsim-airport");
        dataSource.setUsername("root");
        dataSource.setPassword("373616885");
        dataSource.setDriverClassName("com.p6spy.engine.spy.P6SpyDriver");
        Platform platform = PlatformFactory.createNewPlatformInstance(dataSource);

        File file = ResourceUtils.getFile("classpath:record_table.xml");
        Database database = new DatabaseIO().read(file);

        platform.createTables(database,true,true);

    }
}
