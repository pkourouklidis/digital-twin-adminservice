/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.service;

import com.bt.betalab.callcentre.adminservice.api.CallResult;
import com.bt.betalab.callcentre.adminservice.config.AdminServiceConfig;
import com.bt.betalab.callcentre.adminservice.exceptions.AdminServiceException;
import com.bt.betalab.callcentre.adminservice.logging.LogLevel;
import com.bt.betalab.callcentre.adminservice.logging.Logger;
import com.bt.betalab.callcentre.adminservice.messaging.WebClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ReportingService {

    @Autowired
    WebClientFactory clientFactory;

    public void reportCallResult(CallResult result, AdminServiceConfig config) throws AdminServiceException {
        WebClient webClient = clientFactory.generateWebClient(config.getCallReportingUrl());
        ResponseEntity reply = webClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(result)
                .retrieve()
                .toBodilessEntity()
                .block();

        if (!reply.getStatusCode().is2xxSuccessful()) {
            Logger.log("Failed to report call result. Error code: " + reply.getStatusCodeValue(), LogLevel.ERROR);
            throw new AdminServiceException();
        }
    }
}
