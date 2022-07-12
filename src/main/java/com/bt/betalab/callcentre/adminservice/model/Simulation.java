/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.model;


import com.bt.betalab.callcentre.adminservice.api.SimulationRequest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Simulation {
    private String simulationId = UUID.randomUUID().toString();
    private Timestamp simulationStartTime = new Timestamp((new Date()).getTime());

    private int callDelay = 50;
    private int difficultyBias = 50;
    private int waitTimeBias = 50;
    private int serviceTimeBias = 50;
    private int understandingBias = 50;
    private int workers = 0;

    private int queueDepth = 0;
    private int activeWorkers = 0;
    private String status = "stopped";

    public Simulation() {}

    public Simulation(SimulationRequest request) {
        this.callDelay = request.getCallDelay();
        this.difficultyBias = request.getDifficultyBias();
        this.waitTimeBias = request.getWaitTimeBias();
        this.serviceTimeBias = request.getServiceTimeBias();
        this.understandingBias = request.getUnderstandingBias();
        this.workers = request.getWorkers();
    }

    public void update(SimulationRequest request) {
        this.callDelay = request.getCallDelay();
        this.difficultyBias = request.getDifficultyBias();
        this.waitTimeBias = request.getWaitTimeBias();
        this.serviceTimeBias = request.getServiceTimeBias();
        this.understandingBias = request.getUnderstandingBias();
        this.workers = request.getWorkers();
    }

    public String getSimulationId() {
        return simulationId;
    }

    public Timestamp getSimulationStartTime() {
        return simulationStartTime;
    }

    public int getCallDelay() {
        return callDelay;
    }

    public int getDifficultyBias() {
        return difficultyBias;
    }

    public int getWaitTimeBias() {
        return waitTimeBias;
    }

    public int getServiceTimeBias() {
        return serviceTimeBias;
    }

    public int getUnderstandingBias() {
        return understandingBias;
    }

    public int getWorkers() {
        return workers;
    }

    public int getQueueDepth() {
        return queueDepth;
    }

    public int getActiveWorkers() {
        return activeWorkers;
    }

    public void setQueueDepth(int queueDepth) {
        this.queueDepth = queueDepth;
    }

    public void setActiveWorkers(int activeWorkers) {
        this.activeWorkers = activeWorkers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
