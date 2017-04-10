package com.kt.traffic.common.reader;

import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * Created by BigFence on 2017-04-10.
 */
public class TagoRestDomainReader<E, T> extends RestDomainReader<E, T> {


    public TagoRestDomainReader(String apiUrl, String apiKey, URI apiUri, RestTemplate restTemplate, Class<T> clazz) {
        super(apiUrl, apiKey, apiUri, restTemplate, clazz);
    }

    protected List<E> fetchDataFromAPI() {

        return null;
    }
}
