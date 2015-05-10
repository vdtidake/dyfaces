package org.dyfaces.utils;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(eager=true)
@ApplicationScoped
public class Dyfaces implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 8163975741704463463L;
	public final static String version = "0.0.1-SNAPSHOT";
    private static final Logger LOGGER = Logger.getLogger(Dyfaces.class.getName());

	public Dyfaces() {
		LOGGER.log(Level.INFO, "Dyfaces current version {0}", version);
	}

	public String getVersion() {
		return version;
	}

}
