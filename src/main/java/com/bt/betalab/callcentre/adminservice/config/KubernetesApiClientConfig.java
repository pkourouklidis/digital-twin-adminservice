/**
 * @author Adam Ziolkowski (612330595), BetaLab, Applied Research
 * Date: 22/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/

package com.bt.betalab.callcentre.adminservice.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;

@Configuration
public class KubernetesApiClientConfig {

    Logger logger = LoggerFactory.getLogger(KubernetesApiClientConfig.class);

    @Bean
    public ApiClient getKubernetesApiClient() throws IOException {

        try {
            logger.info("Attempting to create kubernetes api client using in cluster strategy");
            ApiClient client = ClientBuilder.cluster().build();
            return client;
        } catch (Exception e){
            logger.info("Failed to create kubernetes api client using in cluster strategy");
        }

        try {
            logger.info("Attempting to create kubernetes api client using kubeconfig file");
            String kubeConfigPath = System.getProperty("user.home") + "/.kube/config";
            ApiClient client =
                    ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
            return client;
        } catch (Exception e){
            logger.info("Failed to create kubernetes api client using kubeconfig file");
        }

        logger.info("Continuing with default kubernetes api client");
        return ClientBuilder.defaultClient();

    }

}
