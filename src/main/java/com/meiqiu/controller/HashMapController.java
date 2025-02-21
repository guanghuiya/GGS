package com.meiqiu.controller;

import cn.hutool.bloomfilter.bitMap.BitMap;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author sgh
 * @Date 2025/2/21
 * @Time 09:59
 */
public class HashMapController {
    public static void main(String[] args) {
        /**
         * 多线程环境下不安全
         * key和value都允许为null，但是为null的key只能有一个
         * 实现了Serializable接口，支持序列化，实现了Cloneable接口，能被克隆
         * 初始大小16，扩容因子0.75，每次扩容原来的两倍
         * HashMap去掉了contains方法，改成了 containsKey 和 containsValue
         */
        Map<String,Object> hashMap = new HashMap<>();

        /**
         * 方法都实现了synchronized，多线程环境下线程安全
         * key和value都不允许为null
         * 实现了Serializable接口，支持序列化，实现了Cloneable接口，能被克隆
         * 初始大小11，扩容因子0.75，每次扩容为原来的2倍加1
         */
        Map<String,Object> hashTable = new Hashtable<>();

        /**
         * key 不允许为 null
         * 在 Java 8 及以后的版本中，ConcurrentHashMap 使用了基于 CAS 的乐观锁机制和 synchronized 关键字来保证线程安全。每个桶（bucket）可以独立地被锁定和解锁，从而减少锁的粒度，提高并发性能。
         * 使用volatile关键字保证是从主内存读到的数据，锁的位置是每个哈希位置的链表头节点，降低锁冲突的频率
         * cas：无锁原子操作
         */
        ConcurrentHashMap<String,Object> concurrentHashMap = new ConcurrentHashMap<>();

        BitMap bitMap = new BitMap() {
            @Override
            public void add(long l) {

            }

            @Override
            public boolean contains(long l) {
                return false;
            }

            @Override
            public void remove(long l) {

            }
        };





    }
}
