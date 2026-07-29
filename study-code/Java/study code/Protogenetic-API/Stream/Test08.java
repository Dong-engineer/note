package Java_Protogenetic_API.Stream;

import java.util.List;
import java.util.Random;

/**
 * @Description 从随机数中创建流对象
 * @Author Dong
 */
public class Test08 {
    public static void main(String[] args) {
        Random random = new Random();

        List<Integer> randomList = random
                .ints(10, 1, 5)
                .boxed()
                .toList();

        randomList.forEach(System.out::println);
    }
}
