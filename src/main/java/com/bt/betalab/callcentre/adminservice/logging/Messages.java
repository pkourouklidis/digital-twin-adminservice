/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 04/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.logging;

public class Messages {

        public static final String CONNECTINGTOMESSAGEQUEUEMESSAGE = "Attempting to connect to message queue at ";

        public static final String CONNECTEDTOMESSAGEQUEUEMESSAGE = "Connection to message queue established, awaiting JSON messages...";

        public static final String CHANNELERRORMESSAGEQUEUEMESSAGE = "Problem encountered creating channel to message queue: ";

        public static final String CONNECTIONERRORMESSAGEQUEUEMESSAGE = "Problem encountered connecting to message queue: ";

        public static final String OPERATIONSUCCESSMESSAGE = "Successfully performed request on specified gateway";

        public static final String OPERATIONFAILUREMESSAGE = "Error performing request on specified gateway";

        public static final String RETRIEVINGMESSAGEERRORMESSAGE = "Error occurred while retrieving message from RabbitMQ: ";

        public static final String ACKMESSAGEERRORMESSAGE = "Error occurred while acknowledging message receipt to RabbitMQ: ";

        public static final String HANDLEERRORMESSAGE = "Error occurred while handling call: ";

        public static final String INVALIDERRORMESSAGE = "Invalid call message received: ";

        public static final String KUBEAPIEXCEPTIONMESSAGE = "Failed to resolve API request with Kubernetes";

        public static final String INVALIDKUBERNETESCONFIGURATIONMESSAGE = "Invalid Kubernetes configuration provided, aborting API action";

}
