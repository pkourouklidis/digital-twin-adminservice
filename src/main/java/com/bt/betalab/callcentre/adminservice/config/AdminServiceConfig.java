/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminServiceConfig {
    private String queueAddress;
    private String queuePort;
    private String queueUser;
    private String queuePassword;
    private String queueName;
    private String loadGeneratorUrl;
    private String callReportingUrl;

    private String kubernetesNameSpace;

    private String workerImage;

    private String myReportingUrl;

    public String getQueueAddress() {
        return queueAddress;
    }

    public void setQueueAddress(String queueAddress) {
        this.queueAddress = queueAddress;
    }

    public int getQueuePort() {
        try {
            return Integer.valueOf(queuePort);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void setQueuePort(String queuePort) {
        this.queuePort = queuePort;
    }

    public String getQueueUser() {
        return queueUser;
    }

    public void setQueueUser(String queueUser) {
        this.queueUser = queueUser;
    }

    public String getQueuePassword() {
        return queuePassword;
    }

    public void setQueuePassword(String queuePassword) {
        this.queuePassword = queuePassword;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getLoadGeneratorUrl() {
        return loadGeneratorUrl;
    }

    public void setLoadGeneratorUrl(String loadGeneratorUrl) {
        this.loadGeneratorUrl = loadGeneratorUrl;
    }

    public String getCallReportingUrl() {
        return callReportingUrl;
    }

    public void setCallReportingUrl(String callReportingUrl) {
        this.callReportingUrl = callReportingUrl;
    }

    public String getKubernetesNameSpace() {
        return kubernetesNameSpace;
    }

    public void setKubernetesNameSpace(String kubernetesNameSpace) {
        this.kubernetesNameSpace = kubernetesNameSpace;
    }

    public String getWorkerImage() {
        return workerImage;
    }

    public void setWorkerImage(String workerImage) {
        this.workerImage = workerImage;
    }

    public String getMyReportingUrl() {
        return myReportingUrl;
    }

    public void setMyReportingUrl(String myReportingUrl) {
        this.myReportingUrl = myReportingUrl;
    }
}
