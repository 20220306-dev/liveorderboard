package com.example.orderboard;

import com.example.orderboard.adapters.AdaptersConfig;
import com.example.orderboard.domain.DomainConfig;
import com.example.orderboard.interfaces.InterfacesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import({InterfacesConfig.class,
        DomainConfig.class,
        AdaptersConfig.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
