package com.mz.ditran.sample.util;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 09:59
 */
public class SqlUtils {

    /**
     * 自动执行sql
     * @param dataSource
     * @throws Exception
     */
    public static void runSqlBySpringUtils(DataSource dataSource) throws Exception {
        try {
            Connection conn = dataSource.getConnection();
            FileSystemResource rc = new FileSystemResource("passive_a_script.sql");
            EncodedResource er = new EncodedResource(rc, "GBK");
            ScriptUtils.executeSqlScript(conn, er);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
