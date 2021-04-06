package com.example.service;

import com.example.api.dto.DeploymentDTO;
import com.example.api.dto.DeploymentListDTO;
import com.example.api.dto.DeploymentDetailsDTO;
import com.example.api.mapper.ApiV1Mapper;
import com.google.gson.GsonBuilder;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeploymentService {

    private final ApiV1Mapper apiMapper;

    public List<DeploymentListDTO> findNamespacedDeployment(String namespace) throws ApiException {
        V1DeploymentList deployments = apiMapper.addAppsV1Api(namespace);

        log.info("Deployments.app INFO \n{}", new GsonBuilder().setPrettyPrinting().create().toJson(deployments));

        return Optional.ofNullable(deployments)
                .map(v1DeploymentList -> v1DeploymentList.getItems().stream()
                        .map(DeploymentListDTO::new)
                        .collect(Collectors.toList())
                ).orElse(null);
    }

    public DeploymentDetailsDTO findNamespacedDeploymentDetails(String namespace, String name) throws ApiException {
        V1Deployment deployment = apiMapper.addAppsV1Api(name,namespace);

        log.info("Details Deployments.app INFO \n{}", new GsonBuilder().setPrettyPrinting().create().toJson(deployment));

        return new DeploymentDetailsDTO(deployment);
    }

    public void createNamespacedDeployment(String namespace, DeploymentDTO saveResponseDTO) throws ApiException {
        V1Deployment deployment = apiMapper.addAppsV1Api(namespace, saveResponseDTO);
        log.info("Deploy Deployments.app INFO \n{}", new GsonBuilder().setPrettyPrinting().create().toJson(deployment));
    }

    public void deleteNamespacedDeployment(String name, String namespace) throws ApiException {
        V1Status deployment = apiMapper.addAppsV1ApiForDelete(name, namespace);
        log.info("Delete Deployments.app INFO \n{}", new GsonBuilder().setPrettyPrinting().create().toJson(deployment));
    }

}

