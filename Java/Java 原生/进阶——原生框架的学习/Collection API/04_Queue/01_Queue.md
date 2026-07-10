# Queue

`Queue` 队列，这是队列的顶层接口，该接口定义队列的基本操作。

队列的特性就是“先进先出”，且对队列进行操作时，开发者只能在队列的两端操作。即一端添加元素一端删除元素（后面 Java 对该接口进行扩展，开发者可以在两端进行任何操作）。

## 源码

```java
public interface Queue<E> extends Collection<E> {
    /**
    * 将元素添加至队列中
    * return: 返回布尔值，成功为 true，失败为 false
    * throws: 
    * 		IllegalStateException => 由于队列容量限制，元素添加失败
    *		ClassCastException => 类型转换错误，通常是添加的元素与队列的泛型类型无法转换导致的
    *		NullPointException => 空指针错误，添加的元素值为 null
    *		IllegalArgumentException => 被添加元素的属性组织了该元素的添加
    */
    boolean add(E e);

    /**
    * 将元素添加至队列中，Java 官方是推荐使用 offer() 的，如果队列容量不足是不会抛出 IllegalStateException 的，会直接返回 false，且大部分队列的实现类的 add() 底层是依赖 offer() 来完成的，当开发需求有明确需要抛出异常时可以使用 add()
    * return: 返回布尔值，成功为 true，失败为 false
    * throws: 
    *		ClassCastException => 类型转换错误，通常是添加的元素与队列的泛型类型无法转换导致的
    *		NullPointException => 空指针错误，添加的元素值为 null
    *		IllegalArgumentException => 被添加元素的属性组织了该元素的添加
    */
    boolean offer(E e);

    /**
    * 删除队列的第一个元素，如果队列为空则抛出异常，底层依赖 poll()
    * return: 返回被删除的元素
    * throws: 
    *		NoSuchElementException => 如果队列为空
    */
    E remove();

    /**
    * 删除队列的第一个元素，如果队列为空直接返回 null
    * return: 返回被删除的元素
    */
    E poll();

    /**
    * 查看队列的第一个元素，底层依赖 peek()
    * return: 返回队列的第一个元素
    * throws: 
    *		NoSuchElementException => 如果队列为空
    */
    E element();

    /**
    * 查看队列的第一个元素，如果队列为空直接返回 null
    * return: 返回队列的第一个元素
    */
    E peek();
}
```

## 使用示例

```java
package Java_Protogenetic_API.Collection.Queue;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @ClassName Test01
 * @Description Queue的核心方法
 * @Author 34194
 * @DATE 2025/12/1 15:16
 * @Version 1.0
 */
public class Test01 {
    public static void main(String[] args) {
        // 这里的 ArrayBlockingQueue 是一个实现类——有界队列
        Queue<Integer> queue = new ArrayBlockingQueue<>(5);

        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);
        if (!queue.offer(6)) System.out.println("队列已满");

        System.out.println("队列的首元素：" + queue.peek());
        System.out.println(queue);

        Queue<Integer> nullQueue = new ArrayBlockingQueue<>(5);

        System.out.println("这是一个为 null 的队列" + nullQueue);
        try {
            nullQueue.remove();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
```

## 注意事项

队列的注意事项放在后面的的每个实现类讲述，在接口中讲述不合适。
