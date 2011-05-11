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

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.transport.PropertyScope;

public class SplitAndSetFieldsProcessor implements MessageProcessor
{

    private static String[] fields = new String[] {"FirstName", "LastName", "Email", "Title", "Phone"};
    
    public MuleEvent process(MuleEvent event) throws MuleException
    {
        String[] values = event.getMessageAsString().split(",");
        for (int i = 0; i < values.length; i++)
        {
            String value = values[i];
            event.getMessage().setProperty(fields[i], value, PropertyScope.OUTBOUND);
        }
        
        return event;
    }

}


