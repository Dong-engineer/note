package Java_Protogenetic_API.Stream;

import java.util.*;
import java.util.stream.Stream;

/**
 * @Description 获取 Collection 集合的 Stream 流对象
 * @Author Dong
 */
public class Test01 {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        Set<String> set = new HashSet<>();
        set.add("1");
        set.add("2");
        set.add("3");
        set.add("4");

        // 倒序排序
        Stream<Integer> reverse = list
                .stream()
                .sorted(Comparator.reverseOrder());
        reverse.forEach(System.out::println);

        Stream<String> setStream = set.stream();
        setStream.forEach(System.out::println);
    }
}
