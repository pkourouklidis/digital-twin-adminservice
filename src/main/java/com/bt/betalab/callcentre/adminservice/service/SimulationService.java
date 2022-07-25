/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.service;

import com.bt.betalab.callcentre.adminservice.api.LoadRequest;
import com.bt.betalab.callcentre.adminservice.api.SimulationDetails;
import com.bt.betalab.callcentre.adminservice.api.SimulationRequest;
import com.bt.betalab.callcentre.adminservice.config.AdminServiceConfig;
import com.bt.betalab.callcentre.adminservice.exceptions.AdminServiceException;
import com.bt.betalab.callcentre.adminservice.logging.LogLevel;
import com.bt.betalab.callcentre.adminservice.logging.Logger;
import com.bt.betalab.callcentre.adminservice.logging.Messages;
import com.bt.betalab.callcentre.adminservice.messaging.WebClientFactory;
import com.bt.betalab.callcentre.adminservice.model.Simulation;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
        if (simulation.getStatus().equals("running") || simulation.getStatus().equals("stopped")) {
            simulation.setStatus("creating");
            if (simulation.getStatus().equals("running")) {
                stopSimulation(config);
            }
            simulation = new Simulation(request);
            simulation.setStatus("stopped");
            startSimulation(config);
        }
        throw new AdminServiceException();
    }

    public void updateSimulation(SimulationRequest request, AdminServiceConfig config) throws AdminServiceException {
        if (!simulation.getStatus().equals("failed")) {
            simulation.update(request);
        }
        throw new AdminServiceException();
    }

    public void startSimulation(AdminServiceConfig config) throws AdminServiceException {
        if (simulation.getStatus().equals("stopped") || simulation.getStatus().equals("paused")) {
            simulation.setStatus("starting");
            startStopWorkers(true, config);
            startStopLoadGenerator(true, config);
            simulation.setStatus("running");
        }
        throw new AdminServiceException();
    }

    public void pauseSimulation(AdminServiceConfig config) throws AdminServiceException {
        if (simulation.getStatus().equals("running")) {
            startStopLoadGenerator(false, config);
            startStopWorkers(false, config);
        }
        throw new AdminServiceException();
    }

    public void stopSimulation(AdminServiceConfig config) throws AdminServiceException {
        if (simulation.getStatus().equals("running")) {
            startStopLoadGenerator(false, config);
            startStopWorkers(false, config);
            purgeQueue(config);
        }
        throw new AdminServiceException();
    }

    public Simulation getSimulationStatus(AdminServiceConfig config) throws AdminServiceException {
        simulation.setActiveWorkers(getActiveWorkerCount());
        simulation.setQueueDepth(getQueueDepth(config));
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
            kubernetesService.createWorkers(simulation.getWorkers(), simulation.getWorkerSkillBias(), simulation.getWorkerSpeedBias(), config);
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
            channel.queuePurge(config.getQueueName());
        } catch (IOException e) {
            Logger.log(Messages.CHANNELERRORMESSAGEQUEUEMESSAGE + e.getMessage(), LogLevel.ERROR);
        } catch (TimeoutException e) {
            Logger.log(Messages.CONNECTIONERRORMESSAGEQUEUEMESSAGE + e.getMessage(), LogLevel.ERROR);
        }
        throw new AdminServiceException();
    }

    public int getQueueDepth(AdminServiceConfig config) throws AdminServiceException {
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

            AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(config.getQueueName(), false, false, false, null);
            return declareOk.getMessageCount();
        } catch (IOException e) {
            Logger.log(Messages.CHANNELERRORMESSAGEQUEUEMESSAGE + e.getMessage(), LogLevel.ERROR);
        } catch (TimeoutException e) {
            Logger.log(Messages.CONNECTIONERRORMESSAGEQUEUEMESSAGE + e.getMessage(), LogLevel.ERROR);
        }
        throw new AdminServiceException();
    }
}
