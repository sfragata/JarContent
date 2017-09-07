/**
 *
 */
package com.github.sfragata.jarcontent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Spring configuration (to replace the xml)
 *
 * @author Silvio Fragata Silva
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.github.sfragata.jarcontent.main", "br.com.sfragata.log4jmanager" })
@EnableMBeanExport
public class JarContentConfig {

    @Bean
    public ResourceBundleMessageSource messageSource() {

        final ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("locale/messages");
        return source;
    }

}
