package com.RMQ.MQActions;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.BasicConfigurator;

import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.StandardMetricsCollector;

public final class MQMonitorActionEngine implements ActionEngine {

	
	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();
		String CONNECTION_NAME = null,METRICS="metrics";
		StandardMetricsCollector metrics=null;
		
		 Connection connection;
		
		sampleResult.sampleStart();
		

		for(ActionParameter parameter:parameters) {

			switch(parameter.getName()) {
			
			case "connectionName":
				CONNECTION_NAME = parameter.getValue();
				break;
	
			
	
	}
	}

   BasicConfigurator.configure();
		
		try {
			
		    
			
			connection = (Connection)context.getCurrentVirtualUser().get(CONNECTION_NAME);
			if (connection!=null){
			metrics=(StandardMetricsCollector )context.getCurrentVirtualUser().get(METRICS);
			long publishedMessagesCount = metrics.getPublishedMessages().getCount();
			appendLineToStringBuilder(responseBuilder, " <Published Message count>" + publishedMessagesCount + "</Published Message count>");
		   // metrics.getRejectedMessages().getCount();
		    appendLineToStringBuilder(responseBuilder, " <Rejected Message count>" + metrics.getRejectedMessages().getCount() + "</Rejected Message count>");
		   // metrics.getConsumedMessages().getCount();
		    appendLineToStringBuilder(responseBuilder, " <Consumed  Message count>" + metrics.getConsumedMessages().getCount() + "</Consumed  Message count>");
		    //metrics.getAcknowledgedMessages().getCount();
		    appendLineToStringBuilder(responseBuilder, " <getAcknowledgedMessages>" + metrics.getAcknowledgedMessages().getCount()+ "</getAcknowledgedMessages>");
		    appendLineToStringBuilder(responseBuilder, " <getchannelcount>" +   metrics.getChannels().getCount()+ "</getchannelcount");
		   // metrics.getChannels().getCount();
		    
		   // System.out.println("published message"+publishedMessagesCount );
			}
		}
		catch (NullPointerException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			appendLineToStringBuilder(responseBuilder, e.getMessage());
			return getErrorResult(context, sampleResult, e.getMessage(), e);
		}

		sampleResult.setRequestContent(requestBuilder.toString());
		sampleResult.setResponseContent(responseBuilder.toString());	
		sampleResult.sampleEnd();

		
		return sampleResult;
	}

	private void appendLineToStringBuilder(final StringBuilder sb, final String line){
		sb.append(line).append("\n");
	}

	/**
	 * This method allows to easily create an error result and log exception.
	 */
	private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
		result.setError(true);
		result.setStatusCode("NL-MQMonitor_ERROR");
		result.setResponseContent(errorMessage);
		if(exception != null){
			context.getLogger().error(errorMessage, exception);
		} else{
			context.getLogger().error(errorMessage);
		}
		return result;
	}

	@Override
	public void stopExecute() {
		// TODO add code executed when the test have to stop.
	}

}
