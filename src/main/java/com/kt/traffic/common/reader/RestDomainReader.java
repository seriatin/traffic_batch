package com.kt.traffic.common.reader;

import com.kt.traffic.bus.domain.SeoulBusStationInfoVO;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * Created by BigFence on 2017-04-07.
 */
public class RestDomainReader<E, T> implements ItemReader<E> {

    private final String apiUrl;
    private final String apiKey;
    private final URI apiUri;
    private final RestTemplate restTemplate;
    // private final ParameterizedTypeReference<Resource<T>> typeReference;
    private Class<T> clazz;

    private int nextItemIndex;
    private List<E> items;

    public RestDomainReader(String apiUrl, String apiKey, URI apiUri, RestTemplate restTemplate, Class<T> clazz) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.apiUri = apiUri;
        this.restTemplate = restTemplate;
        this.clazz = clazz;
        nextItemIndex = 0;
    }


    @Override
    public E read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if ( itemsNotInitialized() ) {
            items = fetchDataFromAPI();
        }

        E nextItem = null;

        if ( nextItemIndex < items.size() ) {
            nextItem = items.get(nextItemIndex);
            nextItemIndex++;
        }

        return nextItem;
    }

    private boolean itemsNotInitialized() {
        return this.items == null;
    }

    private List<E> fetchDataFromAPI() {
        /*ResponseEntity<Resource<T>> responseEntity;
        if ( apiUri != null ) {
            responseEntity = restTemplate.exchange(this.apiUri, HttpMethod.GET, HttpEntity.EMPTY, typeReference);
            if ( responseEntity.getStatusCode().equals(HttpStatus.OK) ) {
                Object response = responseEntity.getBody();
                if ( response instanceof SeoulBusStationInfoVO) {
                    return (List<E>)((SeoulBusStationInfoVO) response).items();
                }
            }
        }*/

        ResponseEntity<T> responseEntity;
        if ( apiUri != null ) {
            responseEntity = restTemplate.exchange(this.apiUri, HttpMethod.GET, HttpEntity.EMPTY, clazz);
            if ( responseEntity.getStatusCode().equals(HttpStatus.OK) ) {
                Object response = responseEntity.getBody();
                if ( response instanceof SeoulBusStationInfoVO) {
                    return (List<E>)((SeoulBusStationInfoVO) response).items();
                }
            }
        }

        return null;
    }
}
