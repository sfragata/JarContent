/**
 * 
 */
package br.com.sfragata.jarcontent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.swixml.SwingEngine;

/**
 * Spring configuration (to replace the xml)
 * 
 * @author Silvio Fragata Silva
 * 
 */
@Configuration
@ComponentScan(basePackages = { "br.com.sfragata" })
@EnableMBeanExport
public class JarContentConfig {

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasename("locale/messages");
		return source;
	}

	@Bean
	public SwingEngine swixml() {
		SwingEngine swixml = new SwingEngine();
		return swixml;
	}

	@Bean
	public SwingEngine swixmlDialog() {
		SwingEngine swixml = new SwingEngine();
		return swixml;
	}

}
