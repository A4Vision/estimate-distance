package com.backtolife.survey.util;

import com.backtolife.survey.server.HTTPUtil;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;


public class HTTPUtilTest {
    private static final boolean EXECUTE_SLOW_TEST = true;

    @Before
    public void assumeOrNot(){
        Assume.assumeTrue(EXECUTE_SLOW_TEST);
    }
    
    @Test
    public void getHelloWorld(){
        String response = HTTPUtil.postHTTP("https://europe-west2-tweetserver0-0.cloudfunctions.net/hello_get",
                "");
        assertThat(response).isEqualTo("Hello World!");
    }
}
