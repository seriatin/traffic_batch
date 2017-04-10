package com.kt.traffic.common.reader;

import com.kt.traffic.bus.domain.GyeonggiBaseInfoServiceVO;
import com.kt.traffic.common.domain.BusStationInfoVO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BigFence on 2017-04-10.
 */
public class GyeonggiRestDomainReader<E, T> extends RestDomainReader<E, T> {
    public GyeonggiRestDomainReader(String apiUrl, String apiKey, URI apiUri, RestTemplate restTemplate, Class<T> clazz) {
        super(apiUrl, apiKey, apiUri, restTemplate, clazz);
    }

    protected List<E> fetchDataFromAPI() {
        ResponseEntity<T> responseEntity;
        if ( apiUri != null ) {
            responseEntity = restTemplate.exchange(this.apiUri, HttpMethod.GET, HttpEntity.EMPTY, clazz);
            if ( responseEntity.getStatusCode().equals(HttpStatus.OK) ) {
                Object response = responseEntity.getBody();
                if ( response instanceof GyeonggiBaseInfoServiceVO) {

                    String downloadUrl = ((GyeonggiBaseInfoServiceVO)response).getMsgBody().getBaseInfoItem().getStationDownloadUrl();

                    restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());

                    ResponseEntity<byte[]> stationLocation = restTemplate.getForEntity(downloadUrl, byte[].class, HttpEntity.EMPTY);

                    try {
                        String stationStr = new String(stationLocation.getBody(), "EUC-KR");
                        String[] stationArr = stationStr.split("\\^");

                        List<BusStationInfoVO> itemList = new ArrayList<>();

                        for (String stationItems : stationArr) {
                            if ( stationItems.indexOf("STATION_ID") != -1 ) continue;
                            String[] items = stationItems.split("\\|");
                            BusStationInfoVO vo = new BusStationInfoVO();
                            vo.setRegionCd(items[8]);
                            vo.setStationId(items[0]);
                            vo.setStationNm(items[1]);
                            vo.setCenterYn(items[3]);
                            vo.setGpsX(Double.parseDouble(items[4]));
                            vo.setGpsY(Double.parseDouble(items[5]));
                            vo.setRegionNm(items[6]);
                            vo.setStationOwnId(items[7]);
                            itemList.add(vo);
                        }

                        return (List<E>)itemList;

                    } catch ( UnsupportedEncodingException e ) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

}
