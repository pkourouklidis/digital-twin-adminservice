/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.model;


import com.bt.betalab.callcentre.adminservice.api.SimulationRequest;

import java.time.Instant;
import java.util.UUID;

public class Simulation {
    private String simulationId = UUID.randomUUID().toString();
    private Instant simulationStartTime = Instant.now();

    private int callDelay = 50;
    private int difficultyBias = 50;
    private int waitTimeBias = 50;
    private int serviceTimeBias = 50;
    private int understandingBias = 50;
    private int workers = 0;

    private int workerSkillBias = 50;

    private int workerSpeedBias = 50;

    private int normalWaitTime;

    private int normalServiceTime;

    private int bounceWaitTime;
    private String status = "stopped";

    public Simulation() {}

    public Simulation(SimulationRequest request) {
        this.callDelay = request.getCallDelay();
        this.difficultyBias = request.getDifficultyBias();
        this.waitTimeBias = request.getWaitTimeBias();
        this.serviceTimeBias = request.getServiceTimeBias();
        this.understandingBias = request.getUnderstandingBias();
        this.workers = request.getWorkers();
        this.workerSkillBias = request.getWorkerSkillBias();
        this.workerSpeedBias = request.getWorkerSpeedBias();
        this.normalWaitTime = request.getNormalWaitTime();
        this.normalServiceTime = request.getNormalServiceTime();
        this.bounceWaitTime = request.getBounceWaitTime();
    }

    public void update(SimulationRequest request) {
        this.callDelay = request.getCallDelay();
        this.difficultyBias = request.getDifficultyBias();
        this.waitTimeBias = request.getWaitTimeBias();
        this.serviceTimeBias = request.getServiceTimeBias();
        this.understandingBias = request.getUnderstandingBias();
        this.workers = request.getWorkers();
        this.workerSkillBias = request.getWorkerSkillBias();
        this.workerSpeedBias = request.getWorkerSpeedBias();
        this.normalWaitTime = request.getNormalWaitTime();
        this.normalServiceTime = request.getNormalServiceTime();
        this.bounceWaitTime = request.getBounceWaitTime();
    }

    public String getSimulationId() {
        return simulationId;
    }

    public Instant getSimulationStartTime() {
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

    public int getWorkerSkillBias() {
        return workerSkillBias;
    }

    public int getWorkerSpeedBias() {
        return workerSpeedBias;
    }

    public int getNormalWaitTime() {
        return normalWaitTime;
    }

    public int getNormalServiceTime() {
        return normalServiceTime;
    }

    public int getBounceWaitTime() {
        return bounceWaitTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
