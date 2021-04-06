package com.example.service;

import com.example.api.dto.PodListDTO;
import com.example.api.mapper.ApiV1Mapper;
import com.google.gson.GsonBuilder;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PodService {

    private final ApiV1Mapper apiV1Mapper;

    public List<PodListDTO> findNamespacedPods(String namespace) throws ApiException {
        V1PodList pods = apiV1Mapper.addCoreV1Api(namespace);

        log.info("Pods INFO \n{}", new GsonBuilder().setPrettyPrinting().create().toJson(pods));

        return Optional.ofNullable(pods)
                .map(v1PodList -> v1PodList.getItems().stream()
                        .map(PodListDTO::new)
                        .collect(Collectors.toList())
                ).orElse(null);
    }
}
