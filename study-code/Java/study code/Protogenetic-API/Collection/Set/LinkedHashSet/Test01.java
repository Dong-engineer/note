package Java_Protogenetic_API.Collection.Set.LinkedHashSet;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * @ClassName Test08
 * @Description LinkedHashSet
 * @Author 34194
 * @DATE 2025/11/9 21:50
 * @Version 1.0
 */
public class Test01 {
    public static void main(String[] args) {

        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();

        linkedHashSet.add("2025");
        linkedHashSet.add("年");
        linkedHashSet.add("恭喜");
        linkedHashSet.add("发财");

        System.out.println(linkedHashSet);

        System.out.println("正在添加...");
        linkedHashSet.add("红红火火");
        System.out.println("添加完成>>>");
        System.out.println(linkedHashSet);

        System.out.println("linkedHashSet 中是否包含红红火火：" + (linkedHashSet.contains("红红火火") ? "YES" : "NO"));

        System.out.println("正在删除...");
        linkedHashSet.remove("红红火火");
        System.out.println("添加完成>>>");

        System.out.println("linkedHahSet 中是否红红火火：" + (linkedHashSet.contains("红红火火") ? "YES" : "NO"));

        System.out.println("正在遍历linkedHashSet集合...");
        Iterator<String> iterator = linkedHashSet.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            System.out.println(element);
        }
        System.out.println("遍历完成>>>");

    }
}
