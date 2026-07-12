# HashMap（内部类 Node）

从本文内容开始，开发者正式对 `HashMap` 有了一定的认识，本节会讲述 `HashMap` 底层实际存储数据使用的数据结构之一——**链表**，链表对于开发者来说并不陌生，但是 `HashMap` 是怎么分配元素的内存地址的呢？在本节开发者都会慢慢得到答案。

## Node

### `Node` 的数据结构

```java
/**
* 静态内部类: Node<K, V>
* 该类实现了 Map 类中的内部 Entry 类，Entry 类用于封装键值对。
*/
static class Node<K,V> implements Map.Entry<K,V> {
    // 哈希值，判别键值对是否重复
    final int hash;
    // 键
    final K key;
    // 值
    V value;
    // 下一节点指针
    Node<K,V> next;
}
```

从以上源码中的字段，开发者不难发现一些信息：

1. `HashMap` 每一节点存储元素的方式为 `<Key,  Value>` ，即一个键对应一个值，很显然这种存储元素的方式，在寻找元素时会很方便（至于时间复杂度是多少，放到后面讲述）。
2. `HashMap` 的 `Node` 节点会存储下一节点的内存地址，这和链表的结构几乎一摸一样，这种结构的存储方式，在有新的元素添加或删除时也很方便（同样的时间复杂度后续讲述）。
3. `Node` 节点的 `key` 和 `hash` 都是 `final` 所修饰，这就是说，一旦 `HashMap` 存储了某个元素，该元素的 `key` 和 `hash` 不可能再被修改，只有 `value` 是允许被修改的。

### 构造函数

```java
// 有参构造函数
Node(int hash, K key, V value, Node<K,V> next) {
    this.hash = hash;
    this.key = key;
    this.value = value;
    this.next = next;
}
```

### `Node` 函数

```java
/***** 下面这些方法很简单，不再讲述 *****/
public final K getKey()        { return key; }
public final V getValue()      { return value; }
public final String toString() { return key + "=" + value; }

/**
 * 实现 Entry 类中的 hashCode()
 * 返回 key 的哈希值和 value 的哈希值的乘积，确保 HashMap 中是否存在哈希值相同的类型
 * @return int 类型
 */
public final int hashCode() {
    return Objects.hashCode(key) ^ Objects.hashCode(value);
}

public final V setValue(V newValue) {
    V oldValue = value;
    value = newValue;
    return oldValue;
}

/**
 * 实现 Entry 类中的 euqals()
 * 用于对比 HashMap 中元素是否有相同的元素，其底层依赖 hashCode() 
 * @param o 任意类型参数
 * @return boolean 类型
 */
public final boolean equals(Object o) {
    if (o == this)
        return true;

    return o instanceof Map.Entry<?, ?> e
        && Objects.equals(key, e.getKey())
        && Objects.equals(value, e.getValue());
}
```

以上代码，对于开发者来说应该是很简单（除非开发者的基础很差），`getKey()、getValue()、toString()、hashCode()、setValue()、equals()` 这些方法都是常见的 `getter、setter` 还有重写 `Object` 的三大方法。

要注意的是 `key` 和 `value` 只有 `getter` 方法，没有 `setter` 说明这两个属性是不允许开发者直接去操作的，需要开发者通过其他函数/方法去调用为这两个字段赋值。

# 结语

本文内容，到此结束，从以上源码和内容描述中不难看出，`HashMap` 底层实际存储的结构是——**单向链表**。

当然经过前面内容的铺垫，请开发者保持思考：

1. `HashMap` 的底层仅单单是一个**单向链表**吗？
2. `HashMap` 是如何维护这个链表的，只是简单的增删改查吗？
3. `HashMap` 的 `key` 和 `value` 对开发者来说不难理解，那么 `hash` 字段是怎么被赋值的呢？

这些问题，会在下一节讲述。
