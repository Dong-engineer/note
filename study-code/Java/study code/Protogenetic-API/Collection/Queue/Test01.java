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
