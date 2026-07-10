# HashMap

## 属性

首先是 `HashMap` 的属性，如下，在本文中开发者无需关心字段的具体作用是什么（因为后续讲述方法时，开发者自然就会明白），但开发者需要知道是这些字段的存在。

```java
/**
 * 读前须知，HashMap 的内容量极大，开发者一定要保持耐心读完，以下内容与源码一模一样，HashMap 的源码作者并没有去调整属性、方法的位置，这样也方便开发者去阅读
 * Java 的源码。
 */
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable {

    /***** 静态常量属性 *****/
    // 序列化版本号
    @java.io.Serial
    private static final long serialVersionUID = 362498820763181265L;
    // 默认的初始化长度
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    // HashMap 最大容量
    static final int MAXIMUM_CAPACITY = 1 << 30;
    // 默认负载因子
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
	// 链表转红黑树阈值
    static final int TREEIFY_THRESHOLD = 8;
	// 红黑树转链表阈值
    static final int UNTREEIFY_THRESHOLD = 6;
	// 红黑树最小容量
    static final int MIN_TREEIFY_CAPACITY = 64;

    /***** 属性 *****/
    // 底层数据结构之一，链表数组
    transient Node<K,V>[] table;
    // key value 对的 Set 集合
    transient Set<Map.Entry<K,V>> entrySet;
    // 表长度
    transient int size;
    // 修改次数，快速失败机制
    transient int modCount;
    // 扩容阈值
    int threshold;
	// 负载因子（控制 HashMap 扩容的频率）
    final float loadFactor;
}
```

## 构造函数

### 有参构造函数

在 `HashMap` 的有参构造函数中，开发者只需要关心 `HashMap(int initialCapacity, float loadFactor)` 即可，但在日常开发中开发者很少（几乎没有）会使用该方法。多数情况下使用的是无参构造方法（有参构造的另外两个方法，也不常使用，但需要了解）。

请开发者牢记：无论是有参构造函数初始化，还是无参构造函数初始化，都只会对 `HashMap` 的容量相关的属性进行初始化，`HashMap` 的底层数据结构是不会进行任何初始化的。

流程图如下：

<img src="../img/HashMap/HashMap 构造方法.drawio.png"  />

```java
/**
 * 初始化方法，默认容量 16 ，负载因子 0.75（开发者一定要注意 HashMap 的初始中做了些什么）
 * @param initialCapacity 指定 HashMap 初始长度
 * @param loadFactor 初始负载因子
 * @throws illegalArgumentException 初始化长度为 0 或者负载因子不合理抛出异常
 */
public HashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                                           initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " +
                                           loadFactor);
    this.loadFactor = loadFactor;
    this.threshold = tableSizeFor(initialCapacity);
}

/**
 * 初始化方法，负载因子为默认的 0.75f
 * @param initialCapacity 指定 HashMap 初始长度
 * @throws illegalArgumentException 初始化长度为 0 或者负载因子不合理抛出异常
 */
public HashMap(int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
}

/**
 * 有参构造，将指定参数映射存入新的映射对象中
 * @param m 接收一个 Map[带有泛型] 类型的映射
 */
public HashMap(Map<? extends K, ? extends V> m) {
    // loadFactor = 0.75f 
    this.loadFactor = DEFAULT_LOAD_FACTOR;
    putMapEntries(m, false);
}
```

### 无参构造函数

```java
/**
 * 无参构造，默认的负载因子
 */
public HashMap() {
    this.loadFactor = DEFAULT_LOAD_FACTOR;
}
```

## 静态方法

### 静态哈希函数

```java
/**
 * 静态哈希函数
 * 该函数主要用于确保 HashMap 中键的唯一性，其底层依赖存储对象的 hashCode() 
 * @param key 任意类型参数
 * @return int 类型
 */
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

该方法对元素的哈希值的处理很值得借鉴，正常计算 key 的哈希值得到 int 类型（长度为 32），以上的计算公式开发者可自行举例运算查看效果。

该方法的目的就是通过哈希值的高 16 位与低 16 位进行混合运算，减少哈希冲突，增加哈希值的散列性。

### HashMap 红黑树节点比较：类型校验与安全比较工具方法

```java
/**
 * 判断 x 是否实现了「自身类型的 Comparable 接口」（即 Comparable<X>，泛型参数是自身类）只有满足这个条件，才返回 x 的类对象，否则返回 null
 * 简单说就是做反射校验，判断对象是否实现[自身类型的 Comparable 接口]
 * 是红黑树元素后面做比较的一个前置依赖函数（这里的反射应用开发者可以学习一下）
 * @param x 任意类型参数
 * @return 判断 x 是否实现了「自身类型的 Comparable 接口」（即 Comparable<X>，泛型参数是自身类）只有满足这个条件，才返回 x 的类对象，否则返回 null
 */
