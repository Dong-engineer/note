# HashSet

在上节学习了 `Set` 接口，了解其特性为无序、唯一的特性，但是 `Set` 是一个接口，其具体的实现由其他类完成的， `HashSet` 就是其中之一也是常用之一。

`HashSet` 是 `Set` 接口的实现类之一，这也说明 `HashSet` 具有 `Set` 集合的一切特性。

在上节细心的开发者在运行代码时，不难发现 `Set` 集合的无序是不绝对的，即每次运行的结果开发者不难发现每次打印输出的结果顺序都是一样的，但是元素的排序的确没有按照开发者添加的顺序来输出。这其实和 `HashSet` 的底层有关系。

本节学习内容：

1. 了解 `HashSet` 以及 `HashSet` 特性。
2. 学习并掌握 `HashSet` 的常用方法。
3. 熟悉 `HashSet` 的底层是如何通过 `HashMap` 实现的。
4. 开发时如何合理使用 `HashSet` 。

## 源码

```java
public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable
{
    @java.io.Serial
        static final long serialVersionUID = -5024744406713321676L;

    // HashSet 底层存储是由 HashMap 完成的，开发者通过 add() 方法添加的元素会被保存到 HashMap 的 key 值中，其 value 值则是一个空对像
    private transient HashMap<E,Object> map;

    // HashMap 中所有 key 对应的默认 value（一个静态空对象，节省内存）
    private static final Object PRESENT = new Object();

    // lodFactor：负载因子，用于判断 HashMap 是否需要扩容的参数，该值为 0.75，表示当 HashMap 的容量占到了 75% 就要进行扩容
    /*
    * 这里会有开发者可能会疑惑，这很好请保持你的疑惑—— HashSet 哪里来的负载因子呢，打开 HashMap 的源码，不需要看太多，只需要找到如下两段代码
    * static final float DEFAULT_LOAD_FACTOR = 0.75f;
    * final float loadFactor;
    * 在后面的映射中会讲述，这里知道即可。
    */
    
    // 1. 默认构造方法：初始化一个空的 HashMap（初始容量 16，负载因子 0.75）
    public HashSet() {
        map = new HashMap<>();
    }

    // 2. 传入集合：将集合中的元素添加到新的 HashMap 中
    public HashSet(Collection<? extends E> c) {
        map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
        addAll(c);
    }

    // 3. 指定初始容量和负载因子
    public HashSet(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }

    // 4. 仅指定初始容量（负载因子默认 0.75）
    public HashSet(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }

    // 5. 包访问权限构造方法（仅用于 LinkedHashSet）：可指定 HashMap 的访问顺序
    HashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
    }

    // 核心操作
    // 1. 添加元素：调用 HashMap 的 put() 方法，key 为元素，value 为 PRESENT
    // 若元素已存在（HashMap 的 put() 返回旧 value，不为 null），则添加失败（返回 false）
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    // 2. 删除元素：调用 HashMap 的 remove() 方法，返回是否删除成功
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    // 3. 判断元素是否存在：调用 HashMap 的 containsKey() 方法
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    // 4. 获取集合大小：调用 HashMap 的 size() 方法
    public int size() {
        return map.size();
    }

    // 5. 清空集合：调用 HashMap 的 clear() 方法
    public void clear() {
        map.clear();
    }
}
```

从源码中可见 `HashSet` 集合的实现几乎完全依赖了 `HashMap` ，所以其无序、唯一的特性也是利用了 `HashMap` 的特性，`HashSet` 的去重是依赖了 `HashMap` 的 key 值特性，而 key 值的底层是由 `hashCoed()、equals()` 两个函数实现的。其具体的实现详情请看 `hashCode()` 的重写。

## 使用实例

```java
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName Test09
 * @Description HashSet 的核心方法
 * @Author 34194
 * @DATE 2025/11/8 14:32
 * @Version 1.0
 */
public class Test07 {
    public static void main(String[] args) {
        Set<String> set  = new HashSet<>();
        set.add("《结尾》");
        set.add("《那由她》");
        set.add("《小半》");
        set.add("《半点心》");
        set.add("《桃花朵朵开》");

        System.out.println("set 集合初始长度：" + set.size());
        System.out.println("set 集合是否包含歌曲《星》：" + (set.contains("《星》") ? "YES" : "NO"));

        System.out.println("正在添加歌曲《星》...");
        set.add("《星》");
        System.out.println("添加完成");
        System.out.println(set);

        System.out.println("正在删除歌曲《星》...");
        set.remove("《星》");
        System.out.println("删除完成");
        System.out.println(set);

    }
}
```

## 注意事项

1. 首当的就是线程，上节说过 `Set` 集合是非线程安全，所以 `HashSet` 也是不安全的。
2. 合理利用其特性，`Set` 集合是无序、唯一的，为了方便开发 Java 给了其他实现类改变 `Set` 集合的无序特性，如：`SortedSet、TreeSet` ··· 。
3. 性能问题：
   - `HashSet` 的添加、删除、查询操作都是高效的（平均时间复杂度为 O(1)，注意是平均时间复杂度，当哈希冲突严重时就要另说了）
   - 扩容问题，频繁的扩容或导致 `HashSet` 的性能大大降低，开发者可以事先设置好其容量大小（对于如何合理设置其容量大小，请参考 `HashMap` 的负载因子，根据其负载因子合理设置）
   - `Set` 集合不允许通过索引查询元素，通常是通过迭代器 `iterator` 来访问元素或者 `forEach` 循环

