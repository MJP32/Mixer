package org.mp.mixer.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobCoinClient {
	private static final Logger log = LoggerFactory.getLogger(new JobCoinClient().getClass().getName());
	public static void main(String[] args) throws Exception {
		log.info("JobCoinClient Started. ");
		Menu menu = new Menu();
		menu.mainMenu();
		System.exit(0);
	}
}
