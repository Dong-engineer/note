package Java_Protogenetic_API.Collection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @ClassName Test01
 * @Description Collection 核心方法
 * @Author 34194
 * @DATE 2025/12/8 15:35
 * @Version 1.0
 */
public class Test01{
    public static void main(String[] args) {

        // 创建实例
        Collection<String> fruits = new ArrayList<>();

        // 添加数据
        fruits.add("苹果");
        fruits.add("香蕉");
        fruits.add("梨");

        // 输出
        System.out.println(fruits);

        // 基本操作
        System.out.println(fruits.size()); // 3
        System.out.println(fruits.isEmpty()); // false
        System.out.println(fruits.contains("猕猴桃")); // false

    }
}
