# HashMap

本节内容中的公共函数部分，开发者要会熟练使用，在实际开发中会经常使用。

## 公共函数

```java
/**
 * 获取 HashMap 中键值对的数量（注意：不是容量，容量是 table 数组长度）
 * @return int 类型，返回当前 HashMap 中存储的键值对总数
 */
public int size() {
    return size;
}

/**
 * 判断 HashMap 是否为空（即是否包含任何键值对）
 * @return boolean 类型，为空返回 true，不为空返回 false
 */
public boolean isEmpty() {
    return size == 0;
}

/**
 * 根据指定的 key 获取对应的 value
 * @param key 指定的键（Object 类型，支持空值）
 * @return V 泛型类型，找到对应 key 返回其关联的 value，未找到返回 null
 */
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(key)) == null ? null : e.value;
}

/**
 * 判断 HashMap 中是否包含指定的 key
 * @param key 指定的待校验键（Object 类型，支持空值）
 * @return boolean 布尔类型，包含该 key 返回 true，不包含返回 false
 */
public boolean containsKey(Object key) {
    return getNode(key) != null;
}

/**
 * 向 HashMap 中添加或替换键值对（若 key 已存在，则替换原有 value）
 * 底层依赖 putVal() 方法实现具体的插入逻辑
 * @param key 指定的键（K 泛型类型）
 * @param value 指定的值（V 泛型类型）
 * @return V 泛型类型，返回该 key 之前关联的旧 value，若 key 不存在则返回 null
 */
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

/**
 * 将指定 Map 中的所有键值对批量添加到当前 HashMap 中
 * @param m 待批量添加的 Map，其键类型继承自 K，值类型继承自 V
 */
public void putAll(Map<? extends K, ? extends V> m) {
    putMapEntries(m, true);
}

/**
 * 根据指定的 key 删除 HashMap 中的对应键值对
 * @param key 指定的待删除键（Object 类型，支持空值）
 * @return V 泛型类型，返回被删除键值对的 value，若 key 不存在则返回 null
 */
public V remove(Object key) {
    Node<K,V> e;
    return (e = removeNode(hash(key), key, null, false, true)) == null ?
        null : e.value;
}

/**
 * 清空 HashMap 中的所有键值对，使其恢复为空状态
 * 该方法会将 table 数组中的所有元素置为 null，并重置键值对数量 size 为 0
 */
public void clear() {
    Node<K,V>[] tab;
    modCount++; // 记录 HashMap 结构修改次数
    if ((tab = table) != null && size > 0) {
        size = 0;
        for (int i = 0; i < tab.length; ++i)
            tab[i] = null;
    }
}

/**
 * 判断 HashMap 中是否包含指定的 value
 * @param value 指定的待校验值（Object 类型，支持空值）
 * @return boolean 布尔类型，包含该 value 返回 true，不包含返回 false
 */
public boolean containsValue(Object value) {
    Node<K,V>[] tab; V v;
    if ((tab = table) != null && size > 0) {
        // 遍历 table 数组所有桶位
        for (Node<K,V> e : tab) {
            // 遍历每个桶位下的链表（或红黑树）节点
            for (; e != null; e = e.next) {
                if ((v = e.value) == value ||
                    (value != null && value.equals(v)))
                    return true;
            }
        }
    }
    return false;
}

/**
 * 获取 HashMap 中所有 key 组成的 Set 集合（该集合是 HashMap 的视图，会随 HashMap 变化而变化）
 * @return Set<K> 类型，返回包含所有 key 的不可重复集合
 */
public Set<K> keySet() {
    Set<K> ks = keySet;
    if (ks == null) {
        ks = new KeySet();
        keySet = ks;
    }
    return ks;
}

/**
 * 预处理数组，确保数组容量足够容纳 HashMap 中的所有元素
 * 若传入数组容量不足，则创建新的对应类型数组；若容量过剩，则设置末尾元素为 null
 * @param <T> 数组元素的泛型类型
 * @param a 待预处理的数组
 * @return T[] 类型，返回预处理后的数组（要么是新创建的数组，要么是原数组）
 */
@SuppressWarnings("unchecked")
final <T> T[] prepareArray(T[] a) {
    int size = this.size;
    if (a.length < size) {
        // 若传入数组容量不足，反射创建对应类型、指定容量的新数组
        return (T[]) java.lang.reflect.Array
            .newInstance(a.getClass().getComponentType(), size);
    }
    if (a.length > size) {
        // 若传入数组容量过剩，将第 size 个位置置为 null（标记元素结束）
        a[size] = null;
    }
    return a;
}

/**
 * 将 HashMap 中的所有 key 存入指定数组并返回
 * @param <T> 数组元素的泛型类型
 * @param a 用于存储 key 的目标数组
 * @return T[] 类型，返回存储了所有 key 的数组
 */
<T> T[] keysToArray(T[] a) {
    Object[] r = a;
    Node<K,V>[] tab;
    int idx = 0;
    if (size > 0 && (tab = table) != null) {
        // 遍历 table 数组所有桶位
        for (Node<K,V> e : tab) {
            // 遍历链表（或红黑树），将所有 key 存入数组
            for (; e != null; e = e.next) {
                r[idx++] = e.key;
            }
        }
    }
    return a;
}

/**
 * 将 HashMap 中的所有 value 存入指定数组并返回
 * @param <T> 数组元素的泛型类型
 * @param a 用于存储 value 的目标数组
 * @return T[] 类型，返回存储了所有 value 的数组
 */
<T> T[] valuesToArray(T[] a) {
    Object[] r = a;
    Node<K,V>[] tab;
    int idx = 0;
    if (size > 0 && (tab = table) != null) {
        // 遍历 table 数组所有桶位
        for (Node<K,V> e : tab) {
            // 遍历链表（或红黑树），将所有 value 存入数组
            for (; e != null; e = e.next) {
                r[idx++] = e.value;
            }
        }
    }
    return a;
}

/**
 * 获取 HashMap 中所有 value 组成的 Collection 集合（该集合是 HashMap 的视图，会随 HashMap 变化而变化）
 * @return Collection<V> 类型，返回包含所有 value 的集合（允许重复值）
 */
public Collection<V> values() {
    Collection<V> vs = values;
    if (vs == null) {
        vs = new Values();
        values = vs;
    }
    return vs;
}

/**
 * 获取 HashMap 中所有键值对（Map.Entry）组成的 Set 集合（该集合是 HashMap 的视图，会随 HashMap 变化而变化）
 * @return Set<Map.Entry<K,V>> 类型，返回包含所有键值对的不可重复集合
 */
public Set<Map.Entry<K,V>> entrySet() {
    Set<Map.Entry<K,V>> es;
    return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
}

/**
 * 根据指定的 key 获取对应的 value，若 key 不存在则返回默认值
 * @param key 指定的键（Object 类型，支持空值）
 * @param defaultValue 当 key 不存在时返回的默认值（V 泛型类型）
 * @return V 泛型类型，找到 key 返回对应 value，未找到返回 defaultValue
 */
@Override
public V getOrDefault(Object key, V defaultValue) {
    Node<K,V> e;
    return (e = getNode(key)) == null ? defaultValue : e.value;
}

/**
 * 仅当指定的 key 不存在时，才向 HashMap 中添加该键值对
 * 若 key 已存在，则不做任何修改，直接返回原有 value
 * @param key 指定的键（K 泛型类型）
 * @param value 指定的值（V 泛型类型）
 * @return V 泛型类型，返回该 key 已存在的 value，若 key 不存在则返回 null
 */
@Override
public V putIfAbsent(K key, V value) {
    return putVal(hash(key), key, value, true, true);
}

/**
 * 仅当指定的 key 存在且对应 value 与传入 value 一致时，才删除该键值对
 * @param key 指定的待删除键（Object 类型，支持空值）
 * @param value 指定的待匹配值（Object 类型，支持空值）
 * @return boolean 布尔类型，删除成功返回 true，删除失败（key 不存在或 value 不匹配）返回 false
 */
@Override
public boolean remove(Object key, Object value) {
    return removeNode(hash(key), key, value, true, true) != null;
}

/**
 * 仅当指定的 key 存在且对应旧值与 oldValue 一致时，才替换为 newValue
 * @param key 指定的键（K 泛型类型）
 * @param oldValue 待匹配的旧值（V 泛型类型）
 * @param newValue 要替换的新值（V 泛型类型）
 * @return boolean 布尔类型，替换成功返回 true，替换失败（key 不存在或旧值不匹配）返回 false
 */
@Override
public boolean replace(K key, V oldValue, V newValue) {
    Node<K,V> e; V v;
    if ((e = getNode(key)) != null &&
        ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))) {
        e.value = newValue;
        afterNodeAccess(e);
        return true;
    }
    return false;
}

/**
 * 仅当指定的 key 存在时，替换该 key 对应的 value
 * @param key 指定的键（K 泛型类型）
 * @param value 要替换的新值（V 泛型类型）
 * @return V 泛型类型，返回该 key 对应的旧 value，若 key 不存在则返回 null
 */
@Override
public V replace(K key, V value) {
    Node<K,V> e;
    if ((e = getNode(key)) != null) {
        V oldValue = e.value;
        e.value = value;
        afterNodeAccess(e);
        return oldValue;
    }
    return null;
}
```

## 注意

如果开发者认真阅读 `HashMap` 的源码后，会发现除了本文内容所写的公共函数外，还有一些函数/方法在本文中并没有提到。
首先，这些函数在实际开发中并不常用，但是他们在 Java 的源码中经常使用。

其次，这些函数，作者认为放在后面的学习更加合适。
