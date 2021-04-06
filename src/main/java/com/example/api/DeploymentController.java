package com.example.api;

import com.example.api.dto.DeploymentDTO;
import com.example.service.DeploymentService;
import com.example.service.PodService;
import com.example.service.SvcService;
import com.example.service.VolumeService;
import io.kubernetes.client.openapi.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DeploymentController {

    private final DeploymentService deploymentService;
    private final PodService podService;
    private final SvcService svcService;
    private final VolumeService volumeService;

    /**
     * List Deployments.app in specific namespace
     *
     * @param namespace
     * @return
     * @throws ApiException
     */
    @GetMapping(value = "/api/namespaces/{namespace}/deployments")
    public ModelAndView getNamespacedDeployments(@PathVariable("namespace") String namespace) throws ApiException {
        ModelAndView modelAndView = new ModelAndView("deploymentList");
        modelAndView.addObject("deploymentListDTO", deploymentService.findNamespacedDeployment(namespace));
        return modelAndView;
    }

    /**
     * Details Deployments.app in specific namespace
     *
     * @param namespace
     * @param name
     * @return
     * @throws ApiException
     */
    @GetMapping(value = "/api/namespaces/{namespace}/deployments/{name}")
    public ModelAndView getNamespacedDeploymentDetails(@PathVariable("namespace") String namespace,
                                                       @PathVariable("name") String name) throws ApiException {
        ModelAndView modelAndView = new ModelAndView("deploymentDetail");
        modelAndView.addObject("deploymentDetailsDTO", deploymentService.findNamespacedDeploymentDetails(namespace, name));
        modelAndView.addObject("podListResponseDTO", podService.findNamespacedPods(namespace));
        return modelAndView;
    }

    /**
     * Create Deployments.app UI
     *
     */
    @GetMapping(value = "/api/deploy/deployments")
    public ModelAndView saveWithNamespacedDeploymentUI() {
        ModelAndView modelAndView = new ModelAndView("deploymentSave");
        modelAndView.addObject("deploymentSaveResponseDTO", new DeploymentDTO());
        return modelAndView;
    }

    /**
     * Create Deployments.app with namespace
     *
     * @param namespace, saveResponseDTO, builder
     * @return org.springframework.http.ResponseEntity
     * @throws ApiException
     */
    @PostMapping(value = "/api/namespaces/{namespace}/deployments")
    public ResponseEntity<DeploymentDTO> saveWithNamespacedDeployment(@PathVariable("namespace") String namespace,
                                                                      @RequestBody DeploymentDTO deploymentDTO,
                                                                      UriComponentsBuilder builder) throws ApiException {
        HttpHeaders headers = new HttpHeaders();
        deploymentService.createNamespacedDeployment(namespace, deploymentDTO);
        svcService.createNamespacedService(namespace, deploymentDTO.getServiceDTO());
//        volumeService.createStorageClass(deploymentDTO.getVolumeDTO());
        volumeService.createNamespacedPVC(namespace, deploymentDTO.getVolumeDTO());
        volumeService.createNamespacedPV(deploymentDTO.getVolumeDTO());
        headers.setLocation(builder.path("/api/namespaces/{namespace}/deployments").buildAndExpand(deploymentDTO.getNamespace()).toUri());
        return new ResponseEntity<>(deploymentDTO, headers, HttpStatus.CREATED);
    }

    /**
     * Delete Deployments.app with namespace
     *
     * @param namespace, name
     * @return org.springframework.http.ResponseEntity
     * @throws ApiException
     */
    @DeleteMapping(value = "/api/namespaces/{namespace}/deployments/{name}")
    public ResponseEntity<Void> deleteWithNamespacedDeployment(@PathVariable("namespace") String namespace,
                                                               @PathVariable String name) throws ApiException {
        deploymentService.deleteNamespacedDeployment(name,namespace);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}



