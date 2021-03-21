package com.example.api;

import com.google.gson.GsonBuilder;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PodsController {

    String pretty = null;
    Boolean allowWatchBookmarks = false;
    String _continue = null;
    String fieldSelector = "status.phase=Running";
    String labelSelector = null;
    Integer limit = null;
    String resourceVersion = null;
    Integer timeoutSeconds = null;
    Boolean watch = false;

    @GetMapping(value = "/api/pods/{namespace}")
    public V1PodList getPodsList(@PathVariable("namespace") String namespace) throws ApiException, ApiException {
        CoreV1Api apiInstance = new CoreV1Api();

        V1PodList v1PodList = apiInstance.listNamespacedPod(namespace,
                pretty,
                allowWatchBookmarks,
                _continue,
                fieldSelector,
                labelSelector,
                limit,
                resourceVersion,
                timeoutSeconds,
                watch);

        log.info("pod info \n{}", new GsonBuilder().setPrettyPrinting().create().toJson(v1PodList));

        return v1PodList;
    }

}
