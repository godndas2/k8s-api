# Environments

- jdk 1.8
- io.kubernetes.client-java:10.0.0
- spring boot 2.4.4
- Client Version: v1.18.12 / Server Version: v1.18.12

# Features

* Deployments
    - List
      - Deployment
    - Detail
        - Deployment Describe
        - Deployment Pods
    - Create
        - Deployment
        - Service
        - Volume
        - Pods
* Volume
    - pv(nfs), pvc

--- 
## Test Postman Json
```
{
    "appName": "sb-test-app",
    "namespace": "huhyun",
    "image": "huhyun/k8s-demo:0.1",
    "imageName": "k8s-beginner",
    "numberOfPods": 1,
    "ports" : 8080,
    "serviceDTO": {
        "serviceName": "svc-huhyun-name",
        "serviceType": "NodePort",
        "servicePorts": 8080,
        "servicePortName": "svc-test-8080",
        "serviceTargetPort": 8080
    },
    "volumeDTO": {
        "pvcName": "huhyun-pvc",
        "accessModes": "ReadWriteMany",
        "pvName": "huhyun-pv"
    }
}
```
