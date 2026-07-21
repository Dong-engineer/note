# NavigableSet

`NavigableSet` 是 `SortedSet` 的扩展接口，即子接口。扩展接口，顾名思义在原来基础上额外增加了一些功能。

## 源码

```java
public interface NavigableSet<E> extends SortedSet<E> {
    /**
     * 返回小于指定元素的最大元素
     *
     * @param e 用于匹配对比的目标元素
     * @return 小于e的最大元素；不存在则返回null
     * @throws ClassCastException 若传入元素与集合元素类型无法比较
     * @throws NullPointerException 若集合不允许null元素，且传入参数为null
     */
    E lower(E e);

    /**
     * 返回小于或等于指定元素的最大元素
     *
     * @param e 用于匹配对比的目标元素
     * @return 小于等于e的最大元素；不存在则返回null
     * @throws ClassCastException 若传入元素与集合元素类型无法比较
     * @throws NullPointerException 若集合不允许null元素，且传入参数为null
     */
    E floor(E e);

    /**
     * 返回大于或等于指定元素的最小元素
     *
     * @param e 用于匹配对比的目标元素
     * @return 大于等于e的最小元素；不存在则返回null
     * @throws ClassCastException 若传入元素与集合元素类型无法比较
     * @throws NullPointerException 若集合不允许null元素，且传入参数为null
     */
    E ceiling(E e);

    /**
     * 返回大于指定元素的最小元素
     *
     * @param e 用于匹配对比的目标元素
     * @return 大于e的最小元素；不存在则返回null
     * @throws ClassCastException 若传入元素与集合元素类型无法比较
     * @throws NullPointerException 若集合不允许null元素，且传入参数为null
     */
    E higher(E e);

    /**
     * 获取并移除当前集合中最小的元素
     *
     * @return 集合最小元素；集合为空时返回null
     */
    E pollFirst();

    /**
     * 获取并移除当前集合中最大的元素
     *
     * @return 集合最大元素；集合为空时返回null
     */
    E pollLast();

    /**
     * 获取升序遍历集合元素的迭代器
     *
     * @return 升序迭代器
     */
    Iterator<E> iterator();

    /**
     * 返回当前集合的降序视图
     * <p>该视图与原集合共享底层数据，修改视图会同步影响原集合</p>
     *
     * @return 元素倒序排列的NavigableSet视图
     */
    NavigableSet<E> descendingSet();

    /**
     * 获取降序遍历集合元素的迭代器
     *
     * @return 降序迭代器
     */
    Iterator<E> descendingIterator();

    /**
     * 根据指定边界与开闭区间，截取集合返回子集合视图
     *
     * @param fromElement 区间起始边界元素
     * @param fromInclusive 是否包含起始元素，true包含，false不包含
     * @param toElement   区间结束边界元素
     * @param toInclusive   是否包含结束元素，true包含，false不包含
     * @return 指定区间范围内的NavigableSet视图
     * @throws ClassCastException 边界元素与集合元素无法比较时抛出
     * @throws NullPointerException 集合不允许null且边界元素为null时抛出
     * @throws IllegalArgumentException 起始边界大于结束边界时抛出
     */
    NavigableSet<E> subSet(E fromElement, boolean fromInclusive,
                           E toElement,   boolean toInclusive);

    /**
     * 截取集合头部区间，返回指定上限边界的子集合视图
     *
     * @param toElement 区间上限边界元素
     * @param inclusive 是否包含上限元素，true包含，false不包含
     * @return 小于（或小于等于）toElement的元素视图
     * @throws ClassCastException 边界元素与集合元素无法比较时抛出
     * @throws NullPointerException 集合不允许null且边界元素为null时抛出
     */
    NavigableSet<E> headSet(E toElement, boolean inclusive);

    /**
     * 截取集合尾部区间，返回指定下限边界的子集合视图
     *
     * @param fromElement 区间下限边界元素
     * @param inclusive   是否包含下限元素，true包含，false不包含
     * @return 大于（或大于等于）fromElement的元素视图
     * @throws ClassCastException 边界元素与集合元素无法比较时抛出
     * @throws NullPointerException 集合不允许null且边界元素为null时抛出
     */
    NavigableSet<E> tailSet(E fromElement, boolean inclusive);

    // 继承自SortedSet的方法，此处不再重复说明
    SortedSet<E> subSet(E fromElement, E toElement);

    SortedSet<E> headSet(E toElement);

    SortedSet<E> tailSet(E fromElement);
}
```

## 使用示例

```java
package Java_Protogenetic_API.Collection.Set.NavigableSet;

import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * @ClassName Test01
 * @Description NavigableSet 接口的核心方法
 * @Author 34194
 * @DATE 2025/11/30 12:58
 * @Version 1.0
 */
public class Test01 {
    public static void main(String[] args) {

        NavigableSet<Integer> navigableSet = new TreeSet<>();
        navigableSet.add(101);
        navigableSet.add(102);
        navigableSet.add(103);
        navigableSet.add(201);
        navigableSet.add(202);
        navigableSet.add(203);

        System.out.println(navigableSet);

        System.out.println("小于200的最大的元素：" + navigableSet.lower(200));
        System.out.println("小于等于200的最大的元素：" + navigableSet.floor(200));

        System.out.println("大于200的最小的元素：" + navigableSet.higher(200));
        System.out.println("大于等于200的最小的元素：" + navigableSet.ceiling(200));

        System.out.println("最小元素：" + navigableSet.pollFirst());
        System.out.println("最大元素：" + navigableSet.pollLast());

        System.out.println("移除最小元素、最大元素后的集合：" + navigableSet);

        System.out.println("小于等于103的元素集合：" + navigableSet.headSet(103, true));
        System.out.println("大于等于103的元素集合：" + navigableSet.tailSet(103, true));
        System.out.println("截取102-202中的所有元素的集合(包括102、202)：" + navigableSet.subSet(102, true, 202, true));
        
        System.out.println("按照降序排列：" + navigableSet.descendingSet());
    }
}
```

```
[101, 102, 103, 201, 202, 203]
小于200的最大的元素：103
小于等于200的最大的元素：103
大于200的最小的元素：201
大于等于200的最小的元素：201
最小元素：101
最大元素：203
移除最小元素、最大元素后的集合：[102, 103, 201, 202]
小于等于103的元素集合：[102, 103]
大于等于103的元素集合：[103, 201, 202]
截取102-202中的所有元素的集合(包括102、202)：[102, 103, 201, 202]
按照降序排列：[202, 201, 103, 102]
```

## 注意事项

`SortedSet` 要注意，`NavigableSet` 都要注意。