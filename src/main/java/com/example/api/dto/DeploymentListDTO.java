package com.example.api.dto;

import io.kubernetes.client.openapi.models.V1Deployment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeploymentListDTO {
    private String name;
    private Integer ready;
    private String namespace;
    private String age;

    public DeploymentListDTO(V1Deployment v1Deployment) {
        this.name = v1Deployment.getMetadata().getName();
        if (v1Deployment.getStatus().getReadyReplicas() == null) {
            this.ready = Integer.valueOf("0");
        } else {
            this.ready = v1Deployment.getStatus().getReadyReplicas();
        }
        this.namespace = v1Deployment.getMetadata().getNamespace();
        this.age = v1Deployment.getMetadata().getCreationTimestamp().toString("yyyy-MM-dd HH:mm");
    }

}
