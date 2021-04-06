package com.example.api.dto;

import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Deployment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class DeploymentDetailsDTO {
    private String label;
    private String namespace;
    private String name;

    private String annotations;
    private String creationTime;
    private String selector;

    private String image;

    public DeploymentDetailsDTO(V1Deployment v1Deployment) {
        this.label = v1Deployment.getMetadata().getLabels().entrySet().stream().map(str -> str.getKey() + "=" + str.getValue()).collect(Collectors.joining());
        this.namespace = v1Deployment.getMetadata().getNamespace();
        this.name = v1Deployment.getMetadata().getName();
        this.annotations = v1Deployment.getMetadata().getAnnotations().entrySet().stream().map(str -> str.getKey() + ":" + str.getValue()).findFirst().get();
        this.creationTime = v1Deployment.getMetadata().getCreationTimestamp().toString("yyyy-MM-dd HH:mm");
        this.selector = v1Deployment.getSpec().getSelector().getMatchLabels().entrySet().stream().map(str -> str.getKey() + "=" + str.getValue()).collect(Collectors.joining());
        this.image = v1Deployment.getSpec().getTemplate().getSpec().getContainers().stream().map(V1Container::getImage).collect(Collectors.joining());
    }

}
