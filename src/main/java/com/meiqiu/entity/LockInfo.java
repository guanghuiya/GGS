package com.meiqiu.entity;

import com.meiqiu.config.RedisLock;
import lombok.Data;

/**
 * @Description
 * @Author sgh
 * @Date 2025/2/19
 * @Time 17:53
 */
@Data
public class LockInfo {

    private String key;

    private String value;

    private int expireTime;

    //更新时间
    private long renewalTime;

    //更新间隔
    private long renewalInterval;

    public static LockInfo getLockInfo(String key, String value, int expireTime) {
        LockInfo lockInfo = new LockInfo();
        lockInfo.setKey(key);
        lockInfo.setValue(value);
        lockInfo.setExpireTime(expireTime);
        lockInfo.setRenewalTime(System.currentTimeMillis());
        lockInfo.setRenewalInterval(expireTime * 2000 / 3);
        return lockInfo;
    }
}
