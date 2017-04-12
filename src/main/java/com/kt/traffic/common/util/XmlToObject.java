package com.kt.traffic.common.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by BigFence on 2017-04-11.
 */
public class XmlToObject {

    private static final Logger log = LoggerFactory.getLogger(XmlToObject.class);

    public static <T> List<T> findData(JSONObject obj, String prop, String model, Map<String, Object> schema) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
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
        return convertKey(tmpArr, model, schema);
    }

    public static <T> List<T> convertKey(JSONArray objArr, String model, Map<String, Object> schema) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        int size = objArr.length();
        List<T> resultList = new ArrayList<>();
        for ( int i = 0 ; i < size ; i++ ) {
            if ( objArr.get(i) instanceof JSONObject ) {
                JSONObject obj = objArr.getJSONObject(i);
                /*Set<String> keyset = new JSONObject(obj.toString()).keySet();
                for ( String key : keyset ) {

                    obj.put(schema.getString(key), obj.get(key));
                    if ( !key.equals(schema.getString(key)) ) obj.remove(key);
                }*/

                T result = getInstance(model, obj, schema);
                resultList.add(result);
                //objArr.put(i, obj);
            }
        }
        return resultList;
    }

    public static <T> T getInstance(String model, JSONObject object, Map<String, Object> schema) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        Class modelClass = Class.forName(model);
        Object modelObject = modelClass.newInstance();

        try {
            for ( String key : schema.keySet() ) {
                Field field = modelClass.getDeclaredField((String)schema.get(key));
                Class fieldType = field.getType();
                Method method = modelClass.getMethod("set" + toUpperCaseFirstChar((String)schema.get(key)), new Class[] { field.getType() });
                if ( fieldType.toString().indexOf("String") != -1 ) method.invoke(modelObject, ( object.has(key) ? object.get(key).toString() : "" ));
                else method.invoke(modelObject, object.get(key));
            }
        } catch ( Exception e  ){
            log.error("Exception Object {}" , object.toString());
            e.printStackTrace();
        }

        return (T)modelObject;

    }

    public static String toUpperCaseFirstChar(String src) {
        return Character.toString(src.charAt(0)).toUpperCase() + src.substring(1);
    }


}
