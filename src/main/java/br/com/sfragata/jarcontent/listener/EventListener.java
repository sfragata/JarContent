/**
 * 
 */
package br.com.sfragata.jarcontent.listener;

import br.com.sfragata.jarcontent.to.JarContentTO;

/**
 * Interface to trigger events to the GUI
 * 
 * @author Fragata da Silva, Silvio
 * 
 */
public interface EventListener {

	public void addResult(JarContentTO contentTO);

	public void increaseProgress();

	public void setCollectionLength(int length);

	public void setStatus(String msg);

	public void error(Exception e);
}
