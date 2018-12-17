package org.mz.ditran.sample.dubbo.active;

import org.mz.ditran.sample.dubbo.active.service.ActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 10:30
 */
@ImportResource(locations = {"classpath:spring-dubbo-consumer.xml"})
@SpringBootApplication
@RestController
public class Application {

    @Autowired
    private ActiveService activeService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }
    @GetMapping("/{account}/{amount}")
    public void trans(@PathVariable String account, @PathVariable BigDecimal amount){
        activeService.transMoney(account,amount);
    }


}
