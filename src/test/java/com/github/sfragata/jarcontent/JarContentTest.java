package com.github.sfragata.jarcontent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.sfragata.jarcontent.config.JarContentConfig;

/**
 * Test Class
 *
 * @author Fragata da Silva, Silvio
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JarContentConfig.class })
@ActiveProfiles("dev")
public class JarContentTest {

    @Autowired
    private JarContent jarContent;

    @Test
    public void shouldNotGetError() {

        this.jarContent.findJars(".", "parallel", true);
    }

    // @Test
    // public void nioFileTest1() throws IOException {
    // Files.find(Paths.get("/home/ssilva/.m2/repository"), 99, new
    // BiPredicate<Path,BasicFileAttributes>(){
    // @Override
    // public boolean test(Path t, BasicFileAttributes u) {
    // return t.getFileName().toString().endsWith(".jar");
    // }}, FileVisitOption.FOLLOW_LINKS).forEach((action) -> {
    // System.out.println(action);
    // });
    // }
}
