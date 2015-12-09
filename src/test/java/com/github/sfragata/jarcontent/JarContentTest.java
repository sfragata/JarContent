package com.github.sfragata.jarcontent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.sfragata.jarcontent.JarContent;
import com.github.sfragata.jarcontent.config.JarContentConfig;

/**
 * Test Class
 * 
 * @author Fragata da Silva, Silvio
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JarContentConfig.class })
public class JarContentTest {

	@Autowired
	private JarContent jarContent;

	@Test
	public void shouldNotGetError() {
		jarContent.findJars(".", "parallel", true);
	}

}