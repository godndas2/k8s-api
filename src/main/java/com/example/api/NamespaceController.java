package com.example.api;

import com.google.gson.GsonBuilder;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1NamespaceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class NamespaceController {

    @GetMapping(value = "/api/createnamespace/{namespace}")
    public V1Namespace createNamespace(@PathVariable("namespace") String namespace) throws Exception {
        CoreV1Api coreV1Api = new CoreV1Api();

        V1Namespace v1Namespace = new V1NamespaceBuilder()
                .withNewMetadata()
                .withName(namespace)
                .endMetadata()
                .build();

        V1Namespace ns = coreV1Api.createNamespace(v1Namespace, null, null, null);

        log.info("ns info \n{}", new GsonBuilder().setPrettyPrinting().create().toJson(ns));

        return ns;
    }
}
