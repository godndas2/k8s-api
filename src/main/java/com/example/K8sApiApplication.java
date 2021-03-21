package com.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class K8sApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(K8sApiApplication.class, args);
    }

    /**
     * Default global settings
     * @return
     * @throws Exception
     */
    @PostConstruct
    private void setDefaultApiClient() throws Exception {
        ApiClient client = Config.fromConfig("config.yaml");
        client.setVerifyingSsl(false);
        // Create operation class
        Configuration.setDefaultApiClient(client);
    }
}
