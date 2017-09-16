/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 . All rights reserved.
 *          刀光剑影不是我门派，
 *          天空海阔自有我风采。
 *          双手一推非黑也非白，
 *          不好也不坏，没有胜又何来败。
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.util;

import cn.zhangxd.platform.common.redis.RedisRepository;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: 2017/8/24
 * Time: 下午11:26
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Service
@Slf4j
public class CacheUtils {

    @Autowired
    private RedisRepository redisRepository;


    public Boolean exists(String key) {
        return redisRepository.exists(key);
    }

    public void saveValues(String key, String value, long time) {
        redisRepository.setExpire(key, value, time);
    }

    public <T> List<T> findValues(String key, Class<T> valueClass) {
        try {
            return JSON.parseArray(redisRepository.get(key), valueClass);
        } catch (Exception e) {
            log.error("从redis获取同步数据操作失败\n-- {}", e);
        }
        return null;
    }

    /**
     * 获得List长度
     *
     * @param key
     * @return
     */
    public Long length(String key) {
        return redisRepository.length(key);
    }


    public List<String> findValuesRange(String key, int start, int end) {

        try {
            // 类型转换报错
            return redisRepository.getList(key, start, end);
        } catch (Exception e) {
            log.error("从redis获取分页数据失败\n -- {}", e.getMessage());
        }
        return null;
    }


    /**
     * SID是否首次查询档案数据
     *
     * @param sid
     * @return
     */
    public Boolean firstOps(String sid) {
        List<String> records = redisRepository.getList(Constants.STU_ARCHI_REC_LIST, 0, getQueryRecordSize());
        return records.contains(sid) ? Boolean.FALSE : Boolean.TRUE;
    }

    public void pushRecord(String sid) {
        redisRepository.in(Constants.STU_ARCHI_REC_LIST, sid);
    }

    public void leftPushAllRecord(List<String> records) {
        redisRepository.leftPushAll(Constants.STU_ARCHI_REC_LIST, records);
    }


    public Integer getQueryRecordSize() {
        Integer total = 0;
        if (redisRepository.exists(Constants.STU_ARCHI_REC_LIST)) {
            total = redisRepository.length(Constants.STU_ARCHI_REC_LIST).intValue();
        }
        return total;
    }


    /**
     * 入学档案
     */

    public ListOperations<String, String> opsForList() {
        return redisRepository.opsForList();
    }

    public HashOperations<String, String, String> opsForHash() {
        return redisRepository.opsForHash();
    }


}
