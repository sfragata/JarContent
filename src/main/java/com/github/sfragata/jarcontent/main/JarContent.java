/**
 *
 */
package com.github.sfragata.jarcontent.main;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.github.sfragata.jarcontent.listener.EventListener;
import com.github.sfragata.jarcontent.to.JarContentTO;

/**
 * Class to searh class files into jar files
 *
 * @author Fragata da Silva, Silvio
 */
@Component
public class JarContent {

    private static final Logger LOGGER = LoggerFactory.getLogger(JarContent.class);

    @Autowired
    private EventListener eventListener;

    @Autowired
    private MessageSource messageSource;

    private List<Path> findAllJars(
        final String jarDirPath) {

        try {
            return Files.find(Paths.get(jarDirPath), 9999, (
                file,
                basicFileAttributes) -> {
                try {
                    return file.getFileName().toString().endsWith(".jar") && Files.size(file) > 0;
                } catch (final IOException e) {
                    return false;
                }
            }, FileVisitOption.FOLLOW_LINKS).parallel().collect(Collectors.toList());
        } catch (final IOException e) {
            return Collections.emptyList();
        }
    }

    public void findJars(
        final String jarDir,
        final String className,
        final boolean ignoreCase) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Searching...");
        }
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final List<Path> jars = findAllJars(jarDir);
        stopWatch.stop();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Found {} class(es) into directory {} in {} s.", jars.size(),
                Paths.get(jarDir).toAbsolutePath(), stopWatch.getLastTaskTimeMillis() / 1000.0);
        }
        this.eventListener.setCollectionLength(jars.size());
        stopWatch.start();
        int countFiles = 0;
        for (final Path file : jars) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Looking for class {} into {}", className, file.toString());
            }
            this.eventListener.setStatus(this.messageSource.getMessage("SEARCHING_IN_JAR",
                new Object[] { file.toString() }, Locale.getDefault()));
            try (JarFile jar = getURLContent(file.toAbsolutePath().toString());) {
                final Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    final String entry = entries.nextElement().getName();
                    if (getExtension(entry).equals(".class")) {
                        if (isEquals(className, ignoreCase, entry)) {
                            final JarContentTO fileSelected = new JarContentTO(file.toAbsolutePath().toString(), entry);

                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("File {}", fileSelected);
                            }
                            this.eventListener.addResult(fileSelected);
                            countFiles++;
                        }
                    }
                }
            } catch (final IOException e) {
                LOGGER.error("Error in file " + file.toAbsolutePath().toString(), e);
                this.eventListener.error(e);
            }
            this.eventListener.increaseProgress();
        }
        stopWatch.stop();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("# {} files found in {} s.", countFiles, stopWatch.getLastTaskTimeMillis() / 1000.0);

        }
        final StringBuilder msg = new StringBuilder();
        if (countFiles == 0) {
            msg.append(this.messageSource.getMessage("NOT_FOUND", null, Locale.getDefault()));
        } else {
            msg.append(this.messageSource.getMessage("FOUND", new Object[] { countFiles }, Locale.getDefault()));
        }
        this.eventListener.setStatus(msg.toString());
    }

    private boolean isEquals(
        final String pattern,
        final boolean ignoreCase,
        final String entryJarFile) {

        String entryJarFileString = entryJarFile.trim();
        String patternClass = pattern.trim();

        if (ignoreCase) {
            entryJarFileString = entryJarFileString.toLowerCase();
            patternClass = patternClass.toLowerCase();
        }
        entryJarFileString =
            entryJarFileString.substring(entryJarFileString.lastIndexOf(File.separator) == -1
                ? 0
                : entryJarFileString.lastIndexOf(File.separator));

        return entryJarFileString.indexOf(patternClass) != -1;
    }

    private String getExtension(
        final String file) {

        if (file.lastIndexOf(".") != -1) {
            return file.substring(file.lastIndexOf("."));
        }
        return "";
    }

    private JarFile getURLContent(
        final String jarFile)
        throws IOException {

        URL url = null;
        JarURLConnection urlCon = null;
        // local archive
        url = new URL("jar:file:///" + jarFile + "!/");
        // remote archive
        // url=new URL("jar:http://.../archive.jar!/");
        urlCon = (JarURLConnection) url.openConnection();
        return urlCon.getJarFile();
    }
}
