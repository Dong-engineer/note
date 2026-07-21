# Stream 简介

`Stream API` 和 `Collection API` 一般是配套使用的，`Stream API` 可以让开发者更高效的去处理 `Collection` 中的数据。如下：

```java
List<String> strings = List.of("one", "tow", "three");

// 动态类型
var map = strings.stream()
                 .collect(Collectors.groupingBy(String::length, Collectors.counting()));

map.forEach((key, value) -> IO.println(key + " :: " + value));
```

```cmd
3 :: 2
4 :: 1
5 :: 1
```

以上代码，在开发者熟练使用 `Stream API` 后会变得非常具有变现力，开发者不需要书写很长且很难懂的 for 循环。

