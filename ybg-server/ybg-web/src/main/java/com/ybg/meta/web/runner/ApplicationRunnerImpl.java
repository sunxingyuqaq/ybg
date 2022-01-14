package com.ybg.meta.web.runner;

import cn.dev33.satoken.SaManager;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ybg.meta.api.redis.RedisCacheBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * describe:
 *
 * @author admin
 * @date 2021/12/29 09:41
 */
@Slf4j
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Resource
    private RedisCacheBusiness redisCacheBusiness;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("当前应用的sa-token配置信息如下：");
        log.info("\n" + JSON.toJSONString(SaManager.getConfig(), SerializerFeature.PrettyFormat));
        /*------------------------  ===  -----------------------------*/
        log.info("开始拉取缓存数据！");
        /*------------------------  获取部分redis缓存  -----------------------------*/
        redisCacheBusiness.cacheIconList();
        String success = ResourceUtil.readUtf8Str("success.txt");
        Console.log(success);
    }
}
