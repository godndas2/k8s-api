package com.example.api.dto;

import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Deployment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class DeploymentDTO {
    private String appName;
    private String namespace;
    private String image;
    private String imageName;
    private int numberOfPods;
    private String label;
    private int ports;
    private String annotations;
    private String creationTime;
    private String selector;
    private Integer ready;

    private ServiceDTO serviceDTO;
    private VolumeDTO volumeDTO;

    public DeploymentDTO(String appName, String namespace, String image, String imageName, int numberOfPods, String label, int ports, String annotations, String creationTime, String selector, Integer ready, ServiceDTO serviceDTO, VolumeDTO volumeDTO) {
        this.appName = appName;
        this.namespace = namespace;
        this.image = image;
        this.imageName = imageName;
        this.numberOfPods = numberOfPods;
        this.label = label;
        this.ports = ports;
        this.annotations = annotations;
        this.creationTime = creationTime;
        this.selector = selector;
        this.ready = ready;
        this.serviceDTO = serviceDTO;
        this.volumeDTO = volumeDTO;
    }

    public DeploymentDTO(V1Deployment v1Deployment) {
        this.label = v1Deployment.getMetadata().getLabels().entrySet().stream().map(str -> str.getKey() + "=" + str.getValue()).collect(Collectors.joining());
        this.namespace = v1Deployment.getMetadata().getNamespace();
        this.appName = v1Deployment.getMetadata().getName();
        this.annotations = v1Deployment.getMetadata().getAnnotations().entrySet().stream().map(str -> str.getKey() + ":" + str.getValue()).findFirst().get();
        this.creationTime = v1Deployment.getMetadata().getCreationTimestamp().toString("yyyy-MM-dd HH:mm");
        if (v1Deployment.getStatus().getReadyReplicas() == null) {
            this.ready = Integer.valueOf("0");
        } else {
            this.ready = v1Deployment.getStatus().getReadyReplicas();
        }
        this.selector = v1Deployment.getSpec().getSelector().getMatchLabels().entrySet().stream().map(str -> str.getKey() + "=" + str.getValue()).collect(Collectors.joining());
        this.image = v1Deployment.getSpec().getTemplate().getSpec().getContainers().stream().map(V1Container::getImage).collect(Collectors.joining());
    }

    @Getter
    @NoArgsConstructor
    public static class ServiceDTO {
        private String serviceName;
        private String serviceType;
        private String servicePorts;
        private String servicePortName;
        private int serviceTargetPort;

        public ServiceDTO(String serviceName, String serviceType, String servicePorts, String servicePortName, int serviceTargetPort) {
            this.serviceName = serviceName;
            this.serviceType = serviceType;
            this.servicePorts = servicePorts;


            this.servicePortName = servicePortName;
            this.serviceTargetPort = serviceTargetPort;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class VolumeDTO {
        private String pvcName;
        private String storageClassName;
        private String accessModes;
        private String pvName;

        public VolumeDTO(String pvcName, String storageClassName, String accessModes, String pvName) {
            this.pvcName = pvcName;
            this.storageClassName = storageClassName;
            this.accessModes = accessModes;
            this.pvName = pvName;
        }
    }


}
