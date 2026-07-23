package Java_Protogenetic_API.Stream;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @Description 数组创建 Stream 对象
 * @Author Dong
 */
public class Test03 {

    public static void main(String[] args) {
        String[] arr = {"1", "2", "3"};
        Stream<String> intStream = Arrays.stream(arr);

        System.out.println(intStream.toList());
    }
}
