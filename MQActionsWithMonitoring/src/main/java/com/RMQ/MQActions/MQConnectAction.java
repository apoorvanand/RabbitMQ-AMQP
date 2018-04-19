package com.RMQ.MQActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.RMQ.MQActions.MQSendAction.RMQParameter;
import com.google.common.base.Optional;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

public final class MQConnectAction implements Action{
	private static final String BUNDLE_NAME = "com.RMQ.MQActions.bundle";
	private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayName");
	private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");

	
static enum RMQParameter {
		
		CONNECTION_NAME("connectionName", "the name of the connection to map with other advanced actions", true, true),
		HOSTNAME("Hostname", "Rabbit MQ server name/ip", true, true),
		PORT("Port", "port used for send & receive message .Default value= 5672", true, true),
		VIRTUALHOST("Virtualhost", "Virtualhost ,Default value=", true, true),
		USERNAME("Username", "Username to connect Rabbit MQ server ", true, true),
		PASSWORD("Password", "Password to connect Rabbit MQ server ", true, true);
		
		
		
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
			return "- " + name + ": " + getRequiredOptionalString() + description + ".";		}

		public String getName() {
			return name;
		}
		
		public boolean isVisible() {
			return visible;
		}

	}

	@Override
	public String getType() {
		return "MQConnect";
	}

	@Override
	public List<ActionParameter> getDefaultActionParameters() {
		final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
		// TODO Add default parameters.
		parameters.add(new ActionParameter(RMQParameter.CONNECTION_NAME.getName(), "myConnection"));
		parameters.add(new ActionParameter(RMQParameter.HOSTNAME.getName(), "127.0.0.1"));
		parameters.add(new ActionParameter(RMQParameter.PORT.getName(), "5672"));
		parameters.add(new ActionParameter(RMQParameter.VIRTUALHOST.getName(), "/"));
		parameters.add(new ActionParameter(RMQParameter.USERNAME.getName(), "Username"));
		parameters.add(new ActionParameter(RMQParameter.PASSWORD.getName(), "password"));
		return parameters;
	}

	@Override
	public Class<? extends ActionEngine> getEngineClass() {
		return MQConnectActionEngine.class;
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
		description.append("MQConnect description.\n");
		for (final RMQParameter parameter : RMQParameter.values()) {
			if (parameter.isVisible()) {
				description.append(parameter.getFullDescription()).append("\n");
			}
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
