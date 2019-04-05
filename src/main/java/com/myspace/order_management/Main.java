package com.myspace.order_management;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.credentials.EnteredCredentialsProvider;

/**
 * Example KIE-Server Client implementation that starts an 'Order Management' process instance. 
 * 
 * @author <a href="mailto:duncan.doyle@redhat.com">Duncan Doyle</a>
 */
public class Main {
    
    private static final String KIE_SERVER_URL = "http://localhost:8080/kie-server/services/rest/server";
    private static final String CONTAINER_ID = "order-management";
    private static final String USERNAME = "pamAdmin";
    private static final String PASSWORD = "redhatpam1!";
    private static final String PROCESS_ID = "order-management.order-management";
    
    public static void main(String[] args) {
        
        KieServicesConfiguration kieServicesConfig = KieServicesFactory.newRestConfiguration(KIE_SERVER_URL, new EnteredCredentialsProvider(USERNAME, PASSWORD));
        
        Set<Class<?>> extraClasses = new HashSet<>();
        extraClasses.add(OrderInfo.class);
        extraClasses.add(SupplierInfo.class);
        kieServicesConfig.addExtraClasses(extraClasses);
        
        KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(kieServicesConfig);
        ProcessServicesClient processServicesClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
        
        Map<String, Object> inputData = new HashMap<>();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setItem("Phone Huawei P10");
        orderInfo.setUrgency("low");
        inputData.put("orderInfo", orderInfo);
        
        Long processInstanceId = processServicesClient.startProcess(CONTAINER_ID, PROCESS_ID, inputData);
        
        System.out.println("New 'Order Management' process instance started with instance-id: " + processInstanceId);
    }

}
