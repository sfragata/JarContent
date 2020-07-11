package com.github.sfragata.jarcontent.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.github.sfragata.jarcontent.config.JarContentConfigUI;

/**
 * Main class to start the application
 *
 * @author Fragata da Silva, Silvio
 */
@SuppressWarnings("JavaDoc")
public class JarContentLauncher {

    private static final Logger logger = LoggerFactory.getLogger(JarContentLauncher.class);

    /**
     * @param args
     */
    public static void main(
        final String[] args) {

        final Thread t = new Thread(() -> {

            try {
                if (logger.isInfoEnabled()) {
                    logger.info("Starting application...");
                }
                new AnnotationConfigApplicationContext(JarContentConfigUI.class);
            } catch (final Exception e) {
                logger.error("Error", e);
            }
        });
        t.setName("JarContentLauncher");
        t.start();
    }
}
