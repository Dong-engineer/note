# ArrayList

`ArrayList` 动态的 `List` ，动态数组开发者应该并不陌生， `ArrayList` 的底层实现就是由动态数组完成的，后面源码会讲述到以及其是如何完成动态数组扩展的。

这也表明了 `ArrayList` 具有数组的一切特性，连续性、有序性、可重复性。

## 源码

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

    // 1. 底层存储元素的数组
    transient Object[] elementData;

    // 2. 集合中实际包含的元素数量
    private int size;

    // 3. 默认初始容量
    private static final int DEFAULT_CAPACITY = 10;

    // 4. 添加元素的核心方法
    public boolean add(E e) {
        // 确保内部数组有足够的容量
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        // 将元素添加到数组末尾
        elementData[size++] = e;
        return true;
    }

    // 5. 确保容量的核心逻辑
    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    private void ensureExplicitCapacity(int minCapacity) {
        modCount++; // 修改计数器，用于检测并发修改

        // 如果所需容量超过了当前数组的长度，则需要扩容
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    // 6. 扩容的核心方法
    private void grow(int minCapacity) {
        
        int oldCapacity = elementData.length;
        // 新容量 = 旧容量 + (旧容量 >> 1)，即大约是原来的 1.5 倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        
        // 创建一个新的、更大的数组，并将旧数组的内容复制过去
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    // 7. 获取元素的方法 (非常高效)
    public E get(int index) {
        
        // 检查索引是否越界
        rangeCheck(index);
        
        // 直接通过数组索引返回元素
        return elementData(index);
    }

    // 8. 在指定位置添加元素 (相对低效)
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        ensureCapacityInternal(size + 1);
        // 将 index 及其后的所有元素向后移动一位
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        // 在空出的位置插入新元素
        elementData[index] = element;
        size++;
    }
    
    // 还有很多方法，其具体如何实现可以观看源码，这里不再书写，因为太多了
}
```

从上面源码中可以看出，`ArrayList` 的初始容量为10，后续的扩容是原来的1.5倍，这点需要注意下。

## 使用实例

```java
package Mastering_the_API.Collection;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Test02
 * @Description TODO
 * @Author 86198
 * @DATE 2025/9/28 15:12
 * @Version 1.0
 */
public class Test02 {

    public static void main(String[] args) {

        List<String> arrayList = new ArrayList<>();
        String[] strings = {"你好", "中国", "Hello", "World", "!"};

        // 添加元素
        long addStartTime = System.nanoTime();
        arrayList.add(strings[0]);
        arrayList.add(strings[1]);
        arrayList.add(strings[2]);
        arrayList.add(strings[3]);
        arrayList.add(strings[4]);
        long addEndTime = System.nanoTime();
        System.out.println("添加程序耗时：" + (addEndTime - addStartTime));
        System.out.println("=============================");

        // 打印输出
        System.out.println(arrayList);
        System.out.println("集合长度：" + arrayList.size());
        System.out.println("=============================");

        // 指定位置添加
        long startTime_add = System.nanoTime();
        arrayList.add(1, "dong");
        long endTime_add = System.nanoTime();
        System.out.println("添加程序耗时：" + (endTime_add - startTime_add));
        System.out.println(arrayList);
        System.out.println("=============================");

        long startTime_remove = System.nanoTime();
        arrayList.remove(1);
        long endTime_remove = System.nanoTime();
        System.out.println("删除程序耗时：" + (endTime_remove - startTime_remove));
        System.out.println(arrayList);
        System.out.println("=============================");

        long startTime_set = System.nanoTime();
        arrayList.set(1,"China");
        long endTime_set = System.nanoTime();
        System.out.println("替换程序耗时：" + (endTime_set - startTime_set));
        System.out.println(arrayList);
        System.out.println("=============================");

    }

}	
```

```cmd
添加程序耗时：6700
=============================
[你好, 中国, Hello, World, !]
集合长度：5
=============================
添加程序耗时：10400
[你好, dong, 中国, Hello, World, !]
=============================
删除程序耗时：6700
[你好, 中国, Hello, World, !]
=============================
替换程序耗时：6200
[你好, China, Hello, World, !]
=============================
```

这里做了下时间测试，因为后面还要讲述 `LinkedList` ，二者对比过后就会发现差距，因为二者底层存储结构实现不同的缘故，这也就到这二者在进行不同操作时所耗时间也不同。

## 注意事项

1. 线程安全问题，`ArrayList` 线程是不安全的，这里开发者先知道，其线程是不安全的，后面会讲述线程安全的实现类
2. 迭代器修改 `ArrayList` 问题，在 *04_Collection* 中讲述过，当开发者使用迭代器遍历元素时，需要对元素进行添加、删除、修改等问题会发生 `ConcurrentModificationException` 异常。
3. 性能开销问题，`ArrayList` 底层有动态数组实现，这也就意味这数组的频繁扩容会导致性能开销很大，所以在创建 `ArrayList` 对象时，开发者可以尽量创建足够的容量空间避免性能的浪费。

