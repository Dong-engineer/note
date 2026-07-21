package Java_Protogenetic_API.Collection.Set;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName Test07
 * @Description Set 的核心方法
 * @Author 86198
 * @DATE 2025/10/22 16:02
 * @Version 1.0
 */
public class Test01 {
    public static void main(String[] args) {

        Set<String> set = new HashSet<>();
        // 添加程序
        set.add("你好");
        set.add("世界");
        set.add("!");
        set.add("你好");

        System.out.println(set);
        System.out.println("set 集合长度为：" + set.size());
        System.out.println("set 集合是否为空：" + (set.isEmpty() ? "是": "否"));

        System.out.println("set 集合移除元素 '!'：" + (set.remove("!") ? "成功" : "失败"));
        System.out.println("set 是否包含元素 '!'：" + (set.contains("!") ? "是" : "否"));
        System.out.println(set);

    }
}
