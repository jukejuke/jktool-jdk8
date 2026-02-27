package io.github.jukejuke.tool.id;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ID生成工具类
 * 提供多种ID生成策略：UUID、雪花算法、时间戳、自增ID等
 */
@Slf4j
public class IdGenerator {

    /**
     * 雪花算法参数
     */
    private static final long EPOCH = 1609459200000L; // 2021-01-01 00:00:00
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private final long workerId;
    private final long datacenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    /**
     * 自增ID计数器
     */
    private static final AtomicLong incrementId = new AtomicLong(0);

    /**
     * 单例实例
     */
    private static class SingletonHolder {
        private static final IdGenerator INSTANCE = new IdGenerator(1, 1);
    }

    /**
     * 私有构造方法
     * @param workerId 工作节点ID
     * @param datacenterId 数据中心ID
     */
    private IdGenerator(long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("工作节点ID必须在0到%s之间", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("数据中心ID必须在0到%s之间", MAX_DATACENTER_ID));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        log.info("初始化IdGenerator: workerId={}, datacenterId={}", workerId, datacenterId);
    }

    /**
     * 获取单例实例
     * @return IdGenerator实例
     */
    public static IdGenerator getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 生成UUID（带连字符）
     * @return UUID字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成UUID（不带连字符）
     * @return 无连字符的UUID字符串
     */
    public static String generateUUIDWithoutDash() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成雪花算法ID
     * @return 雪花算法ID
     */
    public synchronized long generateSnowflakeId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            log.error("时钟回拨，当前时间戳: {}, 上次时间戳: {}", timestamp, lastTimestamp);
            throw new RuntimeException("时钟回拨异常");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT) 
                | (datacenterId << DATACENTER_ID_SHIFT) 
                | (workerId << WORKER_ID_SHIFT) 
                | sequence;
    }

    /**
     * 等待到下一个毫秒
     * @param lastTimestamp 上次时间戳
     * @return 新的时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 生成时间戳ID（毫秒级）
     * @return 时间戳ID
     */
    public static long generateTimestampId() {
        return System.currentTimeMillis();
    }

    /**
     * 生成时间戳ID（秒级）
     * @return 时间戳ID
     */
    public static long generateTimestampSecondId() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 生成自增ID
     * @return 自增ID
     */
    public static long generateIncrementId() {
        return incrementId.incrementAndGet();
    }

    /**
     * 生成带前缀的自增ID
     * @param prefix 前缀
     * @return 带前缀的自增ID
     */
    public static String generateIncrementIdWithPrefix(String prefix) {
        return prefix + incrementId.incrementAndGet();
    }

    /**
     * 重置自增ID计数器
     */
    public static void resetIncrementId() {
        incrementId.set(0);
        log.info("重置自增ID计数器");
    }

    /**
     * 生成随机ID（指定长度）
     * @param length ID长度
     * @return 随机ID
     */
    public static String generateRandomId(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("ID长度必须大于0");
        }
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
