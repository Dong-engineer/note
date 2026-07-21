package Java_Protogenetic_API.Map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName Test01
 * @Description TODO: Map 接口的学习
 * @Author 34194
 * @DATE 2025/12/21 10:10
 * @Version 1.0
 */
public class Test01 {
    public static void main(String[] args) {

        Map<Integer, String> map = new HashMap<>();

        map.put(1, "我是第 1 个元素");
        map.put(2, "我是第 2 个元素");
        map.put(3, "我是第 3 个元素");
        map.put(4, "我是第 4 个元素");
        map.put(5, "我是第 5 个元素");

        System.out.println(map);
        System.out.println("map 集合是否为空：" + (map.isEmpty() ? "YES" : "NO"));
        System.out.println("map 集合长度：" + map.size());
        System.out.println("map 的键为 2 的值：" + map.get(2));
        System.out.println("map 中是否包含值为我第 5 个元素的键：" + (map.containsValue("我是第 5 个元素") ? "YES" : "NO"));
        System.out.println("删除键为 5 的元素：" + map.remove(5) + "已被删除");
        Set<Integer> set= map.keySet();
        System.out.println("map 中所有的键：" + set);
        Collection<String> collection = map.values();
        System.out.println("map 中所有的值：" + collection);

        map.forEach((index, value)-> System.out.println(index + ": " + value));

    }
}
