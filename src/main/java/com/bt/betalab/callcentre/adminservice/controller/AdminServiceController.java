/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.controller;

import com.bt.betalab.callcentre.adminservice.api.CallResult;
import com.bt.betalab.callcentre.adminservice.api.SimulationDetails;
import com.bt.betalab.callcentre.adminservice.api.SimulationRequest;
import com.bt.betalab.callcentre.adminservice.api.SimulationStatus;
import com.bt.betalab.callcentre.adminservice.config.AdminServiceConfig;
import com.bt.betalab.callcentre.adminservice.exceptions.AdminServiceException;
import com.bt.betalab.callcentre.adminservice.model.Simulation;
import com.bt.betalab.callcentre.adminservice.service.ReportingService;
import com.bt.betalab.callcentre.adminservice.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AdminServiceController {

    @Autowired
    AdminServiceConfig config;

    @Autowired
    SimulationService simulationService;

    @Autowired
    ReportingService reportingService;

    @PostMapping(produces = "application/json", value = "api/v1/simulation/create")
    public ResponseEntity<SimulationDetails> createSimulation(@RequestBody SimulationRequest request)  {
        try {
            return ResponseEntity.ok(simulationService.createSimulation(request, config));
        } catch (AdminServiceException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(produces = "application/json", value = "api/v1/simulation/update")
    public ResponseEntity updateSimulation(@RequestBody SimulationRequest request)  {
        try {
            simulationService.updateSimulation(request, config);
            return ResponseEntity.ok().build();
        } catch (AdminServiceException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(produces = "application/json", value = "api/v1/simulation/start")
    public ResponseEntity startSimulation()  {
        try {
            simulationService.startSimulation(config);
            return ResponseEntity.ok().build();
        } catch (AdminServiceException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(produces = "application/json", value = "api/v1/simulation/pause")
    public ResponseEntity pauseSimulation()  {
        try {
            simulationService.pauseSimulation(config);
            return ResponseEntity.ok().build();
        } catch (AdminServiceException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(produces = "application/json", value = "api/v1/simulation/stop")
    public ResponseEntity stopSimulation()  {
        try {
            simulationService.stopSimulation(config);
            return ResponseEntity.ok().build();
        } catch (AdminServiceException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(produces = "application/json", value = "api/v1/simulation/status")
    public ResponseEntity<SimulationStatus> getSimulationStatus()  {
        try {
            return ResponseEntity.ok(simulationService.getSimulationStatus(config));
        } catch (AdminServiceException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(produces = "application/json", value = "api/v1/simulation/settings")
    public ResponseEntity<Simulation> getSimulationSettings()  {
        try {
            return ResponseEntity.ok(simulationService.getSimulationSettings());
        } catch (AdminServiceException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(produces = "application/json", value = "api/v1/report")
    public ResponseEntity<SimulationDetails> reportCallResult(@RequestBody CallResult request)  {
        try {
            reportingService.reportCallResult(request, config);
            return ResponseEntity.ok().build();
        } catch (AdminServiceException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
