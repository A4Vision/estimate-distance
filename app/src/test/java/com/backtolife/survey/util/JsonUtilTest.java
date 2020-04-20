package com.backtolife.survey.util;



import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class JsonUtilTest {
    @Test
    public void parseJsonIntegerList(){
        List<Integer> res = JsonUtil.loadIntegerList("[1, 2, 3   , 4]");
        assertThat(res).isEqualTo(Arrays.asList(1, 2, 3, 4));
    }

    @Test
    public void integerListToJson(){
        String res = JsonUtil.integerListToJson(Arrays.asList(1, 2, 3, 4)).toString();
        assertThat(res).isEqualTo("[1,2,3,4]");
    }

    @Test
    public void invalidIntegerList(){
        assertThat(JsonUtil.loadIntegerList("[1,2, 3")).isNull();
    }

    @Test
    public void doublePrecision(){
        double x = 1585999159.23811;
        double y = 1585999159.23812;
        assertThat(JsonValue.valueOf(x).toString()).isNotEqualTo(JsonValue.valueOf(y).toString());
    }

    @Test
    public void parseEmptyArray(){
        assertThat(JsonArray.readFrom("[]").asArray()).isEmpty();
    }
}