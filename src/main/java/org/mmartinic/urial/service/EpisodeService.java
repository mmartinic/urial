package org.mmartinic.urial.service;

import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.mmartinic.urial.model.Episode;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class EpisodeService {

    public Set<Episode> getEpisodes() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getClasses().add(JacksonJsonProvider.class);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource("http://localhost:8080/muflon/episode/list?range=all");
        Set<Episode> episodes = webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<Set<Episode>>() {
        });
        return episodes;
    }
}
