package com.meiqiu.config;

import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class RedisCacheService {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

    private static final String LOCK = "TRUE";

    private static final long DEFAULT_LOCK_TIME = 60000L;

    public static final long LOCK_TIME_ONE_HOUR = 60 * 60000L;

    public static final long LOCK_TIME_HALF_HOUR = 30 * 60000L;

    public static final long LOCK_TIME_FIVE_MINS = 5 * 60000L;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate StringRedisTemplate;

    private HashOperations<String, Object, Object> hashOps;

    private ValueOperations<String, Object> opsForValue;

    public Collection<String> getCacheNames() {
        try {
            return this.redisTemplate.keys("*Cache");
        } catch (Exception e) {
        }
        return null;
    }

    public int size(String cacheName) {
        try {
            return this.hashOps.size(cacheName).intValue();
        } catch (Exception e) {
        }
        return -1;
    }

    public void clear() {
        logger.info("Do Redis Clear start...");
        try {
            this.redisTemplate.execute(new RedisCallback() {

                @Override
                public Object doInRedis(RedisConnection connect) throws DataAccessException {
                    RedisCacheService.logger.info("Redis connect is " + (!connect.isClosed()));
                    connect.execute("flushall", new byte[0][]);
                    return null;
                }
            });
        } catch (Exception e) {
            update(e);
            logger.error("Clear cache failed:", e);
        }
        logger.info("Do Redis Clear end.");
    }

    public <PK, T> boolean save(PK pk, T t) {
        if (t == null) {
            return false;
        }
        try {
            String json = JSONUtil.toJsonStr(t);
            getHashOps().put(getKey(t), pk, json);
            return true;
        } catch (Exception e) {
            update(e);
            logger.error("Save {0} failed.", e);
            e.printStackTrace();
        }
        return false;
    }

    public <PK, T> boolean save(String str, PK pk, T t) {
        if (t == null) {
            return false;
        }
        try {
            String json = JSONUtil.toJsonStr(t);
            getHashOps().put(str, pk, json);
            return true;
        } catch (Exception e) {
            update(e);
            logger.error("Save {0} failed.", e);
            e.printStackTrace();
            ;
        }
        return false;
    }

    public <PK> boolean delete(String str, PK pk) {
        if (str == null) {
            return false;
        }
        try {
            getHashOps().delete(str, pk);
            return true;
        } catch (Exception e) {
            update(e);
            logger.error("delete {0} failed.", e);
            e.printStackTrace();
        }
        return false;
    }

    public <PK, T> T get(PK pk, Class<T> type) {
        try {
            String json = (String) getHashOps().get(getKey(type), pk);
            return getObjectFromJson(json, type);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public <PK, T> T get(String str, PK pk, Class<T> type) {
        try {
            String json = (String) getHashOps().get(str, pk);
            return getObjectFromJson(json, type);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public <T> List<T> loadAll(Class<T> type) {
        String hkey = getKey(type);
        return loadAll(hkey, type);
    }

    public <T> List<T> loadAll(String typeName, Class<T> type) {
        try {
            return getListFromJson(getHashOps().values(typeName), type);
        } catch (Exception e) {
        }
        return null;
    }

    public <PK, T> boolean remove(PK pk, Class<T> type) {
        try {
            getHashOps().delete(getKey(type), new Object[]{pk});
            return true;
        } catch (Exception e) {
            update(e);
            e.printStackTrace();
        }
        return false;
    }

    public <T> boolean removeAll(Class<T> type) {
        this.redisTemplate.delete(getKey(type));
        return true;
    }

    private void update(Exception e) {
    }

    private HashOperations<String, Object, Object> getHashOps() {
        if (this.hashOps == null) {
            this.hashOps = this.redisTemplate.opsForHash();
        }
        return this.hashOps;
    }

    private ValueOperations<String, Object> getOpsForValue() {
        if (this.opsForValue == null) {
            this.opsForValue = this.redisTemplate.opsForValue();
        }
        return this.opsForValue;
    }

    private static String getKey(Object object) {
        return getKey(object.getClass());
    }

    private static String getKey(Class<?> type) {
        return getKey(type.getSimpleName());
    }

    private static String getKey(String typeName) {
        return String.format("%sCache", new Object[]{typeName});
    }

    private static <T> T getObjectFromJson(String json, Class<T> clazz) {
        T t = null;
        try {
//            t = JsonUtils.toObject(json, clazz);
            t = JSONUtil.toBean(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    private static <T> List<T> getListFromJson(List<Object> list, Class<T> clazz) {
        List<T> tList = new ArrayList();
        if ((list != null) && (list.size() > 0)) {
            for (Object obj : list) {
                if ((obj instanceof String)) {
                    T t = getObjectFromJson((String) obj, clazz);
                    tList.add(t);
                }
            }
        }
        return tList;
    }

    private static <R> List<R> getListFromArryJson(List<Object> list, Class<R> clazz) {
        List<R> rList = new ArrayList();
        if ((list != null) && (list.size() > 0)) {
            for (Object obj : list) {
                R r = getObjectFromJson((String) obj, clazz);
                rList.add(r);
            }
        }
        return rList;
    }

    public <PK, T> boolean containsKey(PK pk, Class<T> type) {
        return getHashOps().hasKey(getKey(type), pk).booleanValue();
    }

    public <T> boolean expire(Class<T> type, int expiredSeconds) {
        return getHashOps().getOperations().expire(getKey(type), expiredSeconds, TimeUnit.SECONDS).booleanValue();
    }

    public <T> boolean expireForDays(String str, int expiredDays) {
        return getHashOps().getOperations().expire(str, expiredDays, TimeUnit.DAYS).booleanValue();
    }

    public <T> boolean expireForSeconds(String str, int expiredSeconds) {
        return getHashOps().getOperations().expire(str, expiredSeconds, TimeUnit.SECONDS).booleanValue();
    }

    public <PK> void saveCommon(String name, PK pk, String value) {
        getHashOps().put(name, pk, value);
    }

    public <PK> String getCommon(String name, PK pk) {
        if (getHashOps().hasKey(name, pk).booleanValue()) {
            return (String) getHashOps().get(name, pk);
        }
        return null;
    }

    public boolean hasCache(String name) {
        List<Object> objects = getHashOps().values(name);
        if (CollectionUtils.isEmpty(objects)) {
            return false;
        }
        return true;
    }

    public Long getAndIncrement(String key) {
        return this.redisTemplate.opsForValue().increment(key);
    }

    public void initKey(String key, Long value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }


    /**
     * @param key
     * @param value
     */
    public void putKeyValue(String key, String id, Long time, Object value) {
        //更改在redis里面查看key编码问题
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
        //设置key并且设置有效时间
        expireKey(key, time, TimeUnit.SECONDS);
//        vo.set(key, value, time, TimeUnit.SECONDS);
    }

    public Object getKeyValue(String key) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }

    /**
     * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
     *
     * @param oldKey
     * @param newKey
     */
    public void renameKey(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * newKey不存在时才重命名
     *
     * @param oldKey
     * @param newKey
     * @return 修改成功返回true
     */
    public boolean renameKeyNotExist(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除多个key
     *
     * @param keys
     */
    public void deleteKey(String... keys) {
        Set<String> kSet = Stream.of(keys).map(k -> k).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    /**
     * 删除Key的集合
     *
     * @param keys
     */
    public void deleteKey(Collection<String> keys) {
        Set<String> kSet = keys.stream().map(k -> k).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    /**
     * 设置key的生命周期
     *
     * @param key
     * @param time
     * @param timeUnit
     */
    public void expireKey(String key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 指定key在指定的日期过期
     *
     * @param key
     * @param date
     */
    public void expireKeyAt(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    /**
     * 查询key的生命周期
     *
     * @param key
     * @param timeUnit
     * @return
     */
    public long getKeyExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 将key设置为永久有效
     *
     * @param key
     */
    public void persistKey(String key) {
        redisTemplate.persist(key);
    }

    /**
     * lock the operation with the default time, the default lock time is 60 seconds
     *
     * @param key the given key
     * @return
     */
    public boolean lock(String key) {
        return lock(key, DEFAULT_LOCK_TIME);
    }

    /**
     * lock the operation with the given key
     *
     * @param key      the given key
     * @param lockTime unit is milliseconds, the lock time should greater than estimated operation time
     * @return true if lock success
     */
    public boolean lock(String key, long lockTime) {
        BoundValueOperations<String, Object> operations = redisTemplate.boundValueOps(key);
        boolean lockSuccess = operations.setIfAbsent(LOCK);
        if (lockSuccess) {
            operations.expire(lockTime, TimeUnit.MILLISECONDS);
        }

        return lockSuccess;

//    RedisConnection connection = null;
//    try {
//      connection = stringRedisTemplate.getConnectionFactory().getConnection();
//      lockSuccess = connection.setNX(key.getBytes(Charset.forName("UTF-8")), LOCK.getBytes(Charset.forName("UTF-8")));
//      if(lockSuccess) {
//        connection.expire(key.getBytes(Charset.forName("UTF-8")), lockTime);
//      }
//    } finally {
//      connection.close();
//    }
    }

    /**
     * unlock the operation with the given key
     *
     * @param key
     */
    public void unlock(String key) {
        redisTemplate.delete(key);
    }


    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public String get(final String key) {
        Object result = null;
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        if (result == null) {
            return null;
        }
        return result.toString();
    }


    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param expireTime 分钟
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 操作set集合，增加数据
     *
     * @param key
     * @param value
     */
    public Long addSet(String key, String value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.add(key, value);
    }

    /**
     * 操作set集合，去除部分数据
     *
     * @param key
     * @param value
     */
    public Long subtractSet(String key, String value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.remove(key, value);
    }

    /**
     * 查询set集合
     *
     * @param key
     */
    public Set<Object> findSet(String key) {
        Set<Object> resultSet = redisTemplate.opsForSet().members(key);
        return resultSet;
    }

    /**
     * 操作set集合，查询数据
     *
     * @param key
     */
    public Set<String> findJsonStringSet(String key) {
        Set<String> resultSet = StringRedisTemplate.opsForSet().members(key);
        return resultSet;
    }


    /**
     * 操作set集合，增加数据
     *
     * @param key
     * @param value
     */
    public void addStrSet(String key, String value) {
        StringRedisTemplate.opsForSet().add(key, value);
    }
}
