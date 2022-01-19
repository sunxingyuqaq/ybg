package com.ybg.meta.api.redis;

/**
 * describe: Redis 缓存部分数据
 *
 * @author admin
 * @date 2021/12/27 13:39
 */
public interface RedisCacheBusiness {

    /**
     * 缓存图标信息
     *
     * @throws Exception e
     */
    void cacheIconList() throws Exception;
}
