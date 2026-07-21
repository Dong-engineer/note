package Java_Protogenetic_API.Collection.List;

import java.util.*;

/**
 * @ClassName Test05
 * @Description List 迭代机制
 * @Author 86198
 * @DATE 2025/10/14 17:22
 * @Version 1.0
 */
public class Test01 {
      public static void main(String[] args) {
          List<String> list = new ArrayList<>();
          list.add("A");
          list.add("B");
          list.add("C");
          list.add("D");

          Iterator<String> iterator = list.iterator();

//          while (iterator.hasNext()) {
//              String item = iterator.next(); // 触发检查
//
//              if ("C".equals(item)) {
//                  // 在迭代时，通过集合的方法修改结构，会导致 modCount 不一致
//                  list.remove(item); // 抛出 ConcurrentModificationException
//              }
//              System.out.println(item);
//          }
          while (iterator.hasNext()) {
              String item = iterator.next(); // 触发检查，如果不一致会抛出 ConcurrentModificationException

              if ("C".equals(item)) {
                  // 记住要使用迭代器的修改方法！！！
                  iterator.remove();
              }
              System.out.println(item);
          }

          System.out.println(list);
      }
}
