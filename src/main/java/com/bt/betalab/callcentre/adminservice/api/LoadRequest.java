/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.api;

import com.bt.betalab.callcentre.adminservice.model.Simulation;

import java.time.Instant;

public class LoadRequest {
    private boolean on;
    private int callDelay;

    private int difficultyBias;

    private int waitTimeBias;

    private int serviceTimeBias;

    private int understandingBias;

    private String simulationId;

    private Instant simulationStartTime;

    public LoadRequest(boolean start, Simulation simulation) {
        this.on = start;
        this.callDelay = simulation.getCallDelay();
        this.difficultyBias = simulation.getDifficultyBias();
        this.waitTimeBias = simulation.getWaitTimeBias();
        this.serviceTimeBias = simulation.getServiceTimeBias();
        this.understandingBias = simulation.getUnderstandingBias();
        this.simulationId = simulation.getSimulationId();
        this.simulationStartTime     = simulation.getSimulationStartTime();
    }

    public boolean isOn() {
        return on;
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

    public String getSimulationId() {
        return simulationId;
    }

    public Instant getSimulationStartTime() {
        return simulationStartTime;
    }
}
