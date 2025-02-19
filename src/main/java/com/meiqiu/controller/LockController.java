package com.meiqiu.controller;

import com.meiqiu.config.RedisLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @Description
 * @Author sgh
 * @Date 2025/2/19
 * @Time 17:46
 */
@RestController
@RequestMapping("/lock")
@Api(tags = "分布式锁")
public class LockController {


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisLock redisLock;

    @ApiOperation(value = "扣减库存")
    @RequestMapping(value = "/deductStock", method = RequestMethod.POST)
    @ResponseBody
    public String deductStock() {
        String id = "1";
        //获取锁 key
        String lockKey = "product" + id;
        String goodsKey = "goods_" + id;
        String requestId = lockKey + "-" + Thread.currentThread().getId();
        boolean locked = redisLock.lock(lockKey, requestId, 10);
        if (!locked) {
            return "线程：" + Thread.currentThread().getId() + "请稍候再试";
        }
        //获取库存
        if (redisTemplate.opsForValue().get(goodsKey) == null) {
            redisTemplate.opsForValue().set(goodsKey, 100);
        }
        int stock = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(goodsKey)).toString());
        int currentStock = stock - 1;
        if (currentStock < 0) {
            redisLock.unlock2(lockKey, requestId);
            return "线程：" + Thread.currentThread().getId() + "商品：" + goodsKey + "库存不足";
        }
        redisTemplate.opsForValue().set(goodsKey, currentStock);
        redisLock.unlock2(lockKey, requestId);
        return "线程：" + Thread.currentThread().getId() + "商品：" + goodsKey + "扣减库存成功，剩余：" + currentStock;
    }


    @ApiOperation(value = "扣减库存")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public void test() {
        //多线程扣件库存
        new Thread(() -> {
            for (int i = 0; i < 11; i++) {
                System.out.println(deductStock());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                System.out.println(deductStock());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                System.out.println(deductStock());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
