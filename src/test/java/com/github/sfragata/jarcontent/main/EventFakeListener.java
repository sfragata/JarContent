package com.github.sfragata.jarcontent.main;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.sfragata.jarcontent.listener.EventListener;
import com.github.sfragata.jarcontent.to.JarContentTO;

/**
 * Fake Event Listener (for test case only)
 *
 * @author Fragata da Silva, Silvio
 */
@Component("eventListener")
public class EventFakeListener
    implements EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventFakeListener.class);

    private final List<JarContentTO> jarContentList = new ArrayList<>();

    @Override
    public void addResult(
        final JarContentTO contentTO) {

        this.jarContentList.add(contentTO);
    }

    @Override
    public void increaseProgress() {

        LOGGER.info("increaseProgress");
    }

    @Override
    public void setCollectionLength(
        final int length) {

        LOGGER.info("length: " + length);
    }

    @Override
    public void setStatus(
        final String msg) {

        LOGGER.info(msg);
    }

    @Override
    public void error(
        final Exception e) {

        e.printStackTrace();
    }

    public List<JarContentTO> getJarContentList() {

        return this.jarContentList;
    }

}
