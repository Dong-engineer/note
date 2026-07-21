# 创建 Stream 对象

在前面的章节中开发者有学习到，如何对 Collection 对象进行创建流，如：`stream()` 。

但是除了 Collection 外，开发者还可以对 Iterator 、数组等对象创建流对象。

## Collection 获取 Stream 对象

Collection 获取 Stream 对象，在前面已经讲述过如何获取 Stream 对象，即通过 `stream()` 获取 Stream 对象。

```java
public class Test01 {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        Set<String> set = new HashSet<>();
        set.add("1");
        set.add("2");
        set.add("3");
        set.add("4");

        // 倒序排序
        Stream<Integer> reverse = list
                .stream()
                .sorted(Comparator.reverseOrder());

        reverse.forEach(System.out::println);

        Stream<String> setStream = set.stream();

        setStream.forEach(System.out::println);
    }
}

```

以上代码只介绍了 `List、Set` ，`Deque` 也可以通过 `stream()` 进行获取流对象。

但是 `Map` 不行，`Map` 无法获取任何的流对象，但是 `Map` 有一些方法，如下：

- `keySet()` —— 获取 key 的 Set 集合
- `entrySet()` —— 获取 key、value 的 Set 集合
- `valueSet()` —— 获取 value 的 Set 集合

对于 `Map` 集合，开发者完全可以通过以上方法间接获取 Stream 对象。