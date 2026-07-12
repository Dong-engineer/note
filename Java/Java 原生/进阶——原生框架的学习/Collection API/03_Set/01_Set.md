# Set

`Set` 是 Collection API 中与 `List` 同级的接口，二者均直接继承于 `Collection` 顶层接口。相较于 `List` 接口赋予集合**有序、可存储重复元素**的特性，`Set` 接口为 `Collection` 集合赋予了全新的核心特性，即**无序、元素不重复**，这也是 `Set` 集合最核心、最区别于 `List` 集合的关键特征。

在 Collection API 中，`Set` 和 `List` 各司其职，适配不同的业务开发场景。`List` 侧重有序存储，允许元素重复，适合需要按存储顺序访问、遍历集合的场景；而 `Set` 凭借去重、无序的特性，主要用于数据去重、唯一性校验等开发场景，是日常 Java 开发中高频使用的集合类型。

本节学习内容：

1. 了解 `Set` 以及 `Set` 特性。
2. 学习并掌握 `Set` 的常用方法。
3. 开发时如何合理使用 `Set` 的使用方法。

## 源码

```java
public interface Set<E> extends Collection<E> {
    // 添加元素：若元素不存在则添加，返回 true；否则返回 false
    boolean add(E e);

    // 添加集合中所有元素：仅添加当前 Set 中不存在的元素
    boolean addAll(Collection<? extends E> c);

    // 移除指定元素：存在则移除，返回 true
    boolean remove(Object o);

    // 判断是否包含指定元素
    boolean contains(Object o);

    // 判断是否包含集合中所有元素
    boolean containsAll(Collection<?> c);

    // 移除集合中所有元素
    void clear();

    // 获取迭代器
    Iterator<E> iterator();

    // 转换为数组
    Object[] toArray();
    <T> T[] toArray(T[] a);

    // 集合大小
    int size();

    // 判断是否为空
    boolean isEmpty();

    // 哈希值：基于所有元素的哈希值计算
    int hashCode();

    // 相等性：两个 Set 包含的元素完全相同（不考虑顺序）
    boolean equals(Object o);
}
```

`Set` 集合接口继承了 `Collection` ，其方法不再介绍。

## 使用示例

`Set` 是一个接口，无法直接创建对象，只能通过其实现类来创建集合对象，下面以 `HashSet` 为例。

```java
public class Test07 {
    public static void main(String[] args) {

        Set<String> set = new HashSet<>();

        // 添加程序
        set.add("你好");
        set.add("世界");
        set.add("!");
        set.add("你好");

        System.out.println(set);
        System.out.println("set 集合长度为：" + set.size());
        System.out.println("set 集合是否为空：" + (set.isEmpty() ? "是": "否"));

        System.out.println("set 集合移除元素 '!'：" + (set.remove("!") ? "成功" : "失败"));
        System.out.println("set 是否包含元素 '!'：" + (set.contains("!") ? "是" : "否"));
        System.out.println(set);

    }
}
```

```cmd
[!, 你好, 世界]
set 集合长度为：3
set 集合是否为空：否
set 集合移除元素 '!'：成功
set 是否包含元素 '!'：否
[你好, 世界]
```

以上代码，开发者不难发现 "你好" 元素并没有出现两次，这就是 `Set` 集合的特性体现，当开发者将 `List` 集合或者数组，转化成 `Set` 集合，其中重复的元素均会只保留第一个。

## `Set` 集合不重复特性原理

那么如何判断为重复元素呢？

```java
public class Test08 {
    public static void main(String[] args) {

        User user1 = new User("Dong"); // User 类并没有重写 equals() 和 hashCode()
        User user2 = new User("Dong");

        Set<User> set = new HashSet<>();
        set.add(user1);
        set.add(user2);

        System.out.println(set);

    }
}
```

```cmd
[User{name='Dong'}, User{name='Dong'}]
```

以上代码，两个 `User` 实例其属性一样，但是在添加到 `Set` 中并没有去除掉重复的，注释中提到了 `User` 类并没有重写 `equals()` 和 `hashCode()` 。

那么如果重写了呢？

```cmd
[User{name='Dong'}]
```

两次运行代码一致，可见结果大相径庭，这也表明了 **`Set` 集合的去重特性是由存储类的 `equals()` 和 `hashCode()` 来完成的** 。

## 注意事项

1. `Set` 集合是线程不安全的，如同 `List` 一样，Java 给有专门线程安全的实现类。
2. `Set` 集合中只能有一个 `null` （唯一性）。
3. 还有一些实现类的注意事项，后方学习到时再讲述。
