package org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.shortner", "org.data"})
public class URLShortnerMain {
    public static void main(String[] args) {
        SpringApplication.run(URLShortnerMain.class, args);
    }
}
