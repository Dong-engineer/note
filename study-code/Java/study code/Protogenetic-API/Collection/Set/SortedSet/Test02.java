package Java_Protogenetic_API.Collection.Set.SortedSet;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @ClassName MyComparator
 * @Description 自定义比较器
 * @Author 34194
 * @DATE 2025/11/26 16:14
 * @Version 1.0
 * 注意这里在实现接口时不要忘记泛型
 */
class MyComparator1 implements Comparator<Integer> {

    public MyComparator1() {

    }
    /**
     * 实现 Comparator 的 compare 方法
     * @param x the first object to be compared.
     * @param y the second object to be compared.
     * @return 返回 Integer 的比较逻辑
     */
    @Override
    public int compare(Integer x, Integer y) {
        return Integer.compare(y, x);
    }
}

/**
 * @ClassName Test02
 * @Description SortedSet 的比较器
 * @Author 34194
 * @DATE 2025/11/26 16:14
 * @Version 1.0
 */
public class Test02 {

    public static void main(String[] args) {

        SortedSet<Integer> sortedSet = new TreeSet<>(new MyComparator1());
        sortedSet.add(1);
        sortedSet.add(10);
        sortedSet.add(100);
        sortedSet.add(1000);
        sortedSet.add(10000);

        System.out.println(sortedSet);

    }
}
