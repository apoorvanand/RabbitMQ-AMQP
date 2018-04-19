package com.RMQ.MQActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.google.common.base.Optional;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

public final class MQSendAction implements Action{
	private static final String BUNDLE_NAME = "com.RMQ.MQActions.sendbundle";
	private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayName");
	private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");

	
static enum RMQParameter {
		
		CONNECTION_NAME("connectionName", "the name of the connection to map with other advanced actions", true, true),
		EXCHANGECHANNELNAME("Excahngechannelname", "name of exhange cahnnel for binding queue", true, true),
		EXCHANGECHANNELTYPE("Excahngechanneltype", "type of exhange cahnnel: direct/fanout", true, true),
		QEUENAME("Qeuename", "Name of Qeue", false, true),
		ROUTINGKEY("Routingkey", "key for routing message to queue", true, true),
		MESSAGE("Message", "Message to send to the queue", true, true);
		//contentFile_parse("contentFile_parse", "Whether to parse the file to replace variables. Possible values are Y/N , Default value= N", false, false);
		
		private final String name;
		private final String description;
		private final boolean required;
		/** Whether or not the parameter is included in the description (whether the user can see it or not). */
		private final boolean visible;

		/**
		 * @param name
		 * @param description
		 * @param required
		 * @param visible Whether or not the parameter is included in the description (whether the user can see it or not). 
		 */
		RMQParameter(String name, String description, boolean required, final boolean visible) {
			this.name = name;
			this.description = description;
			this.required = required;
			this.visible = visible;
		}
		private String getRequiredOptionalString() {
			return required ? "(required) " : "(optional) ";
		}

		public String getFullDescription() {
			return "- " + name + ": " + getRequiredOptionalString() + description + ".";
			
		}

		public String getName() {
			return name;
		}
		
		public boolean isVisible() {
			return visible;
		}

	}

	@Override
	public String getType() {
		return "MQSend";
	}

	@Override
	public List<ActionParameter> getDefaultActionParameters() {
		final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
		// TODO Add default parameters.
		parameters.add(new ActionParameter(RMQParameter.CONNECTION_NAME.getName(), "myConnection"));
		parameters.add(new ActionParameter(RMQParameter.EXCHANGECHANNELNAME.getName(), "channelname"));
		parameters.add(new ActionParameter(RMQParameter.EXCHANGECHANNELTYPE.getName(), "direct"));
		parameters.add(new ActionParameter(RMQParameter.QEUENAME.getName(), "Queue Name"));
		parameters.add(new ActionParameter(RMQParameter.ROUTINGKEY.getName(), "Routing key"));
		parameters.add(new ActionParameter(RMQParameter.MESSAGE.getName(), "Hello Rabbit MQ!"));
		//parameters.add(new ActionParameter(RMQParameter.contentFile_parse.getName(), "N"));
		return parameters;
	}

	@Override
	public Class<? extends ActionEngine> getEngineClass() {
		return MQSendActionEngine.class;
	}

	@Override
	public Icon getIcon() {
		// TODO Add an icon
		return null;
	}

	@Override
	public boolean getDefaultIsHit(){
		return false;
	}

	@Override
	public String getDescription() {
		final StringBuilder description = new StringBuilder();
		// TODO Add description
		description.append("MQSend description.\n");
		
		for (final RMQParameter parameter : RMQParameter.values()) {
			//if (parameter.isVisible()) {
				description.append(parameter.getFullDescription()).append("\n");
			//}
		}

		return description.toString();

	}

	@Override
	public String getDisplayName() {
		return DISPLAY_NAME;
	}

	@Override
	public String getDisplayPath() {
		return DISPLAY_PATH;
	}

	@Override
	public Optional<String> getMinimumNeoLoadVersion() {
		return Optional.absent();
	}

	@Override
	public Optional<String> getMaximumNeoLoadVersion() {
		return Optional.absent();
	}
}
