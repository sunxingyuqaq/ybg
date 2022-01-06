package com.ybg.meta.business.rest;

import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Query;

/**
 * describe:
 *
 * @author admin
 * @date 2022/01/04 15:33
 */
public interface WeatherRest {

    /**
     * 测试接口请求
     *
     * @param test          t
     * @param authorization token
     * @return s
     */
    @Post(url = "http://localhost:9000/ybg/test")
    String postTest(@Query(name = "test") String test, @Header("Authorization") String authorization);
}
