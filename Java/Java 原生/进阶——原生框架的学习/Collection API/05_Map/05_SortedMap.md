# SortedMap

在学习 `SortedMap` 之前，开发者可以回忆一下 *04_SortedSet*  一节的内容，二者有部分相同的特性——规则有序（根据开发自定义排序规则，默认自然排序）。

`SortedMap` 是一个接口，继承了 `Map` 。

本节学习内容：

1. 了解 `SortedMap` 以及 `SortedMap` 特性。
2. 掌握 `SortedMap` 日常开发常用方法。
3. 熟知 `SortedMap` 是如何让元素规则有序的。
4. 在实际开发时，能够合理使用 `SortedMap` 进行开发。

## 源码

```java
public interface SortedMap<K,V> extends Map<K,V> {
   
    /**
     * 获取用于排序键的比较器
     * @return 自定义比较器；若键使用自然顺序排序则返回null
     */
    Comparator<? super K> comparator();

    
    /**
     * 截取区间子Map：包含 fromKey，不包含 toKey [fromKey, toKey)
     * 子 Map 与原 Map 底层数据互通，原 Map 修改会同步影响子 Map
     * @param fromKey 区间起始键（包含）
     * @param toKey 区间终止键（不包含）
     * @return 指定键区间范围内的有序子SortedMap
     */
    SortedMap<K,V> subMap(K fromKey, K toKey);

    
    /**
     * 截取头部子 Map：所有键小于toKey [min, toKey)
     * @param toKey 区间边界键（不包含）
     * @return 所有键小于 toKey 的有序子 SortedMap
     */
    SortedMap<K,V> headMap(K toKey);

    /**
     * 截取尾部子 Map：所有键大于等于 fromKey [fromKey, max]
     * @param fromKey 区间起始键（包含）
     * @return 所有键大于等于 fromKey 的有序子 SortedMap
     */
    SortedMap<K,V> tailMap(K fromKey);

    /**
     * 获取当前 Map 中最小的键（排序第一位）
     * @return 排序后的第一个键
     * @throws NoSuchElementException 若Map为空时抛出
     */
    K firstKey();

    /**
     * 获取当前 Map 中最大的键（排序最后一位）
     * @return 排序后的最后一个键
     * @throws NoSuchElementException 若Map为空时抛出
     */
    K lastKey();

   
    /**
     * 返回所有键组成的有序 Set 集合，顺序与 SortedMap 键排序规则一致
     * 该集合与原 Map 联动，删除元素会同步修改原映射
     * @return 有序键集合
     */
    Set<K> keySet();

    
    /**
     * 返回所有值组成的有序 Collection，值顺序跟随对应键的排序顺序
     * 集合与原 Map 联动，删除元素会同步修改原映射
     * @return 有序值集合
     */
    Collection<V> values();

   
    /**
     * 返回键值对Entry实体组成的有序 Set，顺序与键排序规则一致
     * 集合与原 Map 联动，操作 Entry 会同步修改原映射
     * @return 有序键值对集合
     */
    Set<Map.Entry<K, V>> entrySet();
}
```

## SortedMap 排序原理

`SortedMap` 的排序原理和 `SortedSet` 的排序原理一样。二者都是通过 `Comparator` 比较器来进行元素比较，然后对元素进行排序。
在 *04_SortedSet* 一节中，开发者学习过 `SortedSet` 的坑 —— 去重逻辑和 `HashSet` 完全不一样。同样地 `SortedMap` 也含有同样的坑：**判断 Key 是否重复不依靠 `	hashCode()`、`equals()`，仅依据比较器返回值，两 Key 比较结果为 0 就判定为同一个键，新键值会覆盖原有数据。**
开发者希望 `SortedMap` 能够按照自身意愿进行规则排序的话，存入 `SortedMap` 中的元素的 Key 需要实现 `Comparable` 接口，并重写比较规则。

```java
public class User implements Comparable<User> {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 仅按年龄排序，age相等返回0
    @Override
    public int compareTo(User o) {
        return Integer.compare(this.age, o.age);
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', age=" + age + "}";
    }

    // 即便重写 equals、hashCode，TreeMap 也不会使用
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
```

