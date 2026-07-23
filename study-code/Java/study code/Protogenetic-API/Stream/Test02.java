package Java_Protogenetic_API.Stream;

import java.util.stream.Stream;

/**
 * @Description 动态参数（工厂方法）创建 Stream 流对象
 * @Author Dong
 */
public class Test02 {

    public static void main(String[] args) {
        // Stream API 提供了工厂方法，开发者可以更加灵活的创建 Stream 对像
        Stream<Integer> intStream = Stream.of(1, 2, 3, 4);
        Stream<Character> charStream = Stream.of('1', '2', '3');
        Stream<String> strStream = Stream.of("China", "Beijing", "HaiDian");

        System.out.println(intStream.toList());
        System.out.println(charStream.toList());
        System.out.println(strStream.toList());
    }
}
