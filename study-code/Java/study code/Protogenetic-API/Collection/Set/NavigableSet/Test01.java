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
