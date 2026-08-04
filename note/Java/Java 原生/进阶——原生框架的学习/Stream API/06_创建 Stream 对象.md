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

## vararg 创建流

动态参数创建流。是通过 `Stream.of()` 工厂方法来进行创建的流工厂，示例如下：

```java
public class Test02 {
    public static void main(String[] args) {
        // Stream API 提供了工厂方法，开发者可以更加灵活的创建 Stream 对像
        Stream<Integer> intStream = Stream.of(1, 2, 3, 4);
        Stream<Character> charStream = Stream.of('1', '2', '3');
        Stream<String> strStream = Stream.of("China", "Beijing", "HaiDian");

        System.out.println(intStream.toList());
        System.out.println(charStream.toList());
        System.out.println(strStream.toList());
    }
}
```

如果开发者通过 IDE 查看 `Stream.of()` 的创建方式，如下：

```java
public static<T> Stream<T> of(T... values) {
    return Arrays.stream(values);
}
```

开发者可以看出 `Stream.of()` 实际调用的是 `Arrays.stream()` 。

## 数组创建流

数组创建流。是通过 `Arrays.stream()` 来创建的，示例如下：

```java
public class Test03 {

    public static void main(String[] args) {
        String[] arr = {"1", "2", "3"};
        Stream<String> intStream = Arrays.stream(arr);

        System.out.println(intStream.toList());
    }
}
```

## 从 Supplier 创建流

`Supplier` 是 Java 函数式编程框架中四大主要接口之一，开发者如果首次了解的话，可以将其认定为 Lambda 表达式（具体详情，开发者可自行搜查资料）。

当开发者想要创建一个无限且无序的流，开发者可以通过 `generate()` 来创建流对象，示例如下：

```java
public class Test04 {
    public static void main(String[] args) {
        // generate() 创建的流对象，不会自行关闭，且该流是无序的
        // 如果开发者不使用 limit() ，限制流的大小，那么流会一直增加内存的占用量，直到内存溢出或者 JVM 的 heap 被占满
        Stream<Integer> intStream = Stream.generate(() -> 1);

        System.out.println(intStream.toList()); // OutOfMemoryError
    }
}
```

开发者还可以通过 `iterate()` 创建一个有序、无限的流，示例如下：

```java
public class Test05 {
    public static void main(String[] args) {
        // iterate() 和 generate() 二者实际均为无限的流，只不过 iterate() 接受的参数可以是一个 Predicate，这就使得 iterate() 方法可以按照开发者所定义参数、方法体去创建流对象
        Stream<String> stringStream = Stream.iterate("+", p -> p + "+");
        stringStream.forEach(System.out::println);
    }
}
```

## 从字符创建流

开发者可以通过 `String` 的 `chars()` 来获取一个 `InputStream`，示例如下：

```java
public class Test06 {
    public static void main(String[] args) {
        String str = "test stream";

        List<String> stringList = str
                .chars()
                .mapToObj(s -> (char) s)
                .map(Object::toString)
                .toList();

        stringList.forEach(System.out::println);
    }
}
```

## 从指定的数字范围创建流

当开发者想要指定流的范围大小时，可以使用 `range()` 创建流对象，示例如下：

```java
public class Test07 {
    public static void main(String[] args) {
        String[] strs = {"a", "b", "c", "d"};
		
        // range() 是 IntStream、LongStream 等基本数值流的工厂方法
        List<String> listLetter = IntStream
                .range(0, 10)
                .mapToObj(index -> strs[index % strs.length])
                .toList();

        listLetter.forEach(System.out::println);
    }
}
```

除了 `range()` 外，`IntStream、LongStream、DoubleStream`  分别对应的工厂方法 `ints()、long()、double()`。开发者可自行练习。

##  从随机数中创建流

从随机数中创建流对象的方式和从指定的数字范围创建流一样，但是从随机数中创建需要使用到 `Random` ，示例如下：

```java
public class Test08 {
    public static void main(String[] args) {
        Random random = new Random();

        List<Integer> randomList = random
                .ints(10, 1, 5)
                .boxed()
                .toList();

        randomList.forEach(System.out::println);
    }
}
```

## Stream 的建造者模式

`Stream` 提供了建造者模式（对于不熟悉建造者模式的开发者，可自行搜查资料了解），开发者可以通过建造者模式来创建流对象，示例如下：

```java
public class Test09 {
    public static void main(String[] args) {
        // Stream 提供了建造者模式
        // 创建建造者
        Stream.Builder<String> strStream = Stream.<String>builder();

        // 预添加数据
        strStream.add("one");
        strStream.add("two");
        strStream.add("three");
        strStream.add("four");

        // build() 创建流对象，collect() 调用终端操作收集流并转化为数据结构对象
        System.out.println(strStream.build().collect(Collectors.toList()));
    }
}
```



## 补充

在 Java 的官方文档中，还有三种创建流的方式：

1. 在 IO 操作中创建流对象
2. 在 HTPP 中创建流对象
3. 从正则表达式中创建流对象

这三种创建流对象的方式，不在本文档中介绍，这三种创建流的方式，需要开发者熟练使用 `Stream API` 后可自行去学习。
