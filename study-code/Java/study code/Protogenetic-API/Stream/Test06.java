package Java_Protogenetic_API.Stream;

import java.util.List;

/**
 * @Description 从字符串创建流
 * @Author Dong
 * @Date 2026/7/26 13:25
 */
public class Test06 {
    public static void main(String[] args) {
        String str = "test stream";

        List<String> stringList = str
                .chars()
                .mapToObj(s -> (char) s)
                .map(Object::toString)
                .toList();

        stringList.forEach(System.out::println);
    }
}
