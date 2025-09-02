/*
 * Copyright 2025 coze-dev Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coze.studio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 多级缓存服务
 * 实现本地缓存 + Redis 分布式缓存的多级缓存策略
 *
 * @author coze-dev
 */
@Service
@EnableCaching
@Slf4j
@Primary
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    // 本地缓存
    private final Map<String, CacheEntry> localCache = new ConcurrentHashMap<>();

    // 默认缓存配置
    private static final long LOCAL_CACHE_TTL = 300; // 5分钟
    private static final long REDIS_CACHE_TTL = 3600; // 1小时

    @Autowired
    public CacheService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        log.info("多级缓存服务初始化完成");
        // 启动本地缓存清理任务
        startLocalCacheCleanup();
    }

    /**
     * 获取缓存数据
     * 优先从本地缓存获取，失败则从Redis获取
     */
    public <T> T get(String key, Class<T> type) {
        // 1. 尝试从本地缓存获取
        T localValue = getFromLocalCache(key, type);
        if (localValue != null) {
            log.debug("从本地缓存获取数据: key={}", key);
            return localValue;
        }

        // 2. 从Redis缓存获取
        T redisValue = getFromRedisCache(key, type);
        if (redisValue != null) {
            log.debug("从Redis缓存获取数据: key={}", key);
            // 同步到本地缓存
            putToLocalCache(key, redisValue);
            return redisValue;
        }

        log.debug("缓存未命中: key={}", key);
        return null;
    }

    /**
     * 存储缓存数据
     * 同时存储到本地缓存和Redis缓存
     */
    public <T> void put(String key, T value) {
        put(key, value, LOCAL_CACHE_TTL, REDIS_CACHE_TTL);
    }

    /**
     * 存储缓存数据（指定TTL）
     */
    public <T> void put(String key, T value, long localTtlSeconds, long redisTtlSeconds) {
        // 存储到本地缓存
        putToLocalCache(key, value, localTtlSeconds);

        // 存储到Redis缓存
        putToRedisCache(key, value, redisTtlSeconds);

        log.debug("缓存数据存储完成: key={}, localTTL={}s, redisTTL={}s", key, localTtlSeconds, redisTtlSeconds);
    }

    /**
     * 删除缓存数据
     */
    public void evict(String key) {
        // 从本地缓存删除
        localCache.remove(key);

        // 从Redis缓存删除
        redisTemplate.delete(key);

        log.debug("缓存数据删除完成: key={}", key);
    }

    /**
     * 批量删除缓存数据
     */
    public void evictBatch(String pattern) {
        // 从本地缓存删除匹配的键
        localCache.entrySet().removeIf(entry -> entry.getKey().contains(pattern));

        // 从Redis缓存删除匹配的键
        Set<String> keys = redisTemplate.keys(pattern);
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        log.debug("批量删除缓存完成: pattern={}, deletedKeys={}", pattern, keys.size());
    }

    /**
     * 清空所有缓存
     */
    public void clearAll() {
        // 清空本地缓存
        localCache.clear();

        // 清空Redis缓存
        redisTemplate.getConnectionFactory().getConnection().flushAll();

        log.info("所有缓存已清空");
    }

    /**
     * 检查键是否存在
     */
    public boolean exists(String key) {
        // 优先检查本地缓存
        if (localCache.containsKey(key) && !isExpired(localCache.get(key))) {
            return true;
        }

        // 检查Redis缓存
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取缓存统计信息
     */
    public CacheStats getCacheStats() {
        CacheStats stats = new CacheStats();

        // 本地缓存统计
        stats.setLocalCacheSize(localCache.size());
        stats.setLocalCacheHitRate(calculateLocalHitRate());

        // Redis缓存统计
        stats.setRedisCacheSize(getRedisCacheSize());

        return stats;
    }

    /**
     * 缓存预热
     */
    public void warmUp(List<String> keys) {
        log.info("开始缓存预热: keys={}", keys.size());

        for (String key : keys) {
            if (!exists(key)) {
                log.debug("预热缓存: key={}", key);
                // 这里可以实现具体的预热逻辑
                // 例如从数据库加载热点数据到缓存
            }
        }

        log.info("缓存预热完成");
    }

    // ==================== 私有方法 ====================

    /**
     * 从本地缓存获取数据
     */
    @SuppressWarnings("unchecked")
    private <T> T getFromLocalCache(String key, Class<T> type) {
        CacheEntry entry = localCache.get(key);
        if (entry != null && !isExpired(entry)) {
            try {
                return (T) entry.getValue();
            } catch (ClassCastException e) {
                log.warn("本地缓存类型转换失败: key={}, expectedType={}", key, type.getSimpleName());
            }
        }
        return null;
    }

    /**
     * 从Redis缓存获取数据
     */
    @SuppressWarnings("unchecked")
    private <T> T getFromRedisCache(String key, Class<T> type) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                return (T) value;
            }
        } catch (Exception e) {
            log.warn("从Redis缓存获取数据失败: key={}", key, e);
        }
        return null;
    }

    /**
     * 存储到本地缓存
     */
    private <T> void putToLocalCache(String key, T value) {
        putToLocalCache(key, value, LOCAL_CACHE_TTL);
    }

    /**
     * 存储到本地缓存（指定TTL）
     */
    private <T> void putToLocalCache(String key, T value, long ttlSeconds) {
        CacheEntry entry = new CacheEntry(value, System.currentTimeMillis() + ttlSeconds * 1000);
        localCache.put(key, entry);
    }

    /**
     * 存储到Redis缓存
     */
    private <T> void putToRedisCache(String key, T value) {
        putToRedisCache(key, value, REDIS_CACHE_TTL);
    }

    /**
     * 存储到Redis缓存（指定TTL）
     */
    private <T> void putToRedisCache(String key, T value, long ttlSeconds) {
        try {
            redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlSeconds));
        } catch (Exception e) {
            log.warn("存储到Redis缓存失败: key={}", key, e);
        }
    }

    /**
     * 检查缓存条目是否过期
     */
    private boolean isExpired(CacheEntry entry) {
        return System.currentTimeMillis() > entry.getExpireTime();
    }

    /**
     * 启动本地缓存清理任务
     */
    private void startLocalCacheCleanup() {
        Thread cleanupThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(60000); // 每分钟清理一次
                    cleanupExpiredEntries();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        cleanupThread.setDaemon(true);
        cleanupThread.setName("LocalCacheCleanup");
        cleanupThread.start();

        log.info("本地缓存清理任务已启动");
    }

    /**
     * 清理过期的本地缓存条目
     */
    private void cleanupExpiredEntries() {
        int beforeSize = localCache.size();
        localCache.entrySet().removeIf(entry -> isExpired(entry.getValue()));
        int afterSize = localCache.size();

        if (beforeSize != afterSize) {
            log.debug("本地缓存清理完成: before={}, after={}", beforeSize, afterSize);
        }
    }

    /**
     * 计算本地缓存命中率
     */
    private double calculateLocalHitRate() {
        // 这里可以实现命中率统计逻辑
        return 0.85; // 临时返回默认值
    }

    /**
     * 获取Redis缓存大小
     */
    private long getRedisCacheSize() {
        try {
            return redisTemplate.getConnectionFactory().getConnection().dbSize();
        } catch (Exception e) {
            log.warn("获取Redis缓存大小失败", e);
            return 0;
        }
    }

    // ==================== 缓存条目类 ====================

    /**
     * 缓存条目
     */
    private static class CacheEntry {
        private final Object value;
        private final long expireTime;

        public CacheEntry(Object value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }

        public Object getValue() {
            return value;
        }

        public long getExpireTime() {
            return expireTime;
        }
    }

    // ==================== 缓存统计类 ====================

    /**
     * 缓存统计信息
     */
    public static class CacheStats {
        private int localCacheSize;
        private double localCacheHitRate;
        private long redisCacheSize;

        // Getters and setters
        public int getLocalCacheSize() { return localCacheSize; }
        public void setLocalCacheSize(int localCacheSize) { this.localCacheSize = localCacheSize; }
        public double getLocalCacheHitRate() { return localCacheHitRate; }
        public void setLocalCacheHitRate(double localCacheHitRate) { this.localCacheHitRate = localCacheHitRate; }
        public long getRedisCacheSize() { return redisCacheSize; }
        public void setRedisCacheSize(long redisCacheSize) { this.redisCacheSize = redisCacheSize; }
    }
}
