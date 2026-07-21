# LinkedHashSet

`LinkedHashSet` 其在 `HashSet` 原来的基础上修改了无序的特性，也就是说 `LinkedHashSet` 是自然有序的。

这是 `LinkedHashSet` 与 `HashSet` 的最大区别，除此之外二者几乎一样，其添加、删除、查询操作都是 O(n)。

## 源码

```java
public class LinkedHashSet<E>
    extends HashSet<E>
    implements Set<E>, Cloneable, java.io.Serializable {

    public LinkedHashSet() {
        super(16, .75f, true);
    }

    public LinkedHashSet(int initialCapacity) {
        super(initialCapacity, .75f, true);
    }

    public LinkedHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor, true);
    }

    public LinkedHashSet(Collection<? extends E> c) {
        super(Math.max(2*c.size(), 11), .75f, true);
        addAll(c);
    }
    
    // 其他方法...
}
```

通过其构造方法开发者不难看出 `LinkedHashSet` 的构造方法都是通过 `super()` 继承了 `HashSet` 的构造方法，而 `HashSet` 的构造方法开发者再去观看 `HashSet` 源码又可以发现 `super()` 实际继承的构造函数是，如下这段：

```java
HashSet(int initialCapacity, float loadFactor, boolean dummy) {
    map = new LinkedHashMap<>(initialCapacity, loadFactor);
}
```

这也就表明了 `LinkedHashSet` 的增删改查操作实际是委托给 `LinkedHashMap` 完成的。

```java
public class Test08 {
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
```

## 注意事项

1. 哈希冲突，在这里可能开发者还不知道什么是哈希冲突，不用急，先记一下，后面会讲述哈希表，那时便会知道。
2. 线程非安全。