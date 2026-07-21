# TreeSet

`TreeSet` 是 Java 集合框架中基于**红黑树**实现的**有序且不重复**集合，其具有高效的查询、添加、删除、插入等操作其时间复杂度为 `O(log n)` 。

## 源码

```java
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * 基于TreeMap实现的有序可导航Set集合
 * 底层依托红黑树存储元素，集合元素天然按比较规则排序，元素不可重复
 * 实现NavigableSet接口，支持区间截取、邻近元素查询、倒序视图等高级操作
 * 支持克隆与序列化
 * @param <E> 集合中存储的元素类型
 */
public class TreeSet<E> extends AbstractSet<E>
        implements NavigableSet<E>, Cloneable, Serializable {
    /**
     * 底层存储容器，使用NavigableMap维护元素，元素作为Map的key
     */
    private transient NavigableMap<E, Object> m;

    /**
     * Map中统一固定占位value，仅用于填充value占位，无实际业务含义
     */
    private static final Object PRESENT = new Object();

    /**
     * 内部构造器，传入已初始化的NavigableMap作为底层容器
     * @param m 底层导航Map实例
     */
    TreeSet(NavigableMap<E, Object> m) {
        this.m = m;
    }

    /**
     * 无参构造方法
     * 底层使用无比较器的TreeMap，元素需实现Comparable接口自然排序
     */
    public TreeSet() {
        this(new TreeMap<>());
    }

    /**
     * 指定比较器构造TreeSet
     * 使用自定义Comparator定义元素排序规则，元素可不实现Comparable
     * @param comparator 元素自定义比较器
     */
    public TreeSet(Comparator<? super E> comparator) {
        this(new TreeMap<>(comparator));
    }

    /**
     * 通过已有集合初始化TreeSet
     * 先创建空TreeSet，再批量添加传入集合内所有元素，自动去重排序
     * @param c 待转换的源集合
     */
    public TreeSet(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    /**
     * 通过有序SortedSet初始化TreeSet
     * 复用源SortedSet的比较器，批量导入有序元素
     * @param s 源有序集合
     */
    public TreeSet(SortedSet<E> s) {
        this(s.comparator());
        addAll(s);
    }

    /**
     * 获取升序遍历元素的迭代器
     * @return 升序迭代器
     */
    @Override
    public Iterator<E> iterator() {
        return m.navigableKeySet().iterator();
    }

    /**
     * 获取降序遍历元素的迭代器
     * @return 倒序迭代器
     */
    @Override
    public Iterator<E> descendingIterator() {
        return m.descendingKeySet().iterator();
    }

    /**
     * 返回当前集合的倒序视图
     * 修改视图会同步影响原集合底层数据
     * @return 元素倒序排列的新TreeSet视图
     */
    @Override
    public NavigableSet<E> descendingSet() {
        return new TreeSet<>(m.descendingMap());
    }

    /**
     * 获取集合存储元素总数
     * @return 元素数量
     */
    @Override
    public int size() {
        return m.size();
    }

    /**
     * 判断集合是否为空
     * @return 无元素返回true，存在元素返回false
     */
    @Override
    public boolean isEmpty() {
        return m.isEmpty();
    }

    /**
     * 判断集合中是否包含指定元素
     * @param o 待查询元素
     * @return 存在返回true，不存在返回false
     */
    @Override
    public boolean contains(Object o) {
        return m.containsKey(o);
    }

    /**
     * 向集合添加单个元素
     * @param e 待添加元素
     * @return 添加成功返回true；元素已存在返回false
     */
    @Override
    public boolean add(E e) {
        return m.put(e, PRESENT) == null;
    }

    /**
     * 删除集合中指定元素
     * @param o 待删除元素
     * @return 删除成功返回true；元素不存在返回false
     */
    @Override
    public boolean remove(Object o) {
        return m.remove(o) == PRESENT;
    }

    /**
     * 清空集合内所有元素
     */
    @Override
    public void clear() {
        m.clear();
    }

    /**
     * 批量添加集合内全部元素
     * 底层优化：若源集合为SortedSet且比较器一致，直接批量导入提升性能
     * @param c 待批量添加的源集合
     * @return 本次操作是否新增了任意元素
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (m.size() == 0 && c.size() > 0 &&
                c instanceof SortedSet &&
                m instanceof TreeMap<E, Object> map) {
            SortedSet<? extends E> set = (SortedSet<? extends E>) c;
            if (Objects.equals(set.comparator(), map.comparator())) {
                map.addAllForTreeSet(set, PRESENT);
                return true;
            }
        }
        return super.addAll(c);
    }

    /**
     * 根据指定开闭区间截取子集合视图
     * @param fromElement 区间起始边界
     * @param fromInclusive 是否包含起始元素 true包含 false不包含
     * @param toElement 区间结束边界
     * @param toInclusive 是否包含结束元素 true包含 false不包含
     * @return 指定区间范围的TreeSet视图
     */
    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive,
                                  E toElement, boolean toInclusive) {
        return new TreeSet<>(m.subMap(fromElement, fromInclusive,
                toElement, toInclusive));
    }

    /**
     * 截取集合头部区间，返回小于(或等于)上限元素的视图
     * @param toElement 区间上限边界
     * @param inclusive 是否包含上限元素
     * @return 头部区间子集合视图
     */
    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return new TreeSet<>(m.headMap(toElement, inclusive));
    }

    /**
     * 截取集合尾部区间，返回大于(或等于)下限元素的视图
     * @param fromElement 区间下限边界
     * @param inclusive 是否包含下限元素
     * @return 尾部区间子集合视图
     */
    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return new TreeSet<>(m.tailMap(fromElement, inclusive));
    }

    /**
     * 标准左闭右开区间截取子集合
     * 等价 subSet(fromElement, true, toElement, false)
     * @param fromElement 起始边界（包含）
     * @param toElement 结束边界（不包含）
     * @return 标准区间有序集合视图
     */
    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    /**
     * 获取小于toElement的头部区间视图（不包含边界）
     * 等价 headSet(toElement, false)
     * @param toElement 上限边界元素
     * @return 头部有序视图
     */
    @Override
    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    /**
     * 获取大于等于fromElement的尾部区间视图（包含边界）
     * 等价 tailSet(fromElement, true)
     * @param fromElement 下限边界元素
     * @return 尾部有序视图
     */
    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    /**
     * 获取当前集合使用的元素比较器
     * @return 自定义比较器；无自定义比较器返回null（使用元素自然排序）
     */
    @Override
    public Comparator<? super E> comparator() {
        return m.comparator();
    }

    /**
     * 获取集合中最小元素
     * @return 首元素；集合为空抛出NoSuchElementException
     */
    @Override
    public E first() {
        return m.firstKey();
    }

    /**
     * 获取集合中最大元素
     * @return 尾元素；集合为空抛出NoSuchElementException
     */
    @Override
    public E last() {
        return m.lastKey();
    }

    /**
     * 获取小于目标元素的最大元素
     * @param e 对比目标元素
     * @return 邻近元素；无匹配返回null
     */
    @Override
    public E lower(E e) {
        return m.lowerKey(e);
    }

    /**
     * 获取小于等于目标元素的最大元素
     * @param e 对比目标元素
     * @return 邻近元素；无匹配返回null
     */
    @Override
    public E floor(E e) {
        return m.floorKey(e);
    }

    /**
     * 获取大于等于目标元素的最小元素
     * @param e 对比目标元素
     * @return 邻近元素；无匹配返回null
     */
    @Override
    public E ceiling(E e) {
        return m.ceilingKey(e);
    }

    /**
     * 获取大于目标元素的最小元素
     * @param e 对比目标元素
     * @return 邻近元素；无匹配返回null
     */
    @Override
    public E higher(E e) {
        return m.higherKey(e);
    }

    /**
     * 获取并移除集合最小元素
     * @return 最小元素；集合为空返回null
     */
    @Override
    public E pollFirst() {
        Map.Entry<E, ?> e = m.pollFirstEntry();
        return (e == null) ? null : e.getKey();
    }

    /**
     * 获取并移除集合最大元素
     * @return 最大元素；集合为空返回null
     */
    @Override
    public E pollLast() {
        Map.Entry<E, ?> e = m.pollLastEntry();
        return (e == null) ? null : e.getKey();
    }

    /**
     * 克隆当前TreeSet实例，底层TreeMap做拷贝
     * @return 当前集合的独立副本
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        TreeSet<E> clone;
        try {
            clone = (TreeSet<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        clone.m = new TreeMap<>(m);
        return clone;
    }

    /**
     * 序列化写入对象
     * 依次写入比较器、元素数量、所有元素
     * @param s 对象输出流
     * @throws IOException IO写入异常
     */
    @java.io.Serial
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(m.comparator());
        s.writeInt(m.size());
        for (E e : m.keySet())
            s.writeObject(e);
    }

    /**
     * 反序列化读取对象，重建底层TreeMap并恢复元素
     * @param s 对象输入流
     * @throws IOException IO读取异常
     * @throws ClassNotFoundException 元素类找不到异常
     */
    @java.io.Serial
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        @SuppressWarnings("unchecked")
        Comparator<? super E> c = (Comparator<? super E>) s.readObject();
        TreeMap<E, Object> tm = new TreeMap<>(c);
        m = tm;
        int size = s.readInt();
        tm.readTreeSet(size, s, PRESENT);
    }

    /**
     * 获取集合并行分割迭代器
     * @return 支持并行遍历的Spliterator
     */
    @Override
    public Spliterator<E> spliterator() {
        return TreeMap.keySpliteratorFor(m);
    }

    /**
     * 序列化版本号
     */
    @java.io.Serial
    private static final long serialVersionUID = -2479143000061671589L;
}
```

