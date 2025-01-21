package com.meiqiu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description 线程池配置
 * @Author sgh
 * @Date 2025/1/21
 * @Time 14:10
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    //声明一个名称为 restExecutor 的线程池
    @Bean(name = "restExecutor")
    public Executor threadPoolTaskExecutor() {
        //ThreadPoolExecutor 创建
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数：线程池的核心线程数，即使线程池处于空闲状态也会保持存在的线程数量。但是当将allowCoreThreadTimeout设置为true时，核心线程超时也会回收
        executor.setCorePoolSize(4);
        //最大线程数：线程池允许的最大线程数
        executor.setMaxPoolSize(8);
        //队列大小
        executor.setQueueCapacity(100);
        //线程空闲时间：非核心线程闲置时的存活时间。如果线程池中的线程数量超过 corePoolSize 且当前线程空闲时间超过 keepAliveTime，该线程将被终止。单位默认为秒
        executor.setKeepAliveSeconds(60);
        //线程名前缀
        executor.setThreadNamePrefix("RestExecutor-");
        //拒绝策略：当线程池已满且阻塞队列也已满时采取的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //当任务队列满时使用调用者的线程直接执行该任务。
        executor.initialize();
        return executor;
    }
}
