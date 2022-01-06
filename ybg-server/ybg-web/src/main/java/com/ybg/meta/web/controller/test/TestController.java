package com.ybg.meta.web.controller.test;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ybg.meta.business.rest.WeatherRest;
import com.ybg.meta.core.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * describe:
 *
 * @author admin
 * @date 2022/01/04 15:42
 */
@Slf4j
@RestController
public class TestController {

    @Resource
    private WeatherRest weatherRest;

    @PostMapping("/test")
    public String test(@RequestParam(name = "test") String test) {
        log.info("调用了test接口，参数为 {}", test);
        return test;
    }

    @PostMapping("/forest")
    public ResponseResult<String> weatherRest(@RequestParam(name = "test") String test,
                                              @RequestHeader("Authorization") String authorization) {
        String s = weatherRest.postTest(test, authorization);
        if (StrUtil.isNotBlank(s)) {
            return JSON.parseObject(s, new TypeReference<ResponseResult<String>>() {
            });
        }
        return ResponseResult.fail();
    }

}
