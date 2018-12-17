package com.mz.ditran.sample;

import com.mz.ditran.sample.listener.StartUpListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 10:30
 */
@SpringBootApplication
public class Runner {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Runner.class);
        app.addListeners(new StartUpListener());
        app.run(args);
    }
}
