package by.dk.training.items.services;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LogTest.class);

	@Test
	public void logTest() {
		LOGGER.debug("debug message");
		LOGGER.info("info message");
		LOGGER.warn("warn message");
		LOGGER.error("error message");

	}
}
