package Java_Protogenetic_API.Stream;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @Description 从数值范围创建流
 * @Author Dong
 */
public class Test07 {
    public static void main(String[] args) {
        String[] strs = {"a", "b", "c", "d"};

        List<String> listLetter = IntStream
                .range(0, 10)
                .mapToObj(index -> strs[index % strs.length])
                .toList();

        listLetter.forEach(System.out::println);
    }
}
