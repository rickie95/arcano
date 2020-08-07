package com.riccardomalavolti.arcano.endpoints;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class Arcano extends Application {
	/* This class is intentionally empty.
	 * 
	 * ApplicationPath annotation sets the 
	 * root URL for application resources.
	 * 
	 * Resources will be available starting 
	 * at IP-ADDRESS:PORT/
	 */
}
