package com.meiqiu.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Description 异步接口
 * @Author sgh
 * @Date 2025/1/21
 * @Time 14:16
 */
@Service
@Slf4j
public class AsyncService {

    @Async("restExecutor")
    public void executeAsyncTask1() {
        // 异步执行的任务
        for (int i = 0; i < 100; i++) {
            log.info("executeAsyncTask-1:{}", i);
        }
    }

    @Async("restExecutor")
    public void executeAsyncTask2() {
        // 异步执行的任务
        for (int i = 0; i < 100; i++) {
            log.info("executeAsyncTask-2:{}", i);
        }
    }
}
