package com.example.api.mapper;

import com.example.api.dto.DeploymentDTO;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.StorageV1Api;
import io.kubernetes.client.openapi.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApiV1Mapper {
    private static final AppsV1Api APPSV1API = new AppsV1Api();
    private static final CoreV1Api COREV1API = new CoreV1Api();
    private static final StorageV1Api STORAGEV1API = new StorageV1Api();

    private String pretty;
    private String _continue;
    private String fieldSelector;
    private String labelSelector;
    private String dryRun;
    private String resourceVersion;
    private String fieldManager;
    private Boolean allowWatchBookmarks = false;
    private Boolean watch = false;
    private Boolean exact = false;
    private Boolean export = false;
    private Integer limit;
    private Integer timeoutSeconds;
    private Integer gracePeriodSeconds;
    private Boolean orphanDependents = false; // Deprecated
    private String propagationPolicy;
    private V1DeleteOptions body = new V1DeleteOptions(); // DeleteOptions may be provided when deleting an API object.

    public V1DeploymentList addAppsV1Api(String namespace) throws ApiException {
        return APPSV1API.listNamespacedDeployment(namespace, pretty, allowWatchBookmarks, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch);
    }

    public V1Deployment addAppsV1Api(String name, String namespace) throws ApiException {
        return APPSV1API.readNamespacedDeployment(name, namespace, pretty, exact, export);
    }

    public V1Deployment addAppsV1Api(String namespace, DeploymentDTO deploymentDTO) throws ApiException {
        return APPSV1API.createNamespacedDeployment(namespace, deploymentBody(deploymentDTO), pretty, dryRun, fieldManager);
    }

    public V1Status addAppsV1ApiForDelete(String name, String namespace) throws ApiException {
        return APPSV1API.deleteNamespacedDeployment(name, namespace, pretty, dryRun, gracePeriodSeconds, orphanDependents, propagationPolicy, body);
    }

    public V1PodList addCoreV1Api(String namespace) throws ApiException {
        return COREV1API.listNamespacedPod(namespace, pretty, allowWatchBookmarks, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch);
    }

    public V1Service addCoreV1Api(String namespace, DeploymentDTO.ServiceDTO serviceDTO) throws ApiException {
        return COREV1API.createNamespacedService(namespace, svcBody(serviceDTO), pretty, dryRun, fieldManager);
    }

    public V1PersistentVolumeClaim addCoreV1ApiForPvc(String namespace, DeploymentDTO.VolumeDTO volumeDTO) throws ApiException {
        return COREV1API.createNamespacedPersistentVolumeClaim(namespace,pvcBody(volumeDTO), pretty, dryRun, fieldManager);
    }

    public V1PersistentVolume addCoreV1ApiForPv(DeploymentDTO.VolumeDTO volumeDTO) throws ApiException {
        return COREV1API.createPersistentVolume(pvBody(volumeDTO), pretty, dryRun, fieldManager);
    }

//    public V1StorageClass addStorageApi(DeploymentDTO.VolumeDTO volumeDTO) throws ApiException {
//        return STORAGEV1API.createStorageClass(scBody(volumeDTO), pretty, dryRun, fieldManager);
//    }

    /**
     * NFS
     */
    public static V1NFSVolumeSource nfs() {
        V1NFSVolumeSource nfs = new V1NFSVolumeSourceBuilder()
                .withPath("/volume1/dev/test-huhyun-vm")
                .withServer("10.20.200.201")
                .build();

        return nfs;
    }

    /**
     * Service
     */
    public V1Service svcBody(DeploymentDTO.ServiceDTO serviceDTO) {
        Map<String, String> selectLabels = new HashMap<>();
        selectLabels.put("select-name", "test-deployment");

        V1ServicePort servicePort = new V1ServicePortBuilder()
                .withName(serviceDTO.getServicePortName())
                .withPort(Integer.valueOf(serviceDTO.getServicePorts()))
                .withTargetPort(new IntOrString(Integer.valueOf(serviceDTO.getServiceTargetPort())))
                .withProtocol("TCP")
                .build();

        V1Service body = new V1ServiceBuilder()
                .withNewMetadata()
                .withName(serviceDTO.getServiceName())
                .withLabels(selectLabels)
                .endMetadata()
                .withNewSpec()
                .withType(serviceDTO.getServiceType())
                .withSelector(selectLabels)
                .withPorts(servicePort)
                .endSpec()
                .build();

        return body;
    }

    /**
     * PV
     */
    public V1PersistentVolume pvBody(DeploymentDTO.VolumeDTO volumeDTO) {
        Map<String, String> selectLabels = new HashMap<>();
        selectLabels.put("select-name", "test-deployment");

        V1PersistentVolume pv = new V1PersistentVolumeBuilder()
                .withNewMetadata()
                .withName(volumeDTO.getPvName())
                .withLabels(selectLabels)
                .endMetadata()
                .withNewSpec()
                .addToCapacity(Collections.singletonMap("storage", new Quantity("1Gi")))
                .withAccessModes(volumeDTO.getAccessModes())
                .withPersistentVolumeReclaimPolicy("Retain")
                .withNfs(nfs()) // nfs
                .endSpec()
                .build();

        return pv;
    }

    /**
     * PVC
     */
    public V1PersistentVolumeClaim pvcBody(DeploymentDTO.VolumeDTO volumeDTO) {
        Map<String, String> selectLabels = new HashMap<>();
        selectLabels.put("select-name", "test-deployment");

        V1PersistentVolumeClaim pvc = new V1PersistentVolumeClaimBuilder()
                .withNewMetadata()
                .withName(volumeDTO.getPvcName())
                .withLabels(selectLabels)
                .endMetadata()
                .withNewSpec()
                .withVolumeName(volumeDTO.getPvName()) // PV name
//                    .withStorageClassName(volumeDTO.getStorageClassName()) // storageClass
                .withAccessModes(volumeDTO.getAccessModes())
                .withNewResources()
                .addToRequests("storage", new Quantity("1Gi"))
                .endResources()
                .endSpec()
                .build();

        return pvc;
    }

    /**
     * Deployment
     */
    public V1Deployment deploymentBody(DeploymentDTO deploymentDTO) {
        Map<String, String> selectLabels = new HashMap<>();
        selectLabels.put("select-name", "test-deployment");

        V1Deployment body = new V1DeploymentBuilder()
                .withMetadata(new V1ObjectMetaBuilder()
                        .withName(deploymentDTO.getAppName())
                        .withNamespace(deploymentDTO.getNamespace())
                        .withLabels(selectLabels)
                        .build())
                .withSpec(new V1DeploymentSpecBuilder()
                        .withReplicas(deploymentDTO.getNumberOfPods())
                        .withSelector(new V1LabelSelectorBuilder()
                                .withMatchLabels(selectLabels)
                                .build())
                        //docker
                        .withTemplate(new V1PodTemplateSpecBuilder()
                                .withMetadata(new V1ObjectMetaBuilder()
                                        .withLabels(selectLabels)
                                        .build())
                                .withSpec(new V1PodSpecBuilder()
                                        .withContainers(new V1ContainerBuilder()
                                                .withName(deploymentDTO.getImageName()) //docker
                                                .withImage(deploymentDTO.getImage()) //docker
                                                .withPorts(new V1ContainerPortBuilder()
                                                        .withContainerPort(deploymentDTO.getPorts())
                                                        .withProtocol("TCP")
                                                        .build())
                                                // volume mount
                                                .withVolumeMounts(new V1VolumeMount()
                                                        .name("pvc-vm")
                                                        .mountPath(nfs().getPath())) //
                                                .build())
                                        // volume setting
                                        .withVolumes(new V1VolumeBuilder()
                                                .withName("pvc-vm")
                                                .withPersistentVolumeClaim(new V1PersistentVolumeClaimVolumeSource().claimName(deploymentDTO.getVolumeDTO().getPvcName())) // PVC Name
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();

        return body;
    }

    /**
     * StorageClass
     */
    public V1StorageClass scBody(DeploymentDTO.VolumeDTO volumeDTO) {
        V1StorageClass storageClass = new V1StorageClassBuilder()
                .withNewMetadata()
                .withName(volumeDTO.getStorageClassName())
                .endMetadata()
                .build();

        return storageClass;
    }
}
