package Java_Protogenetic_API.Collection.Set.SortedSet;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @ClassName Test09
 * @Description SortedSet 的核心方法
 * @Author 34194
 * @DATE 2025/11/26 15:20
 * @Version 1.0
 */
public class Test01 {

    public static void main(String[] args) {

        SortedSet<Integer> sortedSet = new TreeSet<>();
        sortedSet.add(1);
        sortedSet.add(2);
        sortedSet.add(10);
        sortedSet.add(25);
        sortedSet.add(208);

        System.out.println(sortedSet);

        System.out.println("最小的元素：" + sortedSet.first());
        System.out.println("最大的元素：" + sortedSet.last());
        System.out.println("小于30的元素：" + sortedSet.headSet(30));
        System.out.println("大于30的元素：" + sortedSet.tailSet(30));
        System.out.println("10至30之间的元素：" + sortedSet.subSet(10, 30));

        // 默认自然排序为 null
        System.out.println("比较器：" + sortedSet.comparator());

        // 修改子集合，注意原集合会发生什么变化
        SortedSet<Integer> sortedSetChild = sortedSet.headSet(30);
        sortedSetChild.add(13);
        System.out.println("sortedSetChild：" + sortedSetChild);
        System.out.println("soredSet：" + sortedSet);
    }
}
