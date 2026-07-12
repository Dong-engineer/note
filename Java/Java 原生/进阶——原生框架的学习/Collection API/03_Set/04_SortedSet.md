# SortedSet

`SortedSet` 是一个接口，继承了 `Set` 。`Set` 的特性是——无序、唯一，而 `SortedSet` 的特性是——规则有序（按照开发者自定义的规则进行排序，默认是自然排序）、唯一。

在本节内容内容：

1. 学习并掌握 `SortedSet` 的可用方法。
2. 熟悉 `SortedSet` 是如何让元素做到有序。
3. 如何合理的使用 `SortedSet` 。

## 源码

```java
package java.util;

public interface SortedSet<E> extends Set<E> {

    // 比较器，如果是自然排序则默认返回 null
    Comparator<? super E> comparator();

    // 获取 fromElement 和 toElement 之间的元素集合，左闭右开
    SortedSet<E> subSet(E fromElement, E toElement);
    
    // 获取 toElement 之前的所有元素的集合，左闭右开
    SortedSet<E> headSet(E toElement);
    
	// 获取 fromElement 之后的所有元素的集合，左闭右开
    SortedSet<E> tailSet(E fromElement);
	
    // 获取最小的元素
    E first();
    
    // 获取最大的元素
    E last();
	
    // 该法放有兴趣的开发者可以自行搜寻资料，这里暂时不讲述，后面学习 Stream 并行时会讲述 Spliterator
    @Override
    default Spliterator<E> spliterator() {
        return new Spliterators.IteratorSpliterator<E>(
                this, Spliterator.DISTINCT | Spliterator.SORTED | Spliterator.ORDERED) {
            @Override
            public Comparator<? super E> getComparator() {
                return SortedSet.this.comparator();
            }
        };
    }
}
```

## 使用示例

```java
package Java_Protogenetic_API.Collection.Set.SortedSet;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @ClassName Test09
 * @Description SortedSet 的核心方法
 * @Author 34194
 * @DATE 2025/11/26 15:20
 * @Version 1.0
 */
public class Test01 {

    public static void main(String[] args) {

        SortedSet<Integer> sortedSet = new TreeSet<>();
        sortedSet.add(1);
        sortedSet.add(2);
        sortedSet.add(10);
        sortedSet.add(25);
        sortedSet.add(208);

        System.out.println(sortedSet);

        System.out.println("最小的元素：" + sortedSet.first());
        System.out.println("最大的元素：" + sortedSet.last());
        System.out.println("小于30的元素：" + sortedSet.headSet(30));
        System.out.println("大于30的元素：" + sortedSet.tailSet(30));
        System.out.println("10至30之间的元素：" + sortedSet.subSet(10, 30));

        // 默认自然排序为 null
        System.out.println("比较器：" + sortedSet.comparator());

        // 修改子集合，注意原集合会发生什么变化
        SortedSet<Integer> sortedSetChild = sortedSet.headSet(30);
        sortedSetChild.add(13);
        System.out.println("sortedSetChild：" + sortedSetChild);
        System.out.println("soredSet：" + sortedSet);
    }
}
```

```cmd
[1, 2, 10, 25, 208]
最小的元素：1
最大的元素：208
小于30的元素：[1, 2, 10, 25]
大于30的元素：[208]
10至30之间的元素：[10, 25]
比较器：null
```



```java
package Java_Protogenetic_API.Collection.Set.SortedSet;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @ClassName MyComparator
 * @Description 自定义比较器
 * @Author 34194
 * @DATE 2025/11/26 16:14
 * @Version 1.0
 * 注意这里在实现接口时不要忘记泛型
 */
class MyComparator1 implements Comparator<Integer> {

    public MyComparator1() {

    }
    /**
     * 实现 Comparator 的 compare 方法
     * @param x the first object to be compared.
     * @param y the second object to be compared.
     * @return 返回 Integer 的比较逻辑
     */
    @Override
    public int compare(Integer x, Integer y) {
        return Integer.compare(y, x);
    }
}

/**
 * @ClassName Test02
 * @Description SortedSet 的比较器
 * @Author 34194
 * @DATE 2025/11/26 16:14
 * @Version 1.0
 */
public class Test02 {

    public static void main(String[] args) {

        SortedSet<Integer> sortedSet = new TreeSet<>(new MyComparator1());
        sortedSet.add(1);
        sortedSet.add(10);
        sortedSet.add(100);
        sortedSet.add(1000);
        sortedSet.add(10000);

        System.out.println(sortedSet);

    }
}
```

