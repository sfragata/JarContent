package com.github.sfragata.jarcontent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.swixml.SwingEngine;

/**
 * Spring configuration (to replace the xml)
 *
 * @author Silvio Fragata Silva
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.github.sfragata.jarcontent.gui" })
@Import({ JarContentConfig.class })
public class JarContentConfigUI {

    @Bean
    public SwingEngine swixml() {

        return new SwingEngine();
    }

    @Bean
    public SwingEngine swixmlDialog() {

        return new SwingEngine();
    }

}
