package Java_Protogenetic_API.Collection.Queue.Deque;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @ClassName Test01
 * @Description Deque 的核心方法
 * @Author 34194
 * @Date 2025/12/1 15:19
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
    }
}
