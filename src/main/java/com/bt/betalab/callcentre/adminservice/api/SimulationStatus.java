/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 18/08/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.api;

import java.time.Instant;

public class SimulationStatus {
    private int queueDepth = 0;
    private int activeWorkers = 0;
    private String status = "stopped";
    private Instant simulationStartTime;
    private String simulationId;

    public int getQueueDepth() {
        return queueDepth;
    }

    public void setQueueDepth(int queueDepth) {
        this.queueDepth = queueDepth;
    }

    public int getActiveWorkers() {
        return activeWorkers;
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

    public Instant getSimulationStartTime() {
        return simulationStartTime;
    }

    public void setSimulationStartTime(Instant simulationStartTime) {
        this.simulationStartTime = simulationStartTime;
    }

    public String getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(String simulationId) {
        this.simulationId = simulationId;
    }
}
