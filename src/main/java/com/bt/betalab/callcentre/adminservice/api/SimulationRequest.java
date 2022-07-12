/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.api;

public class SimulationRequest {
    private int callDelay;
    private int difficultyBias;
    private int waitTimeBias;
    private int serviceTimeBias;
    private int understandingBias;
    private int workers;

    public int getCallDelay() {
        return callDelay;
    }

    public void setCallDelay(int callDelay) {
        this.callDelay = callDelay;
    }

    public int getDifficultyBias() {
        return difficultyBias;
    }

    public void setDifficultyBias(int difficultyBias) {
        this.difficultyBias = difficultyBias;
    }

    public int getWaitTimeBias() {
        return waitTimeBias;
    }

    public void setWaitTimeBias(int waitTimeBias) {
        this.waitTimeBias = waitTimeBias;
    }

    public int getServiceTimeBias() {
        return serviceTimeBias;
    }

    public void setServiceTimeBias(int serviceTimeBias) {
        this.serviceTimeBias = serviceTimeBias;
    }

    public int getUnderstandingBias() {
        return understandingBias;
    }

    public void setUnderstandingBias(int understandingBias) {
        this.understandingBias = understandingBias;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }
}
