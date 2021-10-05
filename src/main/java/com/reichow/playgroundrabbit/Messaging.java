package com.reichow.playgroundrabbit;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
public class Messaging {

	private static final Logger logger = LoggerFactory.getLogger(Messaging.class);

	static class Source {

		@Bean
		public Supplier<String> send() {
			return () -> "Time: " + LocalDateTime.now();
		}
	}

	static class Sink {

		@Bean
		public Consumer<GenericMessage<String>> receive() {
			return s -> logger.info("### Received message: {}", s);
		}
	}
}
