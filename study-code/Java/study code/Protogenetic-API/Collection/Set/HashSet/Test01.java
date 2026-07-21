package Java_Protogenetic_API.Collection.Set.HashSet;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName Test09
 * @Description HashSet 的核心方法
 * @Author 34194
 * @DATE 2025/11/8 14:32
 * @Version 1.0
 */
public class Test01 {
    public static void main(String[] args) {
        Set<String> set  = new HashSet<>();
        set.add("《结尾》");
        set.add("《那由她》");
        set.add("《小半》");
        set.add("《半点心》");
        set.add("《桃花朵朵开》");

        System.out.println("set 集合初始长度：" + set.size());
        System.out.println("set 集合是否包含歌曲《星》：" + (set.contains("《星》") ? "YES" : "NO"));

        System.out.println("正在添加歌曲《星》...");
        set.add("《星》");
        System.out.println("添加完成");
        System.out.println(set);

        System.out.println("正在删除歌曲《星》...");
        set.remove("《星》");
        System.out.println("删除完成");
        System.out.println(set);

    }
}
