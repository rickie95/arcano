package com.riccardomalavolti.arcano.dto;

import java.util.List;

import javax.ws.rs.core.Link;

public interface RESTResource {

    List<Link> getLinks(String absoluteBasePath);

}
