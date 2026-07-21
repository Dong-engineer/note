package Java_Protogenetic_API.Collection.List.LinkedList;


import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName Test03
 * @Description LinkedList 的核心方法
 * @Author 86198
 * @DATE 2025/9/28 16:14
 * @Version 1.0
 */
public class Test01 {

    public static void main(String[] args) {
        List<String> linkList = new LinkedList<>();
        String[] strings = {"你好", "中国", "Hello", "World", "!"};
        long addStartTime = System.nanoTime();
        linkList.add(strings[0]);
        linkList.add(strings[1]);
        linkList.add(strings[2]);
        linkList.add(strings[3]);
        linkList.add(strings[4]);
        long addEndTime = System.nanoTime();
        System.out.println("添加程序耗时：" + (addEndTime - addStartTime));
        System.out.println(linkList);
        System.out.println("====================");

        System.out.println("添加元素中（默认添加到尾元素），请稍等...");
        long indexStartTime = System.nanoTime();
        linkList.add(1, "dong");
        long indexEndTime = System.nanoTime();
        System.out.println("添加耗时:" + (indexEndTime - indexStartTime));
        System.out.println(linkList);
        System.out.println("====================");

        System.out.println("删除元素中，请稍等...");
        long delStartTime = System.nanoTime();
        linkList.remove(1);
        long delEndTime = System.nanoTime();
        System.out.println("添加耗时:" + (delEndTime - delStartTime));
        System.out.println(linkList);
        System.out.println("====================");

        System.out.println("查询元素中，请稍后...");
        long selStartTime = System.nanoTime();
        String elementOne= linkList.get(0);
        long selEndTime = System.nanoTime();
        System.out.println("查询结果：" + elementOne);
        System.out.println("查询耗时：" + (selEndTime - selStartTime));
        System.out.println("====================");

        System.out.println("替换元素中，请稍后...");
        long setStartTime = System.nanoTime();
        linkList.set(1,"China");
        long setEndTime = System.nanoTime();
        System.out.println(linkList);
        System.out.println("替换耗时：" + (setEndTime - setStartTime));
        System.out.println("====================");
    }

}
