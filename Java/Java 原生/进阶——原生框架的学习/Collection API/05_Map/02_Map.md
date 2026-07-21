# Map

`Map` => 地图。

`Map` 集合正如其名，在地图上一个名字就代表了一座城市，当然这只是一个比喻并不能完全代表 `Map` 的特性。

`Map` 集合存储数据的的方式是键值对，键被称作 `key` ，值称作 `value` ，记作：`{key: value}` 。

键是不允许有重复的，在 `Set` 集合接口一节中有讲述到：`Set` 集合接口的不重复特性是通过 `hashCode()、equals()` 来实现的，另外 `HashSet` 的底层是通过 `HashMap` 实现的，将 `HashSet` 所需要存储的元素作为键存入至 `HashMap` 中，而每一个键的值均是 `HashSet` 中的一个静态常量 `Object` 对象。

下面进入 `Map` 接口源码的学习。

本节的内容中，开发者无需关心 `Map` 接口的具体实现（后续会讲述），对于本节内容开发者只需要知道 `Map` 接口有哪些方法，这些方法分别是干什么的即可（面向接口编程），这些方法后续开发者会经常使用。

## 源码

```java
package java.util;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

// Map 是泛型接口，K 表示键的类型，V 表示值的类型
public interface Map<K, V> {
    
    /**
    * 返回集合长度
    * return: int 类型
    */
    int size();

    /**
    * 判断集合是否为空
    * return: boolean 类型
    */
    boolean isEmpty();

    /**
    * 判断集合是否包含某个 key（键）
    * return: boolean 类型
    * throws:
    *		ClassCastException: 类型转换错误
    * 		NullPointException: 空指针错误
    */
    boolean containsKey(Object key);

    /**
    * 判断集合是否包含某个 key（键）
    * return: boolean 类型
    * throws:
    * 		ClassCastException: 类型转换错误
    * 		NullPointException: 空指针错误
    */
    boolean containsValue(Object value);

    /**
    * 根据 key（键） 获取 value（值）
    * return: value 的类型
    * throws:
    * 		ClassCastException: 类型转换错误
    * 		NullPointException: 空指针错误
    */
    V get(Object key);

    /**
    * 存入 key（键） value（值）对
    * return: value 的类型
    * throws:
    * 		UnsupportedOperationException: Map 实例不支持 put()
    * 		ClassCastException: 类型转换错误
    * 		NullPointException: 空指针错误
    *		IllegalArgumentException: 存入的键值对中的属性类型不允许该键值对存入其中
    */
    V put(K key, V value);

    /**
    * 根据 key（键）删除对应键值对
    * return: value 的类型
    * throws:
    * 		UnsupportedOperationException: Map 实例不支持 remove()
    * 		ClassCastException: 类型转换错误
    * 		NullPointException: 空指针错误
    */
    V remove(Object key);

   	/**
    * 将另一个 Map 的所有键值对复制到当前 Map 中
    * return: value 的类型
    * throws:
    * 		UnsupportedOperationException: Map 实例不支持 putAll()
    * 		ClassCastException: 类型转换错误
    * 		NullPointException: 空指针错误
    *		IllegalArgumentException: 存入的键值对中的属性类型不允许该键值对存入其中
    */
    void putAll(Map<? extends K, ? extends V> m);

    /**
    * 清空 Map 集合
    * throws:
    * 		UnsupportedOperationException: Map 实例不支持 clear()
    */
    void clear();

    /**
    * 返回所有键（key）组成的 Set 集合（视图：修改 Set 会影响原 Map）
 	*/
    Set<K> keySet();

    /**
    * 返回所有值（value）组成的 Collection 集合（视图：修改 Collection 会影响原 Map）
 	*/
    Collection<V> values();

    /**
    * 返回所有键值对（Entry）组成的 Set 集合（视图：修改 Entry 会影响原 Map）
 	*/
    Set<Map.Entry<K, V>> entrySet();

    /***** 内部接口（该接口主要用于封装单个键值（key: value）对） *****/
    interface Entry<K, V> {
        // 获取当前 Entry 的键
        K getKey();

        // 获取当前 Entry 的值
        V getValue();

        // 修改当前 Entry 的值，返回旧值
        V setValue(V value);

        // 判断两个 Entry 是否相等（键和值都相等）
        boolean equals(Object o);

        // 返回 Entry 的哈希值（key.hashCode() ^ value.hashCode()）
        int hashCode();

        // 默认方法，按键排序
        public static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K, V>> comparingByKey() {
            return (c1, c2) -> c1.getKey().compareTo(c2.getKey());
        }

        // 默认方法，按值排序
        public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> comparingByValue() {
            return (c1, c2) -> c1.getValue().compareTo(c2.getValue());
        }
    }

    /***** 相等性判断与哈希值 *****/
    boolean equals(Object o);
    int hashCode();

    // 获取键对应的值，若键不存在则返回默认值 defaultValue
    default V getOrDefault(Object key, V defaultValue) {
        V v;
        return (((v = get(key)) != null) || containsKey(key)) ? v : defaultValue;
    }

    // 遍历所有键值对，执行指定的操作（BiConsumer 是函数式接口）
    default void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<K, V> entry : entrySet()) {
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                // 针对并发修改的异常
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }
}
```

## 使用示例

```java
package Java_Protogenetic_API.Map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName Test01
 * @Description TODO: Map 接口的学习
 * @Author 34194
 * @DATE 2025/12/21 10:10
 * @Version 1.0
 */
public class Test01 {
    public static void main(String[] args) {

        Map<Integer, String> map = new HashMap<>();

        map.put(1, "我是第 1 个元素");
        map.put(2, "我是第 2 个元素");
        map.put(3, "我是第 3 个元素");
        map.put(4, "我是第 4 个元素");
        map.put(5, "我是第 5 个元素");

        System.out.println(map);
        System.out.println("map 集合是否为空：" + (map.isEmpty() ? "YES" : "NO"));
        System.out.println("map 集合长度：" + map.size());
        System.out.println("map 的键为 2 的值：" + map.get(2));
        System.out.println("map 中是否包含值为我第 5 个元素的键：" + (map.containsValue("我是第 5 个元素") ? "YES" : "NO"));
        System.out.println("删除键为 5 的元素：" + map.remove(5) + "已被删除");
        Set<Integer> set= map.keySet();
        System.out.println("map 中所有的键：" + set);
        Collection<String> collection = map.values();
        System.out.println("map 中所有的值：" + collection);

        map.forEach((index, value)-> System.out.println(index + ": " + value));

    }
}
```

## 注意事项

1. **键的类型要求**：作为 Key 的类必须重写 `equals()` 和 `hashCode()` 方法（String、Integer 等包装类已默认实现），否则 `HashMap` 会无法正确判断键的唯一性，导致内存泄漏或查询异常。
2. **线程安全问题**：`HashMap` 线程不安全。
3. **null 值处理**：`HashMap` 允许键和值为 null，`TreeMap` 允许值为 null，但键不能为 null（因为需要排序），`Hashtable` 不允许键和值为 null。
