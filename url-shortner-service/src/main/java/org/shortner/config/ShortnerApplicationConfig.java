package org.shortner.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShortnerApplicationConfig {

    @Value("${hash-algorithm}")
    private String hashAlgorithm;

    @Bean
    public DigestUtils digestUtils() {
        return new DigestUtils(hashAlgorithm);
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder().create();
    }
}
