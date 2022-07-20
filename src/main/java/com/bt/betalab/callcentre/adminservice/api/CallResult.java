/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.api;

import java.sql.Timestamp;
import java.time.Instant;

public class CallResult {
    private boolean isEasy;
    private Customer customer;

    private Instant arrivalTime;
    private Instant pickupTime;
    private Instant closingTime;
    private boolean isBounced = false;
    private boolean isSolved = false;
    private WorkerDetails workerDetails;

    private String simulationId;
    private String simulationStartTime;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Instant getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Instant getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Instant pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Instant getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Instant closingTime) {
        this.closingTime = closingTime;
    }

    public boolean getIsEasy() {
        return isEasy;
    }

    public void setIsEasy(boolean easy) {
        isEasy = easy;
    }

    public boolean getIsBounced() {
        return isBounced;
    }

    public void setIsBounced(boolean bounced) {
        isBounced = bounced;
    }

    public boolean getIsSolved() {
        return isSolved;
    }

    public void setIsSolved(boolean solved) {
        isSolved = solved;
    }

    public String getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(String simulationId) {
        this.simulationId = simulationId;
    }

    public String getSimulationStartTime() {
        return simulationStartTime;
    }

    public void setSimulationStartTime(String simulationStartTime) {
        this.simulationStartTime = simulationStartTime;
    }

    public WorkerDetails getWorkerDetails() {
        return workerDetails;
    }

    public void setWorkerDetails(WorkerDetails workerDetails) {
        this.workerDetails = workerDetails;
    }
}
