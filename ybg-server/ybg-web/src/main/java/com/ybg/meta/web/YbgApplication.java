package com.ybg.meta.web;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * describe:
 *
 * @author admin
 * @date 2021/12/29 09:40
 */
@ComponentScan(basePackages = "com.ybg.meta")
@ForestScan(basePackages = "com.ybg.meta")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class YbgApplication {

    public static void main(String[] args) {
        SpringApplication.run(YbgApplication.class, args);
    }
}