```java
public class SortedMapDemo {
    public static void main(String[] args) {
        SortedMap<User, String> userMap = new TreeMap<>();

        User u1 = new User("张三", 18);
        User u2 = new User("李四", 18);
        User u3 = new User("王五", 20);

        userMap.put(u1, "学生1");
        userMap.put(u2, "学生2");
        userMap.put(u3, "学生3");

        // 遍历输出集合
        for (Map.Entry<User, String> entry : userMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
```

```cmd
User{name='张三', age=18} -> 学生2
User{name='王五', age=20} -> 学生3
```

张三、李四 age 相同，`compareTo` 返回 0，`TreeMap` 判定为同一个 Key，后存入的 `u2` 直接覆盖 `u1` 的 value，最终集合只保留一条 `age=18` 的数据，和 `SortedSet` 去重陷阱完全一致。

想要避开这个陷阱开发者需要合理书写 `compareTo()` 的内容。

## SortedMap 注意事项 

1. `SortedMap` 的键去重逻辑和 `HashMap` 的键去重逻辑并不相同，若测试案例中向 `sortedMap` 存入3个 `User` 类型键对象，打印后只会保留一条键值对，即便 `User` 类重写了 `equals()`、`hashCode()` 也不会生效。 `SortedMap` 判断键是否重复不依靠哈希与等值判断，完全由比较器决定；若仅以 `age` 作为比较依据，多个 `User` 的 `age` 相同，`Integer.compare(user1.getAge(), user2.getAge()) == 0`，`SortedMap` 会判定两个键完全相同，后存入的键值会直接覆盖原有 value，这是使用 `SortedMap` 最容易踩坑的地方。 
2.  实际开发自定义键比较规则时，比较逻辑要和 `hashCode()`、`equals()` 保持统一：如果两个键对象通过 `equals()` 判断为同一个对象，那么比较器对二者的比较结果必须返回 0，避免出现哈希等值判定与排序判定逻辑冲突的问题。
3. 将自定义实体类作为 `SortedMap` 的键时，必须提供比较规则，二选一：要么键实体实现 `Comparable` 接口重写 `compareTo`，要么构造 `TreeMap` 时传入自定义 `Comparator`；否则运行添加元素时会抛出 `ClassCastException`。IDEA 会给出类型警告，部分场景下带警告代码看似能运行，但属于错误写法，上线后极易抛类型转换异常。
4. 不允许将 `null` 作为键存入 `SortedMap`，直接添加会抛出 `NullPointerException`；如果业务存在键为 `null` 的场景，需要在自定义比较器内部单独做 null 判空处理，兼容空键逻辑。
5. 线程安全层面，`SortedMap` 的实现类 `TreeMap` 本身是非线程安全容器，多线程并发读写、增删键值对时会出现并发异常；即便使用同步包装方法 `Collections.synchronizedSortedMap()` 包装集合，仅能保证集合操作线程安全，自定义比较器仍存在风险。若比较器内部维护了成员变量（例如统计比较次数、缓存数据），这类成员变量不具备线程安全，多线程场景下需要手动加锁保证变量安全。
6. 数值溢出问题，编写比较器数值比较逻辑时，要留意基础数据类型取值范围，避免直接使用减法做大小对比造成溢出。推荐使用 JDK 提供的工具方法 `Integer.compare()`、`Long.compare()` 等，官方底层已规避数值溢出；若自行编写数值运算比较逻辑，需要手动校验数值区间，防止正负溢出导致排序错乱。
7. 区间视图联动特性，`SortedMap` 提供 `subMap()`、`headMap()`、`tailMap()` 截取子有序映射，子 Map 属于原集合的视图，底层共用同一个 `TreeMap` 数据源。修改子 Map 内的键值、新增删除元素，原主 Map 数据会同步变更；反之修改原 Map，子视图数据也会同步变化，开发时需要注意二者数据联动带来的副作用。

