package com.ybg.meta.business.impl.redis;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ybg.meta.api.redis.RedisCacheBusiness;
import com.ybg.meta.api.system.dto.DictBackDto;
import com.ybg.meta.api.system.entity.Dict;
import com.ybg.meta.core.constant.CommonConst;
import com.ybg.meta.core.constant.RedisConst;
import com.ybg.meta.core.utils.BeanCopyUtil;
import com.ybg.meta.core.utils.RedisCache;
import com.ybg.meta.mapper.mapper.system.DictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author admin
 */
@Service
public class RedisCacheBusinessImpl implements RedisCacheBusiness {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private DictMapper dictMapper;

    @Override
    public void cacheIconList() {
        /*------------------------  检查redis中是否含有数据开始  -----------------------------*/
        List<DictBackDto> dictCache = redisCache.getCacheList(RedisConst.DICT_ICON_LIST);
        if (CollUtil.isNotEmpty(dictCache)) {
            return;
        }
        /*------------------------  检查redis中是否含有数据完成  -----------------------------*/
        /*------------------------  开始查询图标信息  -----------------------------*/
        Dict dict = dictMapper.selectOne(new LambdaQueryWrapper<Dict>()
                .eq(Dict::getDictCode, CommonConst.DICT_ICON_CODE));
        List<Dict> dicts = dictMapper.selectList(new LambdaQueryWrapper<Dict>()
                .eq(Dict::getParentId, dict.getDictId()));
        /*------------------------  查询图标信息完成  -----------------------------*/
        /*------------------------  存储图标信息开始  -----------------------------*/
        redisCache.setCacheList(RedisConst.DICT_ICON_LIST, BeanCopyUtil.copyList(dicts, DictBackDto.class));
        /*------------------------  存储图标信息完成  -----------------------------*/
    }
}
