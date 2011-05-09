/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.activiti;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.api.transport.PropertyScope;
import org.mule.module.activiti.action.model.ProcessInstance;
import org.mule.tck.FunctionalTestCase;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.test.TestHelper;
import org.activiti.engine.test.Deployment;

public class ActivitiJMSTestCase extends FunctionalTestCase
{
    @Override
    protected String getConfigResources()
    {
        return "mule-config.xml";
    }

    @Deployment
    public void testJMSRead() throws Exception {
        ProcessEngine processEngine = muleContext.getRegistry().get("processEngine");
        RuntimeService runtimeService = muleContext.getRegistry().get("runtimeService");
        TestHelper.annotationDeploymentSetUp(processEngine, getClass(), getName());
        
        ProcessInstance instance = this.createProcess();
        Thread.sleep(2000);
        this.sendMessageToJMSWithResponse(instance);
        
        assertNotNull(runtimeService.getVariables(instance.getId()));
    }

    private void sendMessageToJMSWithResponse(ProcessInstance instance) throws MuleException
    {
        MuleClient client = muleContext.getClient();
        Map parameterMap = new HashMap();
        parameterMap.put("username", "esteban.robles@mulesoft.com");
        
        MuleMessage responseMessage = client.send("jms://approveQueue", instance, parameterMap);
    }

    private ProcessInstance createProcess() throws MuleException
    {
        MuleClient client = muleContext.getClient();
        DefaultMuleMessage message = new DefaultMuleMessage("", muleContext);
        Map parameterMap = new HashMap();
        parameterMap.put("processDefinitionKey", "processOrder");
        
        message.setProperty("createProcessParameters", parameterMap , PropertyScope.OUTBOUND);
        
        MuleMessage responseMessage = client.send("vm://in", message);
        return (ProcessInstance) responseMessage.getPayload();
    }
}


