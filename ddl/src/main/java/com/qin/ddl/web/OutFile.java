package com.qin.ddl.web;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DataWriter;
import org.apache.ddlutils.io.DatabaseDataIO;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author qinjp
 * @date 2020/11/18
 */
public class OutFile {
    public static void main(String[] args) throws FileNotFoundException, CloneNotSupportedException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://47.185.100.77:3306/gz-airport");
        dataSource.setUsername("root");
        dataSource.setPassword("373616885");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        Platform platform = PlatformFactory.createNewPlatformInstance(dataSource);


        Database database = platform.readModelFromDatabase(null);

        // 写表格式
        new DatabaseIO().write(database,"D:\\table.xml" );

        FileOutputStream file = new FileOutputStream("D:\\data.xml");
        DataWriter dataWriter = new DataWriter(file);
        new DatabaseDataIO().writeDataToXML(platform, database, dataWriter);


    }


}
