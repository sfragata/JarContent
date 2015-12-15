/**
 * 
 */
package com.github.sfragata.jarcontent;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	private static Log logger = LogFactory.getLog(JarContent.class);
	@Autowired
	private EventListener eventListener;

	@Autowired
	MessageSource messageSource;

	private List<Path> findAllJars(String jarDirPath) {

		try {
			return Files.find(Paths.get(jarDirPath), 9999, new BiPredicate<Path, BasicFileAttributes>() {
				@Override
				public boolean test(Path t, BasicFileAttributes u) {
					try {
						return t.getFileName().toString().endsWith(".jar") && Files.size(t) > 0;
					} catch (IOException e) {
						return false;
					}
				}
			}, FileVisitOption.FOLLOW_LINKS).parallel().collect(Collectors.toList());
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}

	public void findJars(final String jarDir, final String className, final boolean ignoreCase) {

		if (logger.isDebugEnabled()) {
			logger.debug("Searching...");
		}
		final StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<Path> jars = findAllJars(jarDir);
		stopWatch.stop();
		if (logger.isInfoEnabled()) {
			logger.info(new StringBuilder("Found ").append(jars.size()).append(" class in directory ").append(jarDir)
					.append(" in ").append(stopWatch.getLastTaskTimeMillis() / 1000.0).append(" s."));
		}
		eventListener.setCollectionLength(jars.size());
		stopWatch.start();
		int countFiles = 0;
		for (Path file : jars) {
			if (logger.isDebugEnabled()) {
				logger.debug(new StringBuilder("Looking for class ").append(className).append(" into ")
						.append(file.toString()));
			}
			eventListener.setStatus(messageSource.getMessage("SEARCHING_IN_JAR", new Object[] { file.toString() },
					Locale.getDefault()));
			try (JarFile jar = getURLContent(file.toAbsolutePath().toString());) {
				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					String entry = entries.nextElement().getName();
					if (getExtension(entry).equals(".class")) {
						if (isEquals(className, ignoreCase, entry)) {
							JarContentTO fileSelected = new JarContentTO(file.toAbsolutePath().toString(), entry);

							if (logger.isDebugEnabled()) {
								logger.debug(new StringBuilder("File: ").append(fileSelected));
							}
							eventListener.addResult(fileSelected);
							countFiles++;
						}
					}
				}
			} catch (IOException e) {
				logger.error("Error in file " + file.toAbsolutePath().toString(), e);
				eventListener.error(e);
			}
			eventListener.increaseProgress();
		}
		stopWatch.stop();
		if (logger.isInfoEnabled()) {
			logger.info(new StringBuilder("End of find, files found: ").append(countFiles).append(" in ")
					.append(stopWatch.getLastTaskTimeMillis() / 1000.0).append(" s."));
		}
		StringBuilder msg = new StringBuilder();
		if (countFiles == 0) {
			msg.append(messageSource.getMessage("NOT_FOUND", null, Locale.getDefault()));
		} else {
			msg.append(messageSource.getMessage("FOUND", new Object[] { countFiles }, Locale.getDefault()));
		}
		eventListener.setStatus(msg.toString());
	}

	private boolean isEquals(final String pattern, final boolean ignoreCase, String entryJarFile) {

		entryJarFile = entryJarFile.trim();
		String patternClass = pattern.trim();

		if (ignoreCase) {
			entryJarFile = entryJarFile.toLowerCase();
			patternClass = patternClass.toLowerCase();
		}
		entryJarFile = entryJarFile.substring(
				entryJarFile.lastIndexOf(File.separator) == -1 ? 0 : entryJarFile.lastIndexOf(File.separator));

		return entryJarFile.indexOf(patternClass) != -1;
	}

	private String getExtension(String file) {
		if (file.lastIndexOf(".") != -1) {
			return file.substring(file.lastIndexOf("."));
		}
		return "";
	}

	private JarFile getURLContent(String jarFile) throws IOException {
		URL url = null;
		JarURLConnection urlCon = null;
		// local archive
		url = new URL("jar:file:///" + jarFile + "!/");
		// remote archive
		// url=new URL("jar:http://.../archive.jar!/");
		urlCon = (JarURLConnection) (url.openConnection());
		return urlCon.getJarFile();
	}
}