```cmd
[10000, 1000, 100, 10, 1]
```



```java
package Java_Protogenetic_API.Collection.Set.SortedSet;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;


class MyComparator2 implements Comparator<User> {

    public MyComparator2(){}

    /**
     * 实现 Comparator 接口，重写 compare 方法的比较逻辑
     * @param user1 the first object to be compared.
     * @param user2 the second object to be compared.
     * @return 以用户年龄作为比较的基准，返回 Integer 的 compare() ，实现升序排序
     */
    @Override
    public int compare(User user1, User user2) {
        return Integer.compare(user1.getAge(), user2.getAge());
    }
}
/**
 * @ClassName Test03
 * @Description SortedSet 去重逻辑
 * @Author 34194
 * @DATE 2025/11/27 14:39
 * @Version 1.0
 */
public class Test03 {
    public static void main(String[] args) {

        SortedSet<User> sortedSet = new TreeSet<>(new MyComparator2());
        User user1 = new User("Dong", 18);
        User user2 = new User("J D", 18);
        User user3 = new User("D", 18);


        sortedSet.add(user1);
        sortedSet.add(user2);

        System.out.println(sortedSet);
    }
}
```

```cmd
[User{name='Dong', age=18}]
```



## 注意事项

1. `SortedSet` 的去重逻辑和 `Set` 的去重逻辑并不相同，在使用示例的 `Test03` 中不难发现在向 `sortedSet` 中添加的是3个 `User` 实例，而在打印输出后仅仅只有一个，这就是因为 `SortedSet` 的去重逻辑（这里的 `User` 类作者重写了 `equals()、hashCode()`）。
   `SortedSet` 的去重逻辑是并非和 `Set` 接口一样，其是由比较器来决定的，示例中作者仅判断了 `User` 示例的 `age` 属性，且每个实例的年龄都为 18，在 `Integer.compare()` 中对实例 `age` 进行比较（具体是如何比较的可以看源码），如果二者的比较结果为 `Integer.compare(user1.getAge(), user2.getAge()) == 0`（也就是说二者年龄相等），那么 `SortedSet` 就会判断这俩元素为重复值。
   这是 `SortedSet` 较坑的一点。
2. 在实际开发中开发者在自定义比较器时，其比较逻辑和结果应当以 `hashCode()、equals()` 二者返回的结果为基准，也就是说 `hashCode()、equals()` 判断两个实例为同一元素，那么在书写比较逻辑时这两个元素的比较结果要返回 0，避免出现哈希等值判定与排序判定逻辑冲突的问题。
3. 往 `SortedSet` 中添加自定义类型时，还需要自定义比较器，不然会抛出 `ClassCastException` ，IDEA 会警告开发者试图往 `SortedSet` 中添加不可比较类型（是警告哦！小白可能会忽略这点，因为有时候在有警告的情况下也可以运行程序，不过这样是错误的操作）。
4. 实例为 `null` 是不可以往 `SortedSet` 中添加元素，会抛出 `NullPointException` ，如果由需要那么开发者需要在自定义比较器中对 `null` 进行处理。
5. 线程安全，`SortedSet` 是逻辑上线程安全的，这得益其底层共享一个 `TreeMap` （这个 `TreeSet` 讲述）。
   但是开发者需要注意比较器，比较器也是线程安全的，但是在部分开发需求中，比较器中可能需要一些成员变量（这些变量不一定线程安全），比如比较器中需要记录比较次数，那么这时开发者就需要注意该成员变量是否线程安全了。
6. 数值溢出问题，在书写比较器时，一定要注意类型的范围值，如： `Integer` 等等。一般情况下通常是使用 Java 原生的 `Integer.compare()` （该方法 Java 官方帮开发者解决了类型的范围值溢出问题），有时需要其他的比较逻辑，开发者一定要注意在进行运算时不要超出类型的范围值。
7. 视图功能，在 `Test01` 中开发者不难发现修了原集合的子集合，那么其原集合的值也会随之修改改变，这同样都得益于其底层共享一个 `TreeMap`。
