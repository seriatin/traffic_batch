package com.kt.traffic.common.util;

import org.json.JSONObject;
import org.json.XML;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by BigFence on 2017-04-11.
 */
public class XmlToObjectTest {

    private static String XML_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><response><header><resultCode>00</resultCode><resultMsg>NORMAL SERVICE.</resultMsg></header><body><items><item><bstopArsno>01001</bstopArsno><bstopId>167970102</bstopId><bstopNm>영주삼거리</bstopNm><gpsX>129.0332616667</gpsX><gpsY>35.1153233333</gpsY></item><item><bstopArsno>01002</bstopArsno><bstopId>169310303</bstopId><bstopNm>영주삼거리</bstopNm><gpsX>129.0331583333</gpsX><gpsY>35.115205</gpsY></item></items></body></response>";
    private static String XML_STRING2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><response><header><resultCode>00</resultCode><resultMsg>NORMAL SERVICE.</resultMsg></header><body><items><item><bstopArsno>01001</bstopArsno><bstopId>167970102</bstopId><bstopNm>영주삼거리</bstopNm><gpsX>129.0332616667</gpsX><gpsY>35.1153233333</gpsY></item></items></body></response>";
    private static int PRETTY_PRINT_INDENT_FACTOR = 4;
    private static Logger log = LoggerFactory.getLogger(XmlToObjectTest.class);

    private static String MAPPING_MODEL = "com.kt.traffic.common.domain.BusStationInfoVO";
    private static JSONObject MAPPER_SCHEMA = new JSONObject("{ \"bstopArsno\" : \"stationOwnId\", \"bstopNm\" : \"stationNm\", \"bstopId\": \"stationId\", \"gpsX\": \"gpsX\", \"gpsY\":\"gpsY\" }");

    @Test
    public void xmlToJson() {
        try {
            JSONObject jsonObject = XML.toJSONObject(XML_STRING);
            String jsonPrettyPrintString = jsonObject.toString(PRETTY_PRINT_INDENT_FACTOR);
            log.info("Test \n{}",jsonPrettyPrintString);

            /*List<BusStationInfoVO> t = XmlToObject.findData(jsonObject, "response.body.items.item", MAPPING_MODEL, MAPPER_SCHEMA);
            log.info("ArrayData");
            log.info(t.toString());*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void xmlToJson2() {
        try {
            JSONObject jsonObject = XML.toJSONObject(XML_STRING2);
            String jsonPrettyPrintString = jsonObject.toString(PRETTY_PRINT_INDENT_FACTOR);
            log.info("Test \n{}",jsonPrettyPrintString);

            long startTime = System.currentTimeMillis();


            /*for ( int i = 0 ; i < 10000 ; i++ ) {
                List<BusStationInfoVO> t = XmlToObject.findData(jsonObject, "response.body.items.item", MAPPING_MODEL, MAPPER_SCHEMA);
                log.info("ArrayData");
                log.info(t.toString());

            }*/
            long endTime = System.currentTimeMillis();

// Total time
            long lTime = endTime - startTime;
            log.info("TIME : " + lTime + "(ms)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public JSONArray findData(JSONObject obj, String prop, String model, JSONObject schema) {
        String[] propArr = prop.split("\\.");
        JSONArray tmpArr = null;
        for ( String p : propArr ) {
            if ( obj.get(p) instanceof JSONObject ) {
                obj = obj.getJSONObject(p);
            } else if ( obj.get(p) instanceof JSONArray ) {
                tmpArr = obj.getJSONArray(p);
            } else if ( obj.isNull(p) ) {
                return null;
            }
        }
        if ( tmpArr == null ) {
            tmpArr = new JSONArray();
            tmpArr.put(obj);
        }
        tmpArr = convertKey(tmpArr, schema);
        return tmpArr;
    }

    public JSONArray convertKey(JSONArray objArr, JSONObject schema) {
        int size = objArr.length();
        for ( int i = 0 ; i < size ; i++ ) {
            if ( objArr.get(i) instanceof JSONObject ) {
                JSONObject obj = objArr.getJSONObject(i);
                Set<String> keyset = new JSONObject(obj.toString()).keySet();
                for ( String key : keyset ) {
                    obj.put(schema.getString(key), obj.get(key));
                    if ( !key.equals(schema.getString(key)) ) obj.remove(key);
                }
                objArr.put(i, obj);
            }
        }
        return objArr;
    }*/

}