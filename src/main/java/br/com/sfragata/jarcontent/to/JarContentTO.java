package br.com.sfragata.jarcontent.to;

import java.io.Serializable;

/**
 * Transfer Object
 * 
 * @author Fragata da Silva, Silvio
 */
public class JarContentTO implements Serializable {

	private static final long serialVersionUID = -1L;
	private String jarName;
	private String className;

	public JarContentTO(String jarName, String className) {
		this.jarName = jarName;
		this.className = className;
	}

	@Override
	public String toString() {
		return new StringBuilder("JarContentTO{jarName='").append(jarName)
				.append("', className='").append(className).append("'}")
				.toString();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getJarName() {
		return jarName;
	}

	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((jarName == null) ? 0 : jarName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JarContentTO other = (JarContentTO) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (jarName == null) {
			if (other.jarName != null)
				return false;
		} else if (!jarName.equals(other.jarName))
			return false;
		return true;
	}
}
