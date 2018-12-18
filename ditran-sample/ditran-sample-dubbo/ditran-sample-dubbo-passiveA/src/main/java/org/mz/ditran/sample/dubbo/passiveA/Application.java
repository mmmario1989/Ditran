package org.mz.ditran.sample.dubbo.passiveA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 10:30
 */
@ImportResource(locations = {"classpath:spring-dubbo-provider.xml"})
@SpringBootApplication
public class Application implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.addListeners(new Application());
        app.run(args);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            DataSource dataSource = (DataSource) event.getApplicationContext().getBean("dataSource");
            Connection conn = dataSource.getConnection();
            FileSystemResource rc = new FileSystemResource("data_script.sql");
            EncodedResource er = new EncodedResource(rc, "UTF-8");
            ScriptUtils.executeSqlScript(conn, er);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
