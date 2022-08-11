/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.kubernetes;

import com.bt.betalab.callcentre.adminservice.config.AdminServiceConfig;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KubernetesBodyFactory {
    private static final String PROPAGATION_POLICY_BACKGROUND = "Background";
    public static V1Deployment produceDeploymentBody(String name, String imageName, String kubernetesNamesSpace, int replicas, String reportUrl, String reportUrlUser, String reportUrlPassword, int skillBias, int speedBias, int normalWaitTime, int normalServiceTime, int bounceWaitTime, AdminServiceConfig config) {
        V1Deployment returnObj = new V1Deployment();

        V1ObjectMeta metadata = new V1ObjectMeta();

        metadata.setName(name);
        metadata.setNamespace(kubernetesNamesSpace);

        returnObj.setMetadata(metadata);

        V1DeploymentSpec spec = new V1DeploymentSpec();
        spec.setReplicas(replicas);

        V1LabelSelector selector = new V1LabelSelector();

        Map<String,String> matchLabels = new HashMap<>();
        matchLabels.put("call-centre-component", "worker");
        selector.setMatchLabels(matchLabels);

        spec.setSelector(selector);

        V1PodTemplateSpec template = new V1PodTemplateSpec();

        V1ObjectMeta podMetadata = new V1ObjectMeta();

        podMetadata.setLabels(matchLabels);

        template.setMetadata(podMetadata);

        V1PodSpec podspec = new V1PodSpec();
        V1Container container = new V1Container();
        container.setName(name);
        container.image(imageName);
        container.imagePullPolicy("Always");

        ArrayList<V1EnvVar> env = new ArrayList<>();

        V1EnvVar urlEnvVar = new V1EnvVar();
        urlEnvVar.setName("REPORT_URL");
        urlEnvVar.setValue(reportUrl);
        env.add(urlEnvVar);

        V1EnvVar urlUserEnvVar = new V1EnvVar();
        urlUserEnvVar.setName("REPORT_URL_USER");
        urlUserEnvVar.setValue(reportUrlUser);
        env.add(urlUserEnvVar);

        V1EnvVar urlPasswordEnvVar = new V1EnvVar();
        urlPasswordEnvVar.setName("REPORT_URL_PASSWORD");
        urlPasswordEnvVar.setValue(reportUrlPassword);
        env.add(urlPasswordEnvVar);

        V1EnvVar skillEnvVar = new V1EnvVar();
        skillEnvVar.setName("SKILL_BIAS");
        skillEnvVar.setValue(Integer.valueOf(skillBias).toString());
        env.add(skillEnvVar);

        V1EnvVar speedEnvVar = new V1EnvVar();
        speedEnvVar.setName("SPEED_BIAS");
        speedEnvVar.setValue(Integer.valueOf(speedBias).toString());
        env.add(speedEnvVar);

        V1EnvVar normalWaitTimeEnvVar = new V1EnvVar();
        normalWaitTimeEnvVar.setName("NORMAL_WAIT_TIME");
        normalWaitTimeEnvVar.setValue(Integer.valueOf(normalWaitTime).toString());
        env.add(normalWaitTimeEnvVar);

        V1EnvVar normalServiceTimeEnvVar = new V1EnvVar();
        normalServiceTimeEnvVar.setName("NORMAL_SERVICE_TIME");
        normalServiceTimeEnvVar.setValue(Integer.valueOf(normalServiceTime).toString());
        env.add(normalServiceTimeEnvVar);

        V1EnvVar bounceWaitTimeEnvVar = new V1EnvVar();
        bounceWaitTimeEnvVar.setName("BOUNCE_WAIT_TIME");
        bounceWaitTimeEnvVar.setValue(Integer.valueOf(bounceWaitTime).toString());
        env.add(bounceWaitTimeEnvVar);

        V1EnvVar queueAddressEnvVar = new V1EnvVar();
        queueAddressEnvVar.setName("QUEUE_ADDRESS");
        queueAddressEnvVar.setValue(config.getQueueAddress());
        env.add(queueAddressEnvVar);

        V1EnvVar queuePortEnvVar = new V1EnvVar();
        queuePortEnvVar.setName("QUEUE_PORT");
        queuePortEnvVar.setValue(Integer.valueOf(config.getQueuePort()).toString());
        env.add(queuePortEnvVar);

        V1EnvVar queueUserEnvVar = new V1EnvVar();
        queueUserEnvVar.setName("QUEUE_USER");
        queueUserEnvVar.setValue(config.getQueueUser());
        env.add(queueUserEnvVar);

        V1EnvVar queuePasswordEnvVar = new V1EnvVar();
        queuePasswordEnvVar.setName("QUEUE_PASSWORD");
        queuePasswordEnvVar.setValue(config.getQueuePassword());
        env.add(queuePasswordEnvVar);

        V1EnvVar queueNameEnvVar = new V1EnvVar();
        queueNameEnvVar.setName("QUEUE_NAME");
        queueNameEnvVar.setValue(config.getQueueName());
        env.add(queueNameEnvVar);

        container.setEnv(env);

        ArrayList<V1Container> containerList = new ArrayList<>();
        containerList.add(container);
        podspec.setContainers(containerList);

        template.setSpec(podspec);
        spec.setTemplate(template);
        returnObj.setSpec(spec);

        return returnObj;
    }

    public static V1DeleteOptions produceDeleteOptionsBody() {
        return new V1DeleteOptionsBuilder()
                .withGracePeriodSeconds(0L)
                .withPropagationPolicy(PROPAGATION_POLICY_BACKGROUND)
                .build();
    }

    public static V1Patch producePatchBody(int replicas) {
        String jsonPatchStr = "[{\"op\":\"replace\",\"path\":\"/spec/template/spec/terminationGracePeriodSeconds\",\"value\":" + replicas + "}]";
        return new V1Patch(jsonPatchStr);
    }
}
