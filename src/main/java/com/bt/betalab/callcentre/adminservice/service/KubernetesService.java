/**
 * @author Joost Noppen (611749237), BetaLab, R&I
 * @version 0.9
 * <p>
 * Copyright (c) British Telecommunications plc 2018
 */

package com.bt.betalab.callcentre.adminservice.service;

import com.bt.betalab.callcentre.adminservice.config.AdminServiceConfig;
import com.bt.betalab.callcentre.adminservice.exceptions.AdminServiceException;
import com.bt.betalab.callcentre.adminservice.kubernetes.KubernetesBodyFactory;
import com.bt.betalab.callcentre.adminservice.logging.LogLevel;
import com.bt.betalab.callcentre.adminservice.logging.Logger;
import com.bt.betalab.callcentre.adminservice.logging.Messages;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1DeleteOptions;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KubernetesService {

    private static final String PROPAGATION_POLICY_BACKGROUND = "Background";
    private static final String NOT_FOUND_MSG = "Not Found";

    String kubernetesNameSpace;

    AppsV1Api appsV1APIClient;

    public KubernetesService(AdminServiceConfig config, ApiClient apiClient) {
        Configuration.setDefaultApiClient(apiClient);
        apiClient.setDebugging(System.getenv("DEBUG_MODE") != null);
        appsV1APIClient = new AppsV1Api(apiClient);
        this.kubernetesNameSpace = config.getKubernetesNameSpace();
    }

    public void createWorkers(int target, int skillBias, int speedBias, int normalWaitTime, int normalServiceTime, int bounceWaitTime, AdminServiceConfig config) throws AdminServiceException {
        if (kubernetesNameSpace != null) {
            try {
                if (existsDeployment()) {
                    V1Patch body = KubernetesBodyFactory.producePatchBody(target);
                    appsV1APIClient.patchNamespacedDeploymentScale("workers", kubernetesNameSpace, body, "false", null, null, null);
                } else {
                    V1Deployment body = KubernetesBodyFactory.produceDeploymentBody("workers", config.getWorkerImage(), kubernetesNameSpace, target, config.getMyReportingUrl(), config.getMyReportingUrlUser(), config.getMyReportingUrlPassword(), skillBias, speedBias, normalWaitTime, normalServiceTime, bounceWaitTime, config);
                    appsV1APIClient.createNamespacedDeployment(kubernetesNameSpace, body, "false", null, null);
                }
                if (!waitForWorkersToArrive(target)) { throw new AdminServiceException(); }
            } catch (ApiException e) {
                Logger.log(Messages.KUBEAPIEXCEPTIONMESSAGE + " (" + e.getMessage() + ")", LogLevel.ERROR);
                throw new AdminServiceException();
            }
        } else {
            Logger.log(Messages.INVALIDKUBERNETESCONFIGURATIONMESSAGE, LogLevel.ERROR);
            throw new AdminServiceException();
        }
    }

    public void deleteWorkers() throws AdminServiceException {
        if (kubernetesNameSpace != null) {
            try {
                if (existsDeployment()) {
                    V1DeleteOptions options = KubernetesBodyFactory.produceDeleteOptionsBody();
                    appsV1APIClient.deleteNamespacedDeployment("workers", kubernetesNameSpace, "true", null, 0, false, KubernetesService.PROPAGATION_POLICY_BACKGROUND, options);
                }
            } catch (ApiException e) {
                Logger.log(Messages.KUBEAPIEXCEPTIONMESSAGE + " (" + e.getMessage() + ")", LogLevel.ERROR);
                throw new AdminServiceException();
            }
        } else {
            Logger.log(Messages.INVALIDKUBERNETESCONFIGURATIONMESSAGE, LogLevel.ERROR);
            throw new AdminServiceException();
        }
    }

    public int activeWorkers() throws AdminServiceException {
        if (kubernetesNameSpace != null) {
            try {
                if (existsDeployment()) {
                    return appsV1APIClient.readNamespacedDeploymentStatus("workers", kubernetesNameSpace, "false")
                            .getStatus()
                            .getAvailableReplicas();
                }
                return 0;
            } catch (ApiException e) {
                Logger.log(Messages.KUBEAPIEXCEPTIONMESSAGE + " (" + e.getMessage() + ")", LogLevel.ERROR);
            }
        } else {
            Logger.log(Messages.INVALIDKUBERNETESCONFIGURATIONMESSAGE, LogLevel.ERROR);
        }
        throw new AdminServiceException();
    }

    public boolean waitForWorkersToArrive(int target) {
        int timeOut = 300000; // Timeout is five minutes
        int currentTime = 0;

        while (currentTime < timeOut) {
            try {
                Thread.sleep(1000);
                currentTime += 1000;
                if (activeWorkers() == target) { return true; };
            } catch (Exception e) {
                Logger.log(Messages.KUBEAPIEXCEPTIONMESSAGE + " (" + e.getMessage() + ")", LogLevel.ERROR);
            }
        }
        return false;
    }

    public boolean existsDeployment() throws AdminServiceException {
        if (kubernetesNameSpace != null) {
            try {
                appsV1APIClient.readNamespacedDeployment("workers", kubernetesNameSpace, "false", null, null);
                return true;
            } catch (ApiException e) {
                if (e.getCode() == 404) { return false; }
                Logger.log(Messages.KUBEAPIEXCEPTIONMESSAGE + " (" + e.getMessage() + ")", LogLevel.ERROR);
            }
        } else {
            Logger.log(Messages.INVALIDKUBERNETESCONFIGURATIONMESSAGE, LogLevel.ERROR);
        }
        throw new AdminServiceException();
    }
}