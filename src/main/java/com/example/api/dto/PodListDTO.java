package com.example.api.dto;

import io.kubernetes.client.openapi.models.V1Pod;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PodListDTO {
    private String name;
    private String status;
    private int restartCount;
    private String age;

    public PodListDTO(V1Pod v1Pod) {
        this.name = v1Pod.getMetadata().getName();
        this.status = v1Pod.getStatus().getPhase();
        this.restartCount = v1Pod.getStatus().getContainerStatuses().get(0).getRestartCount();
        this.age = v1Pod.getMetadata().getCreationTimestamp().toString("yyyy-MM-dd HH:mm");
    }
}
