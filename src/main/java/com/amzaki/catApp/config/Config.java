package com.amzaki.catApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    record CatBean() {}

    @Bean
    CatBean catBean() {
        System.out.println("🐈 Creating CatBean..");
        System.out.println("🐈 CatBean Created.");
        System.out.println("😺 MEOW");

        return new CatBean();
    }
}
