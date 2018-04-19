package com.RMQ.MQActions;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public final class MQSendActionEngine implements ActionEngine {

	
	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();
		String CONNECTION_NAME = null, qeuename = null,message = null,exchangechannelname=null,channeltype=null,routingkey=null,contentFile_parse=null,parsedstring=null;
		Channel channel = null;
		 Connection connection;
		
		sampleResult.sampleStart();
		

		for(ActionParameter parameter:parameters) {

			switch(parameter.getName()) {
			
			case "connectionName":
				CONNECTION_NAME = parameter.getValue();
				break;
	
			case "Excahngechannelname":
				exchangechannelname = parameter.getValue();
				break;
			case "Excahngechanneltype":
				channeltype = parameter.getValue();
				break;
			case "Qeuename":
				qeuename = parameter.getValue();
				break;
				
			case "Routingkey":
				routingkey = parameter.getValue();
				break;
			case "Message":
				message = parameter.getValue();
				break;
			case "contentFile_parse":
				contentFile_parse = parameter.getValue();
				break;
			
	}
}

		context.getLogger().debug("host="+qeuename+" message="+message);
				
		appendLineToStringBuilder(requestBuilder, "MQSend request.");
		appendLineToStringBuilder(responseBuilder, "MQSend response.");
		// TODO perform execution.

		
		try {
			//parsing message
			/*if (contentFile_parse=="Y")  {
				parsedstring=Parser.parse(context, message);
			}
			else{
				parsedstring=message;
			}*/
			parsedstring=message;
			connection = (Connection)context.getCurrentVirtualUser().get(CONNECTION_NAME);
			 channel = connection.createChannel();
			  appendLineToStringBuilder(responseBuilder, "Established channel: \n");
			 // commending below lines to support nonadmin users
			  /*channel.exchangeDeclare(exchangechannelname, channeltype, true);
			  channel.queueDeclare(qeuename, true, false, false, null);
			  channel.queueBind(qeuename, exchangechannelname, routingkey);
			  */
			  appendLineToStringBuilder(responseBuilder, "Binded queue to channel: \n");
				 AMQP.BasicProperties.Builder target = new AMQP.BasicProperties.Builder();
		         target.priority(0);
		         target.contentType("text/plain");
		         target.contentEncoding("UTF-8");
		         
		         channel.basicPublish(exchangechannelname, routingkey, target.build(), parsedstring.getBytes());
			    System.out.println(" [x] Sent '" + message + "'");
		    
		    if (context.getLogger().isDebugEnabled()) {
				context.getLogger().debug("message send to Q");
				
			}
		    appendLineToStringBuilder(responseBuilder, "Sending mesage:"+parsedstring + "to Q: " + qeuename+"\n");
		    channel.close();

		} catch (IOException | NullPointerException | TimeoutException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			appendLineToStringBuilder(responseBuilder, e.getMessage());
			return getErrorResult(context, sampleResult, e.getMessage(), e);
		}

		// Put Connection in the context of the VU, so it could be retrieved further
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
		result.setStatusCode("NL-MQSend_ERROR");
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
