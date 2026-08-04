package Java_Protogenetic_API.Stream;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description Stream 的 build 模式
 * @Author Dong
 * @Date 2026/8/2 12:35
 */
public class Test09 {
    public static void main(String[] args) {
        // Stream 提供了建造者模式
        // 创建建造者
        Stream.Builder<String> strStream = Stream.<String>builder();

        // 预添加数据
        strStream.add("one");
        strStream.add("two");
        strStream.add("three");
        strStream.add("four");

        // build() 创建流对象，collect() 调用终端操作收集流并转化为数据结构对象
        System.out.println(strStream.build().collect(Collectors.toList()));
    }
}
