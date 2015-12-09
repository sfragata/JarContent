/**
 * 
 */
package com.github.sfragata.jarcontent.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.github.sfragata.jarcontent.config.JarContentConfig;

/**
 * Main class to start the application
 * 
 * @author Fragata da Silva, Silvio
 */
public class JarContentLauncher {

	private static final Log logger = LogFactory
			.getLog(JarContentLauncher.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread t = new Thread(new Runnable() {
			@SuppressWarnings("resource")
			@Override
			public void run() {
				try {
					if (logger.isInfoEnabled()) {
						logger.info("Starting application...");
					}
					new AnnotationConfigApplicationContext(
							JarContentConfig.class);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		});
		t.setName("JarContentLauncher");
		t.start();
	}
}
