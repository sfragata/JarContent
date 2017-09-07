package com.github.sfragata.jarcontent.to;

import java.io.Serializable;

/**
 * Transfer Object
 *
 * @author Fragata da Silva, Silvio
 */
public class JarContentTO
    implements Serializable {

    private static final long serialVersionUID = -1L;

    private String jarName;

    private String className;

    public JarContentTO(final String jarName, final String className) {
        this.jarName = jarName;
        this.className = className;
    }

    @Override
    public String toString() {

        return new StringBuilder("JarContentTO{jarName='").append(this.jarName).append("', className='")
            .append(this.className).append("'}").toString();
    }

    public String getClassName() {

        return this.className;
    }

    public void setClassName(
        final String className) {

        this.className = className;
    }

    public String getJarName() {

        return this.jarName;
    }

    public void setJarName(
        final String jarName) {

        this.jarName = jarName;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + (this.className == null ? 0 : this.className.hashCode());
        result = prime * result + (this.jarName == null ? 0 : this.jarName.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(
        final Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JarContentTO other = (JarContentTO) obj;
        if (this.className == null) {
            if (other.className != null) {
                return false;
            }
        } else if (!this.className.equals(other.className)) {
            return false;
        }
        if (this.jarName == null) {
            if (other.jarName != null) {
                return false;
            }
        } else if (!this.jarName.equals(other.jarName)) {
            return false;
        }
        return true;
    }
}
