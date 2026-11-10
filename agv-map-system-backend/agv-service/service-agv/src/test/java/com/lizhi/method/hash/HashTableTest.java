package com.lizhi.method.hash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("哈希表测试")
class HashTableTest {

    private HashTable hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable();
    }

    @Test
    @DisplayName("测试基本的put和get操作")
    void testBasicPutAndGet() {
        hashTable.put(1, "key1", "value1");
        assertEquals("value1", hashTable.get(1, "key1"));

        hashTable.put(2, "key2", "value2");
        assertEquals("value2", hashTable.get(2, "key2"));
    }

    @Test
    @DisplayName("测试更新已存在的key")
    void testUpdateExistingKey() {
        hashTable.put(1, "key1", "value1");
        assertEquals("value1", hashTable.get(1, "key1"));

        hashTable.put(1, "key1", "newValue");
        assertEquals("newValue", hashTable.get(1, "key1"));
    }

    @Test
    @DisplayName("测试获取不存在的key返回null")
    void testGetNonExistentKey() {
        assertNull(hashTable.get(1, "nonExistentKey"));
    }

    @Test
    @DisplayName("测试多个键值对存储")
    void testMultipleEntries() {
        hashTable.put(1, 1, "one");
        hashTable.put(2, 2, "two");
        hashTable.put(3, 3, "three");
        hashTable.put(4, 4, "four");
        hashTable.put(5, 5, "five");

        assertEquals("one", hashTable.get(1, 1));
        assertEquals("two", hashTable.get(2, 2));
        assertEquals("three", hashTable.get(3, 3));
        assertEquals("four", hashTable.get(4, 4));
        assertEquals("five", hashTable.get(5, 5));
    }

    @Test
    @DisplayName("测试哈希冲突处理")
    void testHashCollision() {
        int hash1 = 1;
        int hash2 = 9;
        
        hashTable.put(hash1, "collision1", "value1");
        hashTable.put(hash2, "collision2", "value2");

        assertEquals("value1", hashTable.get(hash1, "collision1"));
        assertEquals("value2", hashTable.get(hash2, "collision2"));
    }

    @Test
    @DisplayName("测试扩容功能")
    void testResize() {
        for (int i = 0; i < 10000; i++) {
            hashTable.put(i, "key" + i, "value" + i);
        }

        for (int i = 0; i < 10000; i++) {
            assertEquals("value" + i, hashTable.get(i, "key" + i), 
                "扩容后应该能正确获取key" + i);
        }
    }

    @Test
    @DisplayName("测试使用不同对象作为key")
    void testDifferentKeyTypes() {
        hashTable.put("string".hashCode(), "string", "stringValue");
        hashTable.put(Integer.valueOf(123).hashCode(), 123, "intValue");

        assertEquals("stringValue", hashTable.get("string".hashCode(), "string"));
        assertEquals("intValue", hashTable.get(Integer.valueOf(123).hashCode(), 123));
    }

    @Test
    @DisplayName("测试覆盖相同hash不同key的情况")
    void testSameHashDifferentKeys() {
        String key1 = "Aa";
        String key2 = "BB";
        
        hashTable.put(key1.hashCode(), key1, "valueAa");
        hashTable.put(key2.hashCode(), key2, "valueBB");

        assertEquals("valueAa", hashTable.get(key1.hashCode(), key1));
        assertEquals("valueBB", hashTable.get(key2.hashCode(), key2));
    }

    @Test
    @DisplayName("测试大量数据插入和查询")
    void testBulkOperations() {
        int count = 100;
        for (int i = 0; i < count; i++) {
            hashTable.put(i, i, "value" + i);
        }

        for (int i = 0; i < count; i++) {
            assertEquals("value" + i, hashTable.get(i, i));
        }
    }

    @Test
    @DisplayName("测试null值存储")
    void testNullValue() {
        hashTable.put(1, "key", null);
        assertNull(hashTable.get(1, "key"));
    }
}
