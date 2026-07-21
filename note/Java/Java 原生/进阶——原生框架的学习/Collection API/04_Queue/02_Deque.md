# Deque

`Deque`（双端队列） 接口有些特殊，他不仅仅是 `Queue` 接口的扩展接口，其还是 `Stack` 的替代类，在官方的介绍中 `Stack` 类是 `Vector` 类的扩展类，但是 Java 官方是不建议开发者再去使用 `Vector` 该类的，继而 `Stack` 类也不建议再去使用。

`Deque` 则是 `Stack` 的替代类，该接口不仅可以作为队列，也可以作为堆栈，但是要注意两种数据结构各自方法的合理调用。

`Deque` 在 `Queue` 的基础上进行了扩展，一般队列在两端进行操作一端出队，一端入队，但 `Deque` 允许开发者在两端的每一端都可以进行出队和入队操作。

## 源码

```java
public interface Deque<E> extends Queue<E> {
    
    /**
    * 在队首添加元素，如果超出队列容量会抛出异常，底层依赖 offerFirst() 
    * throws:
    * 		IllegalStateException => 队列容量已满，无法添加元素抛出异常
    * 		ClassCastException	=> 类型转换错误
    * 		NullPointerException => 空指针错误
    * 		IllegalArgumentException => 添加的元素因自身属性原因，无法成功添加
    */
    void addFirst(E e);

    /**
    * 在队尾添加元素，如果超出队列容量会抛出异常，底层依赖 offerLast()
    * throws:
    * 		IllegalStateException => 队列容量已满，无法添加元素抛出异常
    * 		ClassCastException	=> 类型转换错误
    * 		NullPointerException => 空指针错误
    * 		IllegalArgumentException => 添加的元素因自身属性原因，无法成功添加
    */
    void addLast(E e);

    /**
    * 在队首添加元素，失败不会抛出一异常，而会直接返回 false
    * throws:
    * 		ClassCastException	=> 类型转换错误
    * 		NullPointerException => 空指针错误
    * 		IllegalArgumentException => 添加的元素因自身属性原因，无法成功添加
    */
    boolean offerFirst(E e);

	/**
    * 在队尾添加元素，失败不会抛出一异常，而会直接返回 false
    * throws:
    * 		ClassCastException	=> 类型转换错误
    * 		NullPointerException => 空指针错误
    * 		IllegalArgumentException => 添加的元素因自身属性原因，无法成功添加
    */
    boolean offerLast(E e);

    /**
    * 移除队首元素，底层依赖 pollFirst()
    * return: 返回被移除的元素
    * throws:
    * 		NoSuchElementException => 队列为空时，抛出异常
    */
    E removeFirst();

    /**
    * 移除队尾元素，底层依赖 pollLast()
    * return: 返回被移除的元素
    * throws:
    * 		NoSuchElementException => 队列为空时，抛出异常
    */
    E removeLast();

    /**
    * 删除队首元素，队列为空直接返回 false
    * return: 返回被移除的元素
    */
    E pollFirst();

    /**
    * 删除队尾元素，队列为空直接返回 false
    * return: 返回被移除的元素
    */
    E pollLast();

    /**
    * 获取队首元素，底层依赖 peekFirst()
    * return: 返回被移除的元素
    * throws:
    * 		NoSuchElementException => 当队列中为空时，抛出该异常
    */
    E getFirst();
    
    /**
    * 获取队尾元素，底层依赖 peekLast()
    * return: 返回被移除的元素
    * throws:
    * 		NoSuchElementException => 当队列中为空时，抛出该异常
    */
    E getLast();
    
    /**
    * 获取队首元素，如果队列为空，直接返回 false
    */
    E peekFirst();
    
    /**
    * 获取队尾元素，如果队列为空，直接返回 false
    */
    E peekLast();
    
    /**
    * 删除指定元素，当有多个选项时，删除第一个
    * return: true or false
    * throws:
    * 		ClassCastException	=> 类型转换错误
    * 		NullPointerException => 空指针错误
    */
    boolean removeFirstOccurrence(Object o);
    
    /**
    * 删除指定元素，当有多个选项时，删除最后一个
    * throws:
    * 		ClassCastException	=> 类型转换错误
    * 		NullPointerException => 空指针错误
    */
    boolean removeLastOccurrence(Object o);
    
    boolean add(E e);
    
    boolean offer(E e);
    
    E remove();
    
    E poll();
    
    E peek();
    
    /**
    * 向队列中添加一个集合
    * throws:
    * 		IllegalStateException => 队列容量已满，无法添加元素抛出异常
    * 		ClassCastException	=> 类型转换错误
    * 		NullPointerException => 空指针错误
    * 		IllegalArgumentException => 添加的元素因自身属性原因，无法成功添加
    */
    boolean addAll(Collection<? extends E> c);
    
    /**  堆栈方法  **/
    /**
    * 入栈操作
    * throws:
    * 		IllegalStateException => 队列容量已满，无法添加元素抛出异常
    * 		ClassCastException	=> 类型转换错误
    * 		NullPointerException => 空指针错误
    * 		IllegalArgumentException => 添加的元素因自身属性原因，无法成功添加
    */
    void push(E e);
    
    /**
    * 出栈操作
    * throws:
    * 		NoSuchElementException => 栈为空时抛出
    */
    E pop();
    
    /**
    * 删除指定元素
    * throws:
    * 		ClassCastException	=> 类型转换错误
    * 		NullPointerException => 空指针错误
    */
    boolean remove(Object o);
    
    /**  通用方法  **/
    /**
    * 判断栈中是否包含目标元素
    * throws:
    * 		ClassCastException => 类型转换错误
    * 		NullPointException => 空指针错误
    */
    boolean contains(Object o);
    
    /**
    * 返回元素长度
    */
    int size();

    /**
    * 获取迭代器
    */
    Iterator<E> iterator();

   	/**
    * 获取反序迭代器
    */
    Iterator<E> descendingIterator();

}
```

