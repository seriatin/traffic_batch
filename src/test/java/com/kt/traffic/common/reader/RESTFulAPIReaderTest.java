package com.kt.traffic.common.reader;

import com.kt.traffic.common.domain.BusStationInfoVO;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BigFence on 2017-04-11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RESTFulAPIReaderTest {

    private ItemReader<BusStationInfoVO> reader;
    private static final Logger log = LoggerFactory.getLogger(RESTFulAPIReaderTest.class);

    @Autowired
    private Environment environment;
    @Autowired
    private RestTemplate restTemplate;

    @Before
    public void setReader() throws URISyntaxException {
        URI uri = new URI("http://ws.bus.go.kr/api/rest/stationinfo/getStationByName?serviceKey=lz3xwnQikI6Q667LJSVUDLny0sAiaKvWk1feXbrJ6Fc0paC7EE2rzEowBQmIkmDt%2B5oiN91ahjzT0Ne5NeK81Q%3D%3D&stSrch=%25");
        Map<String, Object> schema = new HashMap<String, Object>();
        JSONObject schemaObj = new JSONObject("{ \"arsId\" : \"stationOwnId\", \"stNm\" : \"stationNm\", \"stId\": \"stationId\", \"tmX\": \"gpsX\", \"tmY\":\"gpsY\", \"posX\" : \"posX\", \"posY\" : \"posY\" }");
        schema = schemaObj.toMap();
        this.reader = new RESTFulAPIReader<>("", "", uri, restTemplate, "ServiceResult.msgBody.itemList", schema , "com.kt.traffic.common.domain.BusStationInfoVO");
    }

    @Test
    public void readData() throws Exception {
        BusStationInfoVO item = this.reader.read();
        while ( item != null ) {
            //log.info("[Item] {}", item.toString());
            item = this.reader.read();
        }
    }


}