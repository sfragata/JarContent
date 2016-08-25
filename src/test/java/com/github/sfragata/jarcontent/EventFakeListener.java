package com.github.sfragata.jarcontent;

import org.springframework.stereotype.Component;

import com.github.sfragata.jarcontent.listener.EventListener;
import com.github.sfragata.jarcontent.to.JarContentTO;

/**
 * Fake Event Listener (for test case only)
 * 
 * @author Fragata da Silva, Silvio
 */
@Component("eventListener")
public class EventFakeListener implements EventListener {

	@Override
	public void addResult(JarContentTO contentTO) {
		System.out.println(contentTO);
	}

	@Override
	public void increaseProgress() {
		System.out.println("increaseProgress");
	}

	@Override
	public void setCollectionLength(int length) {
		System.out.println("length: " + length);
	}

	@Override
	public void setStatus(String msg) {
		System.out.println(msg);
	}

	@Override
	public void error(Exception e) {
		e.printStackTrace();
	}
}
