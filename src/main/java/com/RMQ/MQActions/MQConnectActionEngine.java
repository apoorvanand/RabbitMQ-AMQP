package com.RMQ.MQActions;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.StandardMetricsCollector;

public final class MQConnectActionEngine implements ActionEngine {

	
	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();
		String host = null,port = null,virtualhost=null,username = null,password = null,CONNECTION_NAME = null,METRICS="metrics";
		Channel channel = null;
		 Connection connection;
		 StandardMetricsCollector metrics;
		
		sampleResult.sampleStart();
		

		for(ActionParameter parameter:parameters) {

			switch(parameter.getName()) {
			
			case "connectionName":
				CONNECTION_NAME = parameter.getValue();
				break;
	
			case "Hostname":
				host = parameter.getValue();
				break;
			case "Port":
				port = parameter.getValue();
				break;
			case "Virtualhost":
				virtualhost = parameter.getValue();
				break;	
			case "Username":
				username = parameter.getValue();
				break;
			case "Password":
				password = parameter.getValue();
				break;
	
	}
}

		context.getLogger().debug("host="+host+"port="+port+"user="+username+ "pass="+password);
		appendLineToStringBuilder(requestBuilder, "MQConnect request.");
		appendLineToStringBuilder(responseBuilder, "MQConnect response.");
		// TODO perform execution.

		
		try {
			connection = (Connection)context.getCurrentVirtualUser().get(CONNECTION_NAME);
			if (connection==null){
			ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost(host);
		     metrics = new StandardMetricsCollector();
			factory.setMetricsCollector(metrics);
		    factory.setUsername(username);
		    factory.setPassword(password);
		    factory.setVirtualHost(virtualhost);
		   // factory.setHost("127.0.0.1");
		    factory.setPort(Integer.parseInt(port));
		   
			connection = factory.newConnection();
			// Put Connection in the context of the VU, so it could be retrieved further
		    context.getCurrentVirtualUser().put(CONNECTION_NAME, connection);
		    context.getCurrentVirtualUser().put(METRICS, metrics);
			}
		   // channel = connection.createChannel();
		    
		    if (context.getLogger().isDebugEnabled()) {
				context.getLogger().debug("Put Connection in the context of the VU");
				
			}
			

//context.getCurrentVirtualUser().put("CHANNEL", channel);
    appendLineToStringBuilder(responseBuilder, "Connected to RabbitQ Server: " + host);

		} catch (IOException | TimeoutException | NullPointerException e) {
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
		result.setStatusCode("NL-MQConnect_ERROR");
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
