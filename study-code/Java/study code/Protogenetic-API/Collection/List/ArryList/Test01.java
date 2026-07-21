package Java_Protogenetic_API.Collection.List.ArryList;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Test02
 * @Description Arraylist 的核心方法
 * @Author 86198
 * @DATE 2025/9/28 15:12
 * @Version 1.0
 */
public class Test01 {

    public static void main(String[] args) {

        List<String> arrayList = new ArrayList<>();
        String[] strings = {"你好", "中国", "Hello", "World", "!"};

        // 添加元素
        long addStartTime = System.nanoTime();
        arrayList.add(strings[0]);
        arrayList.add(strings[1]);
        arrayList.add(strings[2]);
        arrayList.add(strings[3]);
        arrayList.add(strings[4]);
        long addEndTime = System.nanoTime();
        System.out.println("添加程序耗时：" + (addEndTime - addStartTime));
        System.out.println("=============================");

        // 打印输出
        System.out.println(arrayList);
        System.out.println("集合长度：" + arrayList.size());
        System.out.println("=============================");

        // 指定位置添加
        long startTime_add = System.nanoTime();
        arrayList.add(1, "dong");
        long endTime_add = System.nanoTime();
        System.out.println("添加程序耗时：" + (endTime_add - startTime_add));
        System.out.println(arrayList);
        System.out.println("=============================");

        long startTime_remove = System.nanoTime();
        arrayList.remove(1);
        long endTime_remove = System.nanoTime();
        System.out.println("删除程序耗时：" + (endTime_remove - startTime_remove));
        System.out.println(arrayList);
        System.out.println("=============================");

        long startTime_set = System.nanoTime();
        arrayList.set(1,"China");
        long endTime_set = System.nanoTime();
        System.out.println("替换程序耗时：" + (endTime_set - startTime_set));
        System.out.println(arrayList);
        System.out.println("=============================");

    }

}
