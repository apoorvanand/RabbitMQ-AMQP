package com.RMQ.MQActions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MQConnectActionTest {
	@Test
	public void shouldReturnType() {
		final MQConnectAction action = new MQConnectAction();
		assertEquals("MQConnect", action.getType());
	}

}
