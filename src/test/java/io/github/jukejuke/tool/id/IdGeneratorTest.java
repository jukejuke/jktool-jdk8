package io.github.jukejuke.tool.id;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IdGenerator测试类
 */
class IdGeneratorTest {

    @Test
    void testGenerateUUID() {
        String uuid = IdGenerator.generateUUID();
        assertNotNull(uuid);
        assertEquals(36, uuid.length());
        assertTrue(uuid.contains("-"));
        System.out.println("UUID: " + uuid);
    }

    @Test
    void testGenerateUUIDWithoutDash() {
        String uuid = IdGenerator.generateUUIDWithoutDash();
        assertNotNull(uuid);
        assertEquals(32, uuid.length());
        assertFalse(uuid.contains("-"));
        System.out.println("UUID (无连字符): " + uuid);
    }

    @Test
    void testGenerateSnowflakeId() {
        IdGenerator generator = IdGenerator.getInstance();
        long id1 = generator.generateSnowflakeId();
        long id2 = generator.generateSnowflakeId();
        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);
        System.out.println("雪花算法ID 1: " + id1);
        System.out.println("雪花算法ID 2: " + id2);
    }

    @Test
    void testGenerateTimestampId() {
        long timestamp1 = IdGenerator.generateTimestampId();
        long timestamp2 = IdGenerator.generateTimestampId();
        assertNotNull(timestamp1);
        assertNotNull(timestamp2);
        // 可能在同一毫秒，所以不断言不相等
        System.out.println("时间戳ID 1: " + timestamp1);
        System.out.println("时间戳ID 2: " + timestamp2);
    }

    @Test
    void testGenerateTimestampSecondId() {
        long timestamp1 = IdGenerator.generateTimestampSecondId();
        long timestamp2 = IdGenerator.generateTimestampSecondId();
        assertNotNull(timestamp1);
        assertNotNull(timestamp2);
        // 可能在同一秒，所以不断言不相等
        System.out.println("秒级时间戳ID 1: " + timestamp1);
        System.out.println("秒级时间戳ID 2: " + timestamp2);
    }

    @Test
    void testGenerateIncrementId() {
        IdGenerator.resetIncrementId();
        long id1 = IdGenerator.generateIncrementId();
        long id2 = IdGenerator.generateIncrementId();
        long id3 = IdGenerator.generateIncrementId();
        assertNotNull(id1);
        assertNotNull(id2);
        assertNotNull(id3);
        assertEquals(1, id1);
        assertEquals(2, id2);
        assertEquals(3, id3);
        System.out.println("自增ID 1: " + id1);
        System.out.println("自增ID 2: " + id2);
        System.out.println("自增ID 3: " + id3);
    }

    @Test
    void testGenerateIncrementIdWithPrefix() {
        IdGenerator.resetIncrementId();
        String id1 = IdGenerator.generateIncrementIdWithPrefix("USER_");
        String id2 = IdGenerator.generateIncrementIdWithPrefix("USER_");
        assertNotNull(id1);
        assertNotNull(id2);
        assertTrue(id1.startsWith("USER_"));
        assertTrue(id2.startsWith("USER_"));
        assertNotEquals(id1, id2);
        System.out.println("带前缀自增ID 1: " + id1);
        System.out.println("带前缀自增ID 2: " + id2);
    }

    @Test
    void testResetIncrementId() {
        IdGenerator.generateIncrementId();
        IdGenerator.generateIncrementId();
        IdGenerator.resetIncrementId();
        long id = IdGenerator.generateIncrementId();
        assertEquals(1, id);
        System.out.println("重置后自增ID: " + id);
    }

    @Test
    void testGenerateRandomId() {
        String id1 = IdGenerator.generateRandomId(10);
        String id2 = IdGenerator.generateRandomId(10);
        assertNotNull(id1);
        assertNotNull(id2);
        assertEquals(10, id1.length());
        assertEquals(10, id2.length());
        assertNotEquals(id1, id2);
        System.out.println("随机ID 1: " + id1);
        System.out.println("随机ID 2: " + id2);
    }

    @Test
    void testGenerateRandomIdWithInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            IdGenerator.generateRandomId(0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            IdGenerator.generateRandomId(-1);
        });
    }

    @Test
    void testSnowflakeIdUniqueness() {
        IdGenerator generator = IdGenerator.getInstance();
        long[] ids = new long[1000];
        for (int i = 0; i < 1000; i++) {
            ids[i] = generator.generateSnowflakeId();
        }
        // 检查是否有重复ID
        for (int i = 0; i < ids.length; i++) {
            for (int j = i + 1; j < ids.length; j++) {
                assertNotEquals(ids[i], ids[j], "雪花算法ID重复: " + ids[i]);
            }
        }
        System.out.println("雪花算法ID唯一性测试通过，生成了" + ids.length + "个唯一ID");
    }
}
