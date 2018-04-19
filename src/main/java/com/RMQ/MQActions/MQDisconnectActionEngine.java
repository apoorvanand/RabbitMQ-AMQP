package com.RMQ.MQActions;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.StandardMetricsCollector;

public final class MQDisconnectActionEngine implements ActionEngine {

	
	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();
		String CONNECTION_NAME = null;
	   // Connection connection ;
		
		sampleResult.sampleStart();
		

		for(ActionParameter parameter:parameters) {

			switch(parameter.getName()) {
			
			case "connectionName":
				CONNECTION_NAME = parameter.getValue();
				break;
	
			
	
	}
}

		appendLineToStringBuilder(requestBuilder, "MQDisconnect request.");
		appendLineToStringBuilder(responseBuilder, "MQDisconnect response.");
		// TODO perform execution.


	    
	
		try {
		
			Connection connection = (Connection)context.getCurrentVirtualUser().get(CONNECTION_NAME);
			connection.close();
		 
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			appendLineToStringBuilder(responseBuilder, e.getMessage());
			return getErrorResult(context, sampleResult, e.getMessage(), e);
		}

		// Put Connection in the context of the VU, so it could be retrieved further
					if (context.getLogger().isDebugEnabled()) {
						
						appendLineToStringBuilder(responseBuilder,"disconencted from  RabbitQ: ");
					}
					
		sampleResult.setRequestContent(requestBuilder.toString());
		sampleResult.setResponseContent(responseBuilder.toString());
		appendLineToStringBuilder(responseBuilder, "disconencted from  RabbitQ: "); 		
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
		result.setStatusCode("NL-MQDisconnect_ERROR");
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
