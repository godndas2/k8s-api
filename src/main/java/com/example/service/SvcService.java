package com.example.service;

import com.example.api.dto.DeploymentDTO;
import com.example.api.mapper.ApiV1Mapper;
import com.google.gson.GsonBuilder;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SvcService {
    private final ApiV1Mapper apiMapper;

    public void createNamespacedService(String namespace, DeploymentDTO.ServiceDTO serviceDTO) throws ApiException {
        V1Service service = apiMapper.addCoreV1Api(namespace, serviceDTO);
        log.info("Apply Service INFO \n{}", new GsonBuilder().setPrettyPrinting().create().toJson(service));
    }
}