static Class<?> comparableClassFor(Object x) {
    // 判断参数 x 是否实现了 Comparable，没有实现直接返回 null
    if (x instanceof Comparable) {
        // 这里作者做了一些调整，增加一些可读性
        Class<?> c;				// 用于存储参数 x 的类型，后面会讲到
        // Type[] 类型数组
        // ts：用于存储参数 x 的类[直接实现的所有泛型接口]（包含带泛型信息的接口，无泛型的接口也会被包含，后续通过 t instanceof ParameterizedType 筛选带泛型的接口）
        // as：用于存储[单个带泛型接口的实际泛型参数类型数组]（即 p 对应的泛型参数，不是 ts 数组所有接口的泛型信息）
        // 例如：Comparable<User> 对应的 as 数组只有一个元素 User.class
        Type[] ts, as;
        // p 存储的是[t 对应的 ParameterizedType 实例]（即带具体泛型参数的接口类型对象，比如 Comparable<User> 对应的反射对象）
        // 例如：List<String> 在 ts 数组中，那么在下面的代码中 p.getRawType() = List
        ParameterizedType p;

        // 判断参数 x 的类是否为 String（String 明确实现了 Comparable<String>，直接返回其类对象，跳过后续复杂校验）
        // 此时 Class<?> c 变量被赋值运算，c = x.getClass()
        if ((c = x.getClass()) == String.class)
            return c;
        // c.getGenericInterfaces() 获取 x 的类实现的所有的接口（所有的接口都会返回，如果接口有泛化信息，那么泛化信息也会带着）
        // 补充：c.getInterfaces() 获取 x 的类实现的所有的接口（所有的接口都会返回，接口的泛化信息不会带着）
        // ts 在这里被赋值运算
        if ((ts = c.getGenericInterfaces()) != null) {
            // 遍历 Type[] ts
            for (Type t : ts) {

                // 这里的判断很复杂，不要急，以 && 为分割符，一个一个来讲述
               	/**
                 * (t instanceof ParameterizedType)
                 * t => Type[] ts 中的各个元素
                 * ParameterizedType => 该类是反射中一个接口（还没有讲述反射框架），专门用于描述[带具体泛型参数的类型]
                 * 在这里的意思是：筛选出 Type[] ts 中带有泛化信息的接口
                 */
                /**
                 * ((p = (ParameterizedType) t).getRawType() == Comparable.class)
                 * 强转 t 对变量 p 进行赋值，通过反射获取 t 的原始类型（即不带泛化类型信息），并对 p 进行判断，其是否等于 Comparable
                 */
                /**
                 * (as = p.getActualTypeArguments()) != null
                 * 通过 p 反射获取 t 的所有泛化类型，并对其判断是否为 null
                 */
                /**
                 * as.length == 1 && as[0] == c
                 * as 只有一个泛型参数且该参数就是 x 自身的类
                 */
                /**
                 * 将以上连接起来：
                 * 从变量 c 开始，c 只被赋值运算了一次，所以 c 始终为 x.getClass()
                 * ts 则为 x.getClass().getGenericInterfaces()，ts 不为空
                 * 开始遍历 ts 
                 * ts 中的每一个元素 t ，都必须是 Coparable[泛化类型]，而且泛化类型必须是 x.getClass()
                 * 简单来说参数 x 必须实现 Comparable<x.getClass()>
                 */
                if ((t instanceof ParameterizedType) &&
                    ((p = (ParameterizedType) t).getRawType() ==
                     Comparable.class) &&
                    (as = p.getActualTypeArguments()) != null &&
                    as.length == 1 && as[0] == c)
                    return c;

            }
        }
    }
    return null;
}
/**
 * 安全比较两个[实现了自身类型 Comparable 接口]的键（Key）大小的工具方法
 * 该方法需要 comparableClassFor() 函数的配合，来调用
 * @param kc 键 k 的类对象（由 comparableClassFor 方法返回，即 k 实现的 Comparable 泛型参数类型，也是 k 自身的类）
 * @param k 待比较的第一个键
 * @param x 待比较的第二个键
 */
@SuppressWarnings({"rawtypes","unchecked"})
static int compareComparables(Class<?> kc, Object k, Object x) {
    return (x == null || x.getClass() != kc ? 0 :
            ((Comparable)k).compareTo(x));
}
```

### 工具方法（计算大于等于指定容量的最小 2 次幂）

```java
/**
 * 工具方法，HashMap 中用于计算大于等于传入容量 cap 的最小 2 的幂次，最小 2 次幂要牢记
 * @param cap 用户传入的期望值，或 HashMap 内部计算的容量值
 */
static final int tableSizeFor(int cap) {
    // Integer.numberOfLeadingZeros() => 取高位连续 0 的个数
    // 例如：二进制：00000000 00000000 00000000 00001010 高位连续 0 有 28 个，返回 28
    /**
     * 以十进制 9 和二进制一个 8 位为例，9 => 二进制：0000 1001
     * Integer.numberOfLeadingZeros(9 - 1) 的结果为 4
     * -1 => 二进制 1111 1111 ，向右移动 4 位 => 0000 1111
     * 在经过三目运算，返回 15+1 
     * 结果为 16
     */
    int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}
```

