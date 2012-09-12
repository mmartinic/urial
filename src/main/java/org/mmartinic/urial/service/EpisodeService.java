package org.mmartinic.urial.service;

import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.mmartinic.urial.model.Episode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Component
public class EpisodeService {

    private static final Logger logger = LogManager.getLogger(EpisodeService.class);

    private final String m_episodeServiceUrl;
    private final GenericType<Set<Episode>> m_genericType;
    private final Client m_client;

    @Autowired
    public EpisodeService(@Value("${episodes.service.url}") final String p_episodeServiceUrl) {
        Assert.notNull(p_episodeServiceUrl);
        m_episodeServiceUrl = p_episodeServiceUrl;

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getClasses().add(JacksonJsonProvider.class);
        m_client = Client.create(clientConfig);
        m_genericType = new GenericType<Set<Episode>>() {
        };
    }

    public Set<Episode> getEpisodes() {
        logger.info("Get Episodes from service");
        Set<Episode> episodes = m_client.resource(m_episodeServiceUrl).accept(MediaType.APPLICATION_JSON).get(m_genericType);
        logger.info("Get Episodes service returned " + episodes.size() + " episodes");
        return episodes;
    }
}
