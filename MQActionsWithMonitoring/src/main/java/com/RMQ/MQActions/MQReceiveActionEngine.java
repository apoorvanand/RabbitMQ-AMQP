package com.RMQ.MQActions;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.BasicConfigurator;

import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import com.rabbitmq.client.*;

public final class MQReceiveActionEngine implements ActionEngine {

	
	//public static String messages;

	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();
		String CONNECTION_NAME = null, qeuename = null;
		String message = null;
	
		Channel channel = null;
		 Connection connection;
		
		sampleResult.sampleStart();
		

		for(ActionParameter parameter:parameters) {

			switch(parameter.getName()) {
			
			case "connectionName":
				CONNECTION_NAME = parameter.getValue();
				break;
	
			case "Qeuename":
				qeuename = parameter.getValue();
				break;
			
	}
}

		//context.getLogger().debug("host="+qeuename+"port="+message);
		appendLineToStringBuilder(requestBuilder, "MQreceive request.");
		appendLineToStringBuilder(responseBuilder, "MQReceive response.");
		// TODO perform execution.

		BasicConfigurator.configure();
		
		try {
			connection = (Connection)context.getCurrentVirtualUser().get(CONNECTION_NAME);
			 channel = connection.createChannel();
		      appendLineToStringBuilder(responseBuilder, "Established channel: \n");
		      
		   //   channel.queueDeclare(qeuename, true, false, false, null);
		      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		      appendLineToStringBuilder(responseBuilder, "Receiving mesage from Q: " + qeuename);
		      
		     // consuming multipe messages in oneshot
		      /*
		      Consumer consumer = new DefaultConsumer(channel) {
		        @Override
		        public void  handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
		            throws IOException {
		          String message = new String(body, "UTF-8");
		        
		     
		         System.out.println(" [x] Received '" + message + "'");
		         MQReceiveActionEngine.messages=message;
		         System.out.println(" [x] Received messages '" + MQReceiveActionEngine.messages + "'");
		       
		        }
		      };
		      channel.basicConsume(qeuename, true, consumer);
		    */
		    
		    //consume one message at a time
		      
		      boolean autoAck = false;
		      GetResponse response = channel.basicGet(qeuename, autoAck);
		      if (response == null) {
		          // No message retrieved.
		      } else {
		          AMQP.BasicProperties props = response.getProps();
		          byte[] body = response.getBody();
		          message =  new String(body, "UTF-8");
		          long deliveryTag = response.getEnvelope().getDeliveryTag();
		          channel.basicAck(deliveryTag , false); // acknowledge receipt of the message
		        
		      }
		    
		    if (context.getLogger().isDebugEnabled()) {
				context.getLogger().debug("message Received from  Q");
				  
			}

		    channel.close();
		    appendLineToStringBuilder(responseBuilder, " [1] Message Received <message>" + message + "</message>");
		  //  appendLineToStringBuilder(responseBuilder, " [x] Received '" + MQReceiveActionEngine.messages + "'");
		  //  MQReceiveActionEngine.messages="";
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
		result.setStatusCode("NL-MQReceive_ERROR");
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
