package Java_Protogenetic_API.Collection.Queue.PriorityQueque;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @ClassName Test01
 * @Description Priority 的使用
 * @Author 34194
 * @DATE 2025/12/16 9:25
 * @Version 1.0
 */
public class Test01 {
    public static void main(String[] args) {

        Queue<Integer> priorityQueue = new PriorityQueue<>();

        priorityQueue.offer(1);
        priorityQueue.offer(4);
        priorityQueue.offer(7);
        priorityQueue.offer(2);

        System.out.println("优先队列：" + priorityQueue);
        System.out.println("队列长度：" + priorityQueue.size());
        System.out.println("优先级最高元素：" + priorityQueue.peek());
        System.out.println("删除优先级高的元素：" + priorityQueue.poll());
        System.out.println("是否包含元素 7：" + (priorityQueue.contains(7) ? "包含" : "不包含"));

        // 场景1：传入数组容量不足
        Integer[] arr1 = new Integer[2];
        arr1 = priorityQueue.toArray(arr1);
        System.out.println(Arrays.toString(arr1)); // [2, 4, 7]

        // 场景2：传入数组容量足够且有余
        Integer[] arr2 = new Integer[5];
        arr2 = priorityQueue.toArray(arr2);
        System.out.println(Arrays.toString(arr2)); // [2, 4, 7,null,null]

        // 场景3：传入数组容量刚好
        Integer[] arr3 = new Integer[3];
        arr3 = priorityQueue.toArray(arr3);
        System.out.println(Arrays.toString(arr3)); // [2, 4, 7]

    }
}
