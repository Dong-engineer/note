package Java_Protogenetic_API.Collection.Set.TreeSet;

import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * @ClassName Test01
 * @Description TreeSet的核心方法
 * @Author 34194
 * @Date 2025/12/1 10:02
 * @Version 1.0
 */
public class Test01 {
    public static void main(String[] args) {
        NavigableSet<Integer> treeSet = new TreeSet<>();

        for (int i = 1; i < 101; i ++) {
            treeSet.add(100 + i);
        }

        System.out.println("小于150的最大元素：" + treeSet.lower(150));
        System.out.println("小于等于150的最大元素：" + treeSet.floor(150));

        System.out.println("大于150的最小元素：" + treeSet.higher(150));
        System.out.println("大于等于150的最小元素：" + treeSet.ceiling(150));

        System.out.println("截取120 —— 180(左闭右开)之间的元素：" + treeSet.subSet(120, 180));

        System.out.println("移除126：" + (treeSet.remove(126) ? "成功" : "失败"));
        System.out.println("是否包含126：" + (treeSet.contains(126) ? "包含" : "不包含"));
    }
}
