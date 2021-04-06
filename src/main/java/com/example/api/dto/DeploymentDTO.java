package com.example.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private ServiceDTO serviceDTO;
    private VolumeDTO volumeDTO;

    public DeploymentDTO(String appName, String namespace, String image, String imageName, int numberOfPods, String label, int ports, ServiceDTO serviceDTO, VolumeDTO volumeDTO) {
        this.appName = appName;
        this.namespace = namespace;
        this.image = image;
        this.imageName = imageName;
        this.numberOfPods = numberOfPods;
        this.label = label;
        this.ports = ports;
        this.serviceDTO = serviceDTO;
        this.volumeDTO = volumeDTO;
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
