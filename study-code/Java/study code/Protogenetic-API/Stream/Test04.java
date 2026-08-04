package Java_Protogenetic_API.Stream;

import java.util.stream.Stream;

/**
 * @Description 创建一个永不停歇的流对象
 * @Author Dong
 */
public class Test04 {
    public static void main(String[] args) {
        // generate() 创建的流对象，不会自行关闭，且该流是无序的
        // 如果开发者不使用 limit() ，限制流的大小，那么流会一直增加内存的占用量，直到内存溢出或者 JVM 的 heap 被占满
        Stream<Integer> intStream = Stream.generate(() -> 1);

        System.out.println(intStream.toList()); // OutOfMemoryError
    }
}
