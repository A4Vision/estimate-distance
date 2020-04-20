package com.backtolife.survey.util;

import java.util.HashMap;
import java.util.Map;

public class SimpleProfiler {
    static Map<String, Long> startTimes = new HashMap<>();
    public static void BEFORE(String name){
        startTimes.put(name, System.currentTimeMillis());
    }
    public static void AFTER(String name){
        long elapsed = System.currentTimeMillis() - startTimes.get(name);
        System.out.println(name + " took " + elapsed + "ms");
    }
}
