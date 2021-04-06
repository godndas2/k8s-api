package com.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class K8sApiApplication {

    public static void main(String[] args) throws IOException {
        /**
         * Default k8s global settings
         * local pc에 있는 .kube/ config File을 Load 하면 됩니다.
         * .kube Directory 가 없다면 직접 생성해줘도 무방합니다.
         */
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        SpringApplication.run(K8sApiApplication.class, args);
    }

}
