# List

`List` 接口继承了 `Collection` ，在 *02_集合的层次结构* 一节中，开发者可以知道，与其同级的还有 `Set` 接口。

`List` 主要存储有序、重复的元素。

`Set` 后续讲述。

学习过数据结构的开发者对 `List` 也许并不陌生，下面就讲述其源码。

## 源码

```java
public interface List<E> extends Collection<E> {
    // 1. 基本操作
    int size();
    boolean isEmpty();
    boolean contains(Object o);
    Iterator<E> iterator();
    Object[] toArray();
    <T> T[] toArray(T[] a);
    boolean add(E e); // 在末尾添加
    boolean remove(Object o);
    boolean containsAll(Collection<?> c);
    boolean addAll(Collection<? extends E> c); // 在末尾批量添加
    boolean removeAll(Collection<?> c);
    boolean retainAll(Collection<?> c);
    void clear();
    boolean equals(Object o);
    int hashCode();

    // 2. List 特有：索引操作
    /**
     * 返回指定索引处的元素。
     */
    E get(int index);

    /**
     * 用指定元素替换指定索引处的元素。
     */
    E set(int index, E element);

    /**
     * 在指定位置插入指定元素，后续元素后移。
     */
    void add(int index, E element);

    /**
     * 删除并返回指定索引处的元素，后续元素前移。
     */
    E remove(int index);

    /**
     * 返回指定元素第一次出现的索引。
     */
    int indexOf(Object o);

    /**
     * 返回指定元素最后一次出现的索引。
     */
    int lastIndexOf(Object o);

    // 3. List 特有：迭代器
    /**
     * 返回一个 ListIterator，可以双向遍历和修改。
     */
    ListIterator<E> listIterator();

    /**
     * 从指定索引开始，返回一个 ListIterator。
     */
    ListIterator<E> listIterator(int index);

    /**
     * 返回一个子列表（视图），从 fromIndex (包含) 到 toIndex (不包含)。
     */
    List<E> subList(int fromIndex, int toIndex);

    // 4. JDK 8+ 新增默认方法
    /**
     * 用函数的结果替换每个元素。
     */
    default void replaceAll(UnaryOperator<E> operator) { ... }

    /**
     * 根据指定的比较器对 List 进行排序。
     */
    default void sort(Comparator<? super E> c) { ... }
}
```

关于 `List` 接口的内容，就在此结束了，本节内容和前面的相比，少之又少，最初作者准备在本节讲述 `List` 接口的使用示例，还有其方法的具体实现、时间复杂度、注意事项等等，但感觉这些东西还是放在具体实现类去讲述好一些。

后面两节会讲述 `ArrayList、LinkedList` 两个 `List` 接口的实现类，开发者可以自行先学习，也可以跟随作者脚步往下学习。