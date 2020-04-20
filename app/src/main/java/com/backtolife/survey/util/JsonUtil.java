package com.backtolife.survey.util;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    public static String convertStringMapToJson(Map<String, String> d) {
        JsonObject obj = new JsonObject();
        for (String key : d.keySet()) {
            obj.set(key, d.get(key));
        }
        return obj.toString();
    }


    private static void clearLastChar(StringBuilder builder) {
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
    }

    public static List<Integer> loadIntegerList(String json) {
        JsonArray array;
        try {
            array = JsonArray.readFrom(json);
        } catch (ParseException | UnsupportedOperationException e) {
            System.out.println("invalid json");
            return null;
        }
        return integersListFromJson(array);
    }

    public static List<Integer> integersListFromJson(JsonArray array) {
        List<Integer> res = new ArrayList<>();
        try {
            for (int i = 0; i < array.size(); ++i) {
                res.add(array.get(i).asInt());
            }
        }catch(UnsupportedOperationException e){
            e.printStackTrace();
            return null;
        }
        return res;
    }

    public static List<Double> doublesListFromJson(JsonArray array) {
        List<Double> res = new ArrayList<>();
        try {
            for (int i = 0; i < array.size(); ++i) {
                res.add(array.get(i).asDouble());
            }
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    public static JsonArray integerListToJson(List<Integer> jsons) {
        JsonArray jsonNames = new JsonArray();
        for (Integer num : jsons) {
            jsonNames.add(JsonValue.valueOf(num));
        }
        return jsonNames;
    }

    public static JsonArray toJsonArray(Iterable<JsonValue> jsons) {
        JsonArray jsonNames = new JsonArray();
        for (JsonValue obj : jsons) {
            jsonNames.add(obj);
        }
        return jsonNames;
    }

//    public static String encodeBase64(byte[] data) {
//        return new String(Base64.encodeBase64(data));
//    }
//
//    public static byte[] decodeBase64(String data) {
//        return Base64.decodeBase64(data.getBytes());
//    }

}
