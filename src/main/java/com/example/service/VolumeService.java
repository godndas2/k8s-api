package com.example.service;

import com.example.api.dto.DeploymentDTO;
import com.example.api.mapper.ApiV1Mapper;
import com.google.gson.GsonBuilder;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1PersistentVolume;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaim;
import io.kubernetes.client.openapi.models.V1StorageClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VolumeService {

    private final ApiV1Mapper apiMapper;

    public void createNamespacedPVC(String namespace, DeploymentDTO.VolumeDTO deploymentDTO) throws ApiException {
        V1PersistentVolumeClaim pvc = apiMapper.addCoreV1ApiForPvc(namespace, deploymentDTO);
        log.info("Apply PVC INFO \n{}", new GsonBuilder().setPrettyPrinting().create().toJson(pvc));
    }

    public void createStorageClass(DeploymentDTO.VolumeDTO volumeDTO) throws ApiException {
        V1StorageClass sc = apiMapper.addStorageApi(volumeDTO);
        log.info("Apply StorageClass INFO \n{}", new GsonBuilder().setPrettyPrinting().create().toJson(sc));
    }

    public void createNamespacedPV(DeploymentDTO.VolumeDTO volumeDTO) throws ApiException {
        V1PersistentVolume pv = apiMapper.addCoreV1ApiForPv(volumeDTO);
        log.info("Apply PV INFO \n{}", new GsonBuilder().setPrettyPrinting().create().toJson(pv));
    }

}
