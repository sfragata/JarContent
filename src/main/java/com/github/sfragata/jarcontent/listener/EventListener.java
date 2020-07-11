package com.github.sfragata.jarcontent.listener;

import com.github.sfragata.jarcontent.to.JarContentTO;

/**
 * Interface to trigger events to the GUI
 * 
 * @author Fragata da Silva, Silvio
 * 
 */
public interface EventListener {

	void addResult(JarContentTO contentTO);

	void increaseProgress();

	void setCollectionLength(int length);

	void setStatus(String msg);

	void error(Exception e);
}
