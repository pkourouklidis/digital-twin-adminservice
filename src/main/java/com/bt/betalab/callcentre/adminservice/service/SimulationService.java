/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.service;

import com.bt.betalab.callcentre.adminservice.api.*;
import com.bt.betalab.callcentre.adminservice.config.AdminServiceConfig;
import com.bt.betalab.callcentre.adminservice.exceptions.AdminServiceException;
import com.bt.betalab.callcentre.adminservice.logging.LogLevel;
import com.bt.betalab.callcentre.adminservice.logging.Logger;
import com.bt.betalab.callcentre.adminservice.logging.Messages;
import com.bt.betalab.callcentre.adminservice.messaging.WebClientFactory;
import com.bt.betalab.callcentre.adminservice.model.Simulation;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class SimulationService {

    @Autowired
    WebClientFactory clientFactory;
    Simulation simulation = new Simulation();

    @Autowired
    KubernetesService kubernetesService;

    public SimulationDetails createSimulation(SimulationRequest request, AdminServiceConfig config) throws AdminServiceException {
        if (simulation.getStatus().equals("stopped")) {
            simulation.setStatus("creating");
            simulation = new Simulation(request);
            startStopWorkers(true, config);
            startStopLoadGenerator(true, config);
            simulation.setStatus("running");
            return new SimulationDetails(simulation.getSimulationId(), simulation.getSimulationStartTime());
        }
        throw new AdminServiceException();
    }

    public void updateSimulation(SimulationRequest request, AdminServiceConfig config) throws AdminServiceException {
        if (simulation.getStatus().equals("running") || simulation.getStatus().equals("paused")) {
            if (simulation.getStatus().equals("running")) {
                pauseSimulation(config);
            }
            simulation.setStatus("updating");
            simulation.update(request);
            startSimulation(config);
        } else {
            throw new AdminServiceException();
        }
    }

    public void startSimulation(AdminServiceConfig config) throws AdminServiceException {
        if (simulation.getStatus().equals("paused") || simulation.getStatus().equals("updating")) {
            simulation.setStatus("starting");
            startStopWorkers(true, config);
            startStopLoadGenerator(true, config);
            simulation.setStatus("running");
        } else {
            throw new AdminServiceException();
        }
    }

    public void pauseSimulation(AdminServiceConfig config) throws AdminServiceException {
        if (simulation.getStatus().equals("running")) {
            simulation.setStatus("pausing");
            startStopLoadGenerator(false, config);
            startStopWorkers(false, config);
            simulation.setStatus("paused");
        } else {
            throw new AdminServiceException();
        }
    }

    public void stopSimulation(AdminServiceConfig config) throws AdminServiceException {
        if (simulation.getStatus().equals("running") || simulation.getStatus().equals("paused")) {
            simulation.setStatus("stopping");
            startStopLoadGenerator(false, config);
            startStopWorkers(false, config);
            purgeQueue(config);
            simulation.setStatus("stopped");
        } else {
            throw new AdminServiceException();
        }
    }

    public SimulationStatus getSimulationStatus(AdminServiceConfig config) throws AdminServiceException {
        SimulationStatus status = new SimulationStatus();
        status.setStatus(simulation.getStatus());
        status.setActiveWorkers(getActiveWorkerCount());
        status.setQueueDepth(getQueueDepth(config));
        status.setSimulationStartTime(simulation.getSimulationStartTime());
        status.setSimulationId(simulation.getSimulationId());
        return status;
    }

    public Simulation getSimulationSettings() throws AdminServiceException {
        return simulation;
    }

    public void startStopLoadGenerator(boolean start, AdminServiceConfig config) throws AdminServiceException {
        LoadRequest request = new LoadRequest(start, simulation);

        WebClient webClient = clientFactory.generateWebClient(config.getLoadGeneratorUrl());
        ResponseEntity reply = webClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();

        if (!reply.getStatusCode().is2xxSuccessful()) {
            Logger.log("Failed to update the load generator. Error code: " + reply.getStatusCodeValue(), LogLevel.ERROR);
            throw new AdminServiceException();
        }
    }

    public void startStopWorkers(boolean start, AdminServiceConfig config) throws AdminServiceException {
        if (start) {
            kubernetesService.createWorkers(simulation.getWorkers(), simulation.getWorkerSkillBias(), simulation.getWorkerSpeedBias(), simulation.getNormalWaitTime(), simulation.getNormalServiceTime(), simulation.getBounceWaitTime(), config);
        } else {
            kubernetesService.deleteWorkers();
        }
    }

    public int getActiveWorkerCount() throws AdminServiceException {
        return kubernetesService.activeWorkers();
    }

    public void purgeQueue(AdminServiceConfig config) throws AdminServiceException {
        ConnectionFactory conFactory = new ConnectionFactory();
        conFactory.setHost(config.getQueueAddress());
        conFactory.setPort(config.getQueuePort());
        conFactory.setUsername(config.getQueueUser());
        conFactory.setPassword(config.getQueuePassword());
        conFactory.setAutomaticRecoveryEnabled(true);

        Logger.log(Messages.CONNECTINGTOMESSAGEQUEUEMESSAGE + config.getQueueAddress() + ":" + config.getQueuePort(), LogLevel.INFO);

        try {
            Connection connection = conFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDelete(config.getQueueName());
        } catch (IOException e) {
            Logger.log(Messages.CHANNELERRORMESSAGEQUEUEMESSAGE + e.getMessage(), LogLevel.ERROR);
            throw new AdminServiceException();
        } catch (TimeoutException e) {
            Logger.log(Messages.CONNECTIONERRORMESSAGEQUEUEMESSAGE + e.getMessage(), LogLevel.ERROR);
            throw new AdminServiceException();
        }
    }

    public int getQueueDepth(AdminServiceConfig config) throws AdminServiceException {
        DefaultUriBuilderFactory urlFactory = new DefaultUriBuilderFactory("http://" + config.getQueueAddress() + ":" + config.getQueueApiPort() + "/api/queues/%2F/" + config.getQueueName());
        urlFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        ResponseEntity<MessageInfo> reply = WebClient.builder()
                .uriBuilderFactory(urlFactory)
                .build()
                .get()
                .headers(headers -> headers.setBasicAuth(config.getQueueUser(), config.getQueuePassword()))
                .retrieve()
                .toEntity(MessageInfo.class)
                .block();

        if (!reply.getStatusCode().is2xxSuccessful()) {
            if (reply.getStatusCodeValue() == 404) {
                return 0;
            }
            Logger.log("Failed to update the load generator. Error code: " + reply.getStatusCodeValue(), LogLevel.ERROR);
            throw new AdminServiceException();
        }

        return reply.getBody().getMessages_ready() + reply.getBody().getMessages_unacknowledged();
    }
}
