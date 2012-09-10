package org.mmartinic.urial.service;

import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.mmartinic.urial.model.Episode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Component
public class EpisodeService {

    private final String m_episodeServiceUrl;

    @Autowired
    public EpisodeService(@Value("${episodes.service.url}") final String p_episodeServiceUrl) {
        Assert.notNull(p_episodeServiceUrl);
        m_episodeServiceUrl = p_episodeServiceUrl;
    }

    public Set<Episode> getEpisodes() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getClasses().add(JacksonJsonProvider.class);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(m_episodeServiceUrl);
        Set<Episode> episodes = webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<Set<Episode>>() {
        });
        return episodes;
    }
}
