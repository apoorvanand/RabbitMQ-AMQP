package com.RMQ.MQActions;


import java.util.List;

import org.apache.log4j.BasicConfigurator;

import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import com.rabbitmq.client.Connection;
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
			appendLineToStringBuilder(responseBuilder, " <publishedMessages>" + publishedMessagesCount + "</publishedMessages>");
		   // metrics.getRejectedMessages().getCount();
		    appendLineToStringBuilder(responseBuilder, " <rejectedMessages>" + metrics.getRejectedMessages().getCount() + "</rejectedMessages>");
		   // metrics.getConsumedMessages().getCount();
		    appendLineToStringBuilder(responseBuilder, " <consumedMessages>" + metrics.getConsumedMessages().getCount() + "</consumedMessages>");
		    //metrics.getAcknowledgedMessages().getCount();
		    appendLineToStringBuilder(responseBuilder, " <acknowledgedMessages>" + metrics.getAcknowledgedMessages().getCount()+ "</acknowledgedMessages>");
		    appendLineToStringBuilder(responseBuilder, " <channels>" +   metrics.getChannels().getCount()+ "</channels");
		   // metrics.getChannels().getCount();
		    
		   // System.out.println("published message"+publishedMessagesCount );
			}
		}
		catch (NullPointerException e) {
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
	}

}
