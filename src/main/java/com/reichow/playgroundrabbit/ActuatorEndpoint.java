package com.reichow.playgroundrabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "clear-cache")
public class ActuatorEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActuatorEndpoint.class);

	@Autowired
	private CachingConnectionFactory cachingConnectionFactory;

	@WriteOperation
	public void clearCache() {
		LOGGER.warn("### Resetting connections from cache!");
		cachingConnectionFactory.resetConnection();
	}

	public void resetCache() {
		LOGGER.warn("### Resetting connections from cache!");
		cachingConnectionFactory.resetConnection();
	}
}
