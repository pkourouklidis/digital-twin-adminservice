/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.kubernetes;

import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.models.*;

import java.util.ArrayList;
import java.util.Arrays;

public class KubernetesBodyFactory {
    private static final String PROPAGATION_POLICY_BACKGROUND = "Background";
    public static V1Deployment produceDeploymentBody(String name, String imageName, String kubernetesNamesSpace, int replicas, String reportUrl) {
        V1Deployment returnObj = new V1Deployment();

        V1ObjectMeta metadata = new V1ObjectMeta();

        metadata.setName(name);
        metadata.setNamespace(kubernetesNamesSpace);

        returnObj.setMetadata(metadata);

        V1DeploymentSpec spec = new V1DeploymentSpec();
        spec.setReplicas(replicas);

        V1LabelSelector selector = new V1LabelSelector();

        spec.setSelector(selector);

        V1PodTemplateSpec template = new V1PodTemplateSpec();

        V1ObjectMeta podMetadata = new V1ObjectMeta();

        template.setMetadata(podMetadata);

        V1PodSpec podspec = new V1PodSpec();
        V1Container container = new V1Container();
        container.setName(name);
        container.image(imageName);
        container.imagePullPolicy("Always");

        ArrayList<V1EnvVar> env = new ArrayList<>();
        V1EnvVar envVar = new V1EnvVar();
        envVar.setName("REPORT_URL");
        envVar.setValue(reportUrl);
        env.add(envVar);
        container.setEnv(env);

        V1ContainerPort port = new V1ContainerPort();
        port.setName("http");
        port.setContainerPort(8080);
        port.setProtocol("TCP");
        ArrayList<V1ContainerPort> portList = new ArrayList<>();
        portList.add(port);
        container.setPorts(portList);

        V1Probe probe = new V1Probe();
        V1HTTPGetAction httpGet = new V1HTTPGetAction();
        httpGet.setPath("/_/health");
        httpGet.setPort(new IntOrString(8080));
        probe.setHttpGet(httpGet);
        probe.setInitialDelaySeconds(3);
        probe.setPeriodSeconds(3);
        container.setLivenessProbe(probe);

        ArrayList<V1Container> containerList = new ArrayList<>();
        containerList.add(container);
        podspec.setContainers(containerList);

        V1LocalObjectReference objRef = new V1LocalObjectReference();
        objRef.setName("worker-secret");
        podspec.setImagePullSecrets(Arrays.asList(objRef));

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
