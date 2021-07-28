package com.riccardomalavolti.arcano.endpoints.rest;

import java.net.URI;
import java.util.UUID;

public interface ResourceEndpoint {
	
	String getResourceUri(URI baseUri, UUID resourceId);

}
