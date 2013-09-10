package br.com.sfragata.jarcontent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test Class
 * 
 * @author Fragata da Silva, Silvio
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class JarContentTest {

	@Autowired
	private JarContent jarContent;

	@Test
	public void shouldNotGetError() {
		jarContent.findJars(".", "parallel", true);
	}

}