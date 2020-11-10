package util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 分布式自增长 ID。
 * Twitter 的 Snowflake JAVA 实现方案。
 * 核心代码为其 IdWorker 这个类实现，其原理结构如下，我分别用一个 0 表示一位，用 — 分割开部分的作用：
 * 1||0---0000000000 0000000000 0000000000 0000000000 0 --- 00000 --- 00000 --- 000000000000
 * 在上面的字符串中，第一位为未使用（实际上也可作为 long 的符号位），接下来的 41 位为毫秒级时间，
 * 然后 5 位 datacenter 标识位，5 位机器 ID（并不算标识符，实际是为线程标识），
 * 然后 12 位该毫秒内的当前毫秒内的计数，加起来刚好 64 位，为一个 Long 型。
 * 这样的好处是，整体上按照时间自增排序，并且整个分布式系统内不会产生 ID 碰撞（由 datacenter 和机器 ID 作区分），
 * 并且效率较高，经测试，snowflake 每秒能够产生 26 万 ID 左右，完全满足需要。
 * <p>
 * 64 位 ID（42(毫秒) + 5(机器 ID) + 5(业务编码) + 12(重复累加)）。
 */
public class IdWorker {
    // 时间起始标记点，作为基准，一般取系统的最近时间（一旦确定不能变动）。
    private final static long twepoch = 1605009305507L;
    // 机器标识位数。
    private final static long workerIdBits = 5L;
    // 数据中心标识位数。
    private final static long datacenterIdBits = 5L;
    // 机器 ID 最大值。
    private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // 数据中心 ID 最大值。
    private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // 毫秒内自增位。
    private final static long sequenceBits = 12L;
    // 机器 ID 偏左移 12 位。
    private final static long workerIdShift = sequenceBits;
    // 数据中心 ID 左移 17 位。
    private final static long datacenterIdShift = sequenceBits + workerIdBits;
    // 时间毫秒左移 22 位。
    private final static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private final static long sequenceMask = -1L ^ (-1L << sequenceBits);
    // 上次生产 id 时间戳。
    private static long lastTimestamp = -1L;
    private final long workerId;
    // 数据标识 id 部分。
    private final long datacenterId;
    // 0，并发控制。
    private long sequence = 0L;

    public IdWorker() {
        this.datacenterId = getDatacenterId(maxDatacenterId);
        this.workerId = getMaxWorkerId(datacenterId, maxWorkerId);
    }

    /**
     * @param workerId     工作机器 ID。
     * @param datacenterId 序列号。
     */
    public IdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获取 maxWorkerId。
     */
    protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuffer mpid = new StringBuffer();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            /*
             * GET jvmPid
             */
            mpid.append(name.split("@")[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取 16 个低位。
         */
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /**
     * 数据标识 id 部分。
     */
    protected static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                id = ((0x000000FF & (long) mac[mac.length - 1])
                        | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                id = id % (maxDatacenterId + 1);
            }
        } catch (Exception e) {
            System.out.println(" getDatacenterId: " + e.getMessage());
        }
        return id;
    }

    /**
     * 获取下一个 ID。
     *
     * @return
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            // 当前毫秒内，则 +1。
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                // 当前毫秒内计数满了，则等待下一秒。
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        // ID。偏移组合生成最终的 ID，并返回 ID。
        long nextId = ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;
        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

}
