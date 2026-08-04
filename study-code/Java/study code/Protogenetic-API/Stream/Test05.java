package Java_Protogenetic_API.Stream;

import java.util.stream.Stream;

/**
 * @Description 创建一个持续产生的流
 * @Author Dong
 * @Date 2026/7/26 13:10
 */
public class Test05 {
    public static void main(String[] args) {
        // iterate()
        Stream<String> stringStream = Stream.iterate("+", p -> p + "+");
        stringStream.forEach(System.out::println);
    }
}
