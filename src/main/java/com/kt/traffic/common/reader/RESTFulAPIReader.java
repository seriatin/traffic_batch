package com.kt.traffic.common.reader;

import com.kt.traffic.common.util.XmlToObject;
import org.json.XML;
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
import java.util.Map;

/**
 * Created by BigFence on 2017-04-07.
 */
public class RESTFulAPIReader<E> implements ItemReader<E> {

    protected final String apiUrl;
    protected final String apiKey;
    protected final URI apiUri;
    protected final RestTemplate restTemplate;
    // private final ParameterizedTypeReference<Resource<T>> typeReference;
    protected String domainClassName;
    protected String prop;
    protected Map<String, Object> schema;

    protected int nextItemIndex;
    protected List<E> items;

    public RESTFulAPIReader(String apiUrl, String apiKey, URI apiUri, RestTemplate restTemplate, String prop, Map<String, Object> schema, String domainClassName) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.apiUri = apiUri;
        this.restTemplate = restTemplate;
        this.domainClassName = domainClassName;
        this.prop = prop;
        this.schema = schema;
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

    protected boolean itemsNotInitialized() {
        return this.items == null;
    }

    protected List<E> fetchDataFromAPI() {

        ResponseEntity<String> entity = restTemplate.exchange(this.apiUri, HttpMethod.GET, HttpEntity.EMPTY, String.class);
        if ( entity.getStatusCode().equals(HttpStatus.OK) ) {
            String xmlString = entity.getBody();

            try {
                List<E> data = XmlToObject.findData(XML.toJSONObject(xmlString), this.prop, this.domainClassName, this.schema);
                return data;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        /*ResponseEntity<T> responseEntity;
        if ( apiUri != null ) {
            responseEntity = restTemplate.exchange(this.apiUri, HttpMethod.GET, HttpEntity.EMPTY, clazz);
            if ( responseEntity.getStatusCode().equals(HttpStatus.OK) ) {
                Object response = responseEntity.getBody();
                if ( response instanceof SeoulBusStationInfoVO) {
                    return (List<E>)((SeoulBusStationInfoVO) response).items();
                } else if ( response instanceof PusanBusStationInfoVO ) {
                    return (List<E>)((PusanBusStationInfoVO) response).items();
                }
            }
        }*/

        return null;
    }
}