## 使用示例

```java
package Java_Protogenetic_API.Collection.Queue.Deque.ArrayDeque;

import java.util.ArrayDeque;
import java.util.Deque;
// import java.util.concurrent.BlockingDeque;
// import java.util.concurrent.LinkedBlockingDeque;

/**
 * @ClassName Test01
 * @Description Deque 的核心方法
 * @Author 34194
 * @DATE 2025/12/1 15:19
 * @Version 1.0
 */
public class Test01 {
    public static void main(String[] args) {

        // 队列的方法使用
        Deque<Integer> deque = new ArrayDeque<>(5);

        // 如果开发者希望发生异常可以使用 BlockingDeque。
        // BlockingDeque<Integer> deque = new LinkedBlockingDeque<>(5);
        System.out.println("==========add() 添加元素，底层依赖 offer()==========");
        deque.add(1);
        deque.add(2);
        deque.add(3);
        // 添加至队首
        deque.addFirst(0);
        // 添加至队尾
        deque.addLast(8);
        System.out.println("我是一个队列：" + deque);

        try {
            deque.addFirst(10);
            System.out.println("使用 addFirst() 将元素 10 添加到队首");
            System.out.println("deque 的长度：" + deque.size());
            System.out.println("这里逻辑上会发生异常，但是 ArrayDeque 会自动扩容，所以实际这里不会发生异常");
        } catch(Exception e) {
            System.out.println("发生异常，超出了队列长度");
            e.printStackTrace();
            System.out.println("异常类型为：" + e.getClass());
        }
        System.out.println("==========offer() 添加元素==========");
        System.out.println("综上，Java 官方并不希望开发者在实际开发时去使用 add() 方法，而是使用 offer() 方法，这样开发者可以优雅的对队列进行判断");
        System.out.println("使用 offerFirst() 将元素 12 添加到队首");
        System.out.println(deque.offerFirst(12) ? "添加成功" : "添加失败" );
        System.out.println(deque);

        System.out.println("==========remove() 删除元素，底层依赖 poll()==========");
        deque.removeFirst();
        deque.removeLast();
        System.out.println(deque);

        System.out.println("==========poll() 删除元素==========");
        deque.pollFirst();
        deque.pollLast();
        System.out.println(deque);

        System.out.println("==========get() 查看元素元素，底层依赖 peek()==========");
        System.out.println(deque.getFirst());
        System.out.println(deque.getLast());

        System.out.println("==========peek() 查看元素元素==========");
        System.out.println(deque.peekFirst());
        System.out.println(deque.peekLast());

        System.out.println("==========补充==========");
        /* 因为 Deque 接口扩展的 Queue 接口，而 Queue 接口扩展 Collection 接口
        * 所以在 Deque 的源码中是含有 add(E e)、offer(E e)、remove(E e)这三种方法的
        * 在队列容量允许的情况下，开发者是可以对队列中间的元素进行操作的，但这里作者建议不要那样做，
        * 这样做的话违背了队列的特性。
        * */
        
        // 这里就结束了，关于 Deque 作为栈的使用，请保持耐心，继续学习吧
    }
}
```

## 注意事项

1. 明确 `Deque` 的使用方式，如果作为队列那么请按队列的特性对其进行操作，如果作为栈那么请按栈的特性对其进行操作，这很重要！！！
2. 同样的关于线程的问题，放在后面实现类讲述。
