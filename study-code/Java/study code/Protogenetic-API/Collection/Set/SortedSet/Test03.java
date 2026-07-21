package Java_Protogenetic_API.Collection.Set.SortedSet;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;


class MyComparator2 implements Comparator<User> {

    public MyComparator2(){}

    /**
     * 实现 Comparator 接口，重写 compare 方法的比较逻辑
     * @param user1 the first object to be compared.
     * @param user2 the second object to be compared.
     * @return 以用户年龄作为比较的基准，返回 Integer 的 compare() ，实现升序排序
     */
    @Override
    public int compare(User user1, User user2) {
        return Integer.compare(user1.getAge(), user2.getAge());
    }
}
/**
 * @ClassName Test03
 * @Description SortedSet 去重逻辑
 * @Author 34194
 * @DATE 2025/11/27 14:39
 * @Version 1.0
 */
public class Test03 {
    public static void main(String[] args) {

        SortedSet<User> sortedSet = new TreeSet<>(new MyComparator2());
        User user1 = new User("Dong", 18);
        User user2 = new User("J D", 18);
        User user3 = new User("D", 18);


        sortedSet.add(user1);
        sortedSet.add(user2);
        sortedSet.add(user3);

        System.out.println(sortedSet);
    }
}