结合前两节对 `Set`、`NavigableSet` 接口源码的学习，我们已经掌握了 `TreeSet` 的继承体系与核心方法定义。在本节 `TreeSet` 源码阅读中，大家重点掌握其**构造方法与底层存储原理**。

`TreeSet` 本身没有自定义的存储结构，其底层完全依托 **`NavigableMap`**  实现数据存储，日常开发熟知的 `TreeMap` 正是 `NavigableMap` 接口的具体实现类，这也是 `TreeSet` 具备有序、去重特性的核心原因。

在元素存储逻辑上，`TreeSet` 的 `add()` 方法本质是调用了底层 `Map` 的 `put()` 方法。存入的集合元素会作为 **`Map` 的 Key** 实现唯一性去重，而 `Map` 对应的 Value 统一使用一个静态常量对象 `PRESENT` 填充（`private static final Object PRESENT = new Object()`），仅作为占位值使用，无实际业务意义。通过源码 `return m.put(e, PRESENT)==null` 逻辑，即可判断元素是否新增成功。

## 使用示例

```java
package Java_Protogenetic_API.Collection.Set.TreeSet;

import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * @ClassName Test01
 * @Description TreeSet的核心方法
 * @Author 34194
 * @DATE 2025/12/1 10:02
 * @Version 1.0
 */
public class Test01 {
    public static void main(String[] args) {
        NavigableSet<Integer> treeSet = new TreeSet<>();

        for (int i = 1; i < 101; i += 5) {
            treeSet.add(100 + i);
        }

        System.out.println("小于150的最大元素：" + treeSet.lower(150));
        System.out.println("小于等于150的最大元素：" + treeSet.floor(150));

        System.out.println("大于150的最小元素：" + treeSet.higher(150));
        System.out.println("大于等于150的最小元素：" + treeSet.ceiling(150));

        System.out.println("截取120 —— 180(左闭右开)之间的元素：" + treeSet.subSet(120, 180));

        System.out.println("移除126：" + (treeSet.remove(126) ? "成功" : "失败"));
        System.out.println("是否包含126：" + (treeSet.contains(126) ? "包含" : "不包含"));
    }
}
```

## 注意事项

`TreeSet` 是 `NavigableSet` 的实现类，并没有额外增加新的方法，也就是 `SortedSet、NavigableSet` 的注意事项和功能在 `TreeSet` 同样适用。