# Stream

`Stream` 是 Java 8 引入的流式编程核心接口，位于 `java.util.stream` 包，专门用于对集合、数组等数据源执行**链式、惰性、并行 / 串行**的数据聚合操作。

## 源码

### 中间操作

```java
// 过滤元素，保留满足条件的数据
Stream<T> filter(Predicate<? super T> predicate);

// 映射：T -> R，一对一转换
<R> Stream<R> map(Function<? super T, ? extends R> mapper);
// 映射：拆分流（一对多，如List拆成单个元素）
<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);

// 去重，基于equals()
Stream<T> distinct();
// 自然排序
Stream<T> sorted();
// 自定义比较器排序
Stream<T> sorted(Comparator<? super T> comparator);

// 截取前N个元素
Stream<T> limit(long maxSize);
// 跳过前N个元素
Stream<T> skip(long n);

// peek：调试打印，不改变流元素（仅消费元素）
Stream<T> peek(Consumer<? super T> action);
```

### 终端操作

#### 遍历 / 无返回

```java
void forEach(Consumer<? super T> action);
// 有序遍历（并行流下也保证顺序）
void forEachOrdered(Consumer<? super T> action);
```

#### 统计数值

```java
long count();
Optional<T> min(Comparator<? super T> comparator);
Optional<T> max(Comparator<? super T> comparator);
```

#### 匹配判断（短路操作，找到即停止遍历）

```java
// 任意一个匹配
boolean anyMatch(Predicate<? super T> predicate);
// 全部匹配
boolean allMatch(Predicate<? super T> predicate);
// 全部不匹配
boolean noneMatch(Predicate<? super T> predicate);
```

#### 获取单个元素（短路）

```java
Optional<T> findFirst(); // 获取第一个元素（有序流）
Optional<T> findAny();   // 获取任意元素（并行流更快）
```

#### 归约聚合 reduce

```java
// 无初始值，返回Optional
Optional<T> reduce(BinaryOperator<T> accumulator);
// 带初始值，直接返回T
T reduce(T identity, BinaryOperator<T> accumulator);
// 并行归约（初始值+累加器+合并器）
<U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);
```

#### 收集结果（最常用）

```java
<R, A> R collect(Collector<? super T, A, R> collector);
// 自定义容器收集
<R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner);
```

### 数值流转换（转 IntStream/LongStream/DoubleStream）

```java
IntStream mapToInt(ToIntFunction<? super T> mapper);
LongStream mapToLong(ToLongFunction<? super T> mapper);
DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper);
// flatMap 数值拆分
IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper);
```

## 使用示例

### 前置数据源

```java
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {
    public static void main(String[] args) {
        List<Integer> numList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        List<String> strList = Arrays.asList("java","stream","lambda","spring");
        List<List<Integer>> twoDimList = Arrays.asList(
                Arrays.asList(1,2),
                Arrays.asList(3,4),
                Arrays.asList(5,6)
        );

        demoFilterMap(numList);
        demoFlatMap(twoDimList);
        demoSortDistinct();
        demoMatchFind(numList);
        demoCollect(strList);
        demoParallel(numList);
    }
```

### filter + map 基础转换过滤

```java
// 筛选偶数、乘2、收集为新集合
static void demoFilterMap(List<Integer> list) {
    List<Integer> res = list.stream()
            .filter(n -> n % 2 == 0)       // 中间：过滤偶数
            .map(n -> n * 2)               // 中间：元素*2
            .collect(Collectors.toList());  // 终端：收集List
    System.out.println(res); // [4, 8, 12, 16, 20]
}
```

### flatMap 拆分多维集合

```java
// 二维列表扁平化一维
static void demoFlatMap(List<List<Integer>> list) {
    List<Integer> flat = list.stream()
            .flatMap(subList -> subList.stream())
            .collect(Collectors.toList());
    System.out.println(flat); // [1, 2, 3, 4, 5, 6]
}
```

### 去重、排序、截取分页

```java
static void demoSortDistinct() {
    List<Integer> data = Arrays.asList(5,2,2,9,1,5,3);
    List<Integer> page = data.stream()
            .distinct()          // 去重
            .sorted()            // 升序
            .skip(1)             // 跳过第1个
            .limit(3)            // 取3个
            .collect(Collectors.toList());
    System.out.println(page); // [2, 3, 5]
}
```

### 匹配、查找短路操作

```java
static void demoMatchFind(List<Integer> list) {
    boolean hasBig = list.stream().anyMatch(n -> n > 8);
    boolean allSmall = list.stream().allMatch(n -> n < 100);
    Optional<Integer> first = list.stream().filter(n > 5).findFirst();

    System.out.println(hasBig);       // true
    System.out.println(allSmall);     // true
    first.ifPresent(System.out::println); // 6
}
```

### 分组、拼接字符串收集

```java
static void demoCollect(List<String> list) {
    // 拼接逗号分隔字符串
    String join = list.stream().collect(Collectors.joining(" | "));
    System.out.println(join); // java | stream | lambda | spring
}
```

### 并行流 parallelStream

```java
static void demoParallel(List<Integer> list) {
    long sum = list.parallelStream()
            .filter(n -> n % 2 == 0)
            .mapToInt(Integer::intValue)
            .sum();
    System.out.println("偶数和：" + sum); // 30
}
```

## 使用注意事项

### 1. 流只能消费一次（最重要）

Stream 执行终端操作后会关闭，重复调用抛出 `IllegalStateException: stream has already been operated upon or closed`

```java
Stream<Integer> stream = numList.stream();
stream.count();
stream.filter(n->n>1).collect(Collectors.toList()); // 报错
```

解决：每次操作重新创建流 `list.stream()`

### 2. 惰性求值：中间操作不会立即执行

```java
numList.stream()
    .filter(n -> {
        System.out.println("过滤：" + n); // 无输出，仅记录逻辑
        return n % 2 == 0;
    });
// 无终端操作，代码完全不执行
```

必须搭配 `collect/count/forEach` 等终端操作才会执行。

### 3. 并行流风险

1. **非线程安全容器不能在并行流中直接 add**
   1. `List<Integer> unsafe = new ArrayList<>(); numList.parallelStream().forEach(unsafe::add); // 并发丢失数据`
   2.  解决：使用 `collect` 收集，或 `Collections.synchronizedList`
2. 并行流不保证顺序，需要有序用 `forEachOrdered()`；
3. 数据量小时并行流开销大于收益，简单计算优先串行。

### 4. peek 仅调试使用，不要做业务修改

`peek` 设计初衷是打印日志调试，依赖 peek 修改外部变量会产生不可预期结果；业务逻辑放 map/filter。

### 5. 数据源不要在流操作中修改

遍历集合流时，新增 / 删除原集合元素会触发 `ConcurrentModificationException`

```java
List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3));
list.stream().forEach(n -> list.add(99)); // 并发修改异常
```

### 6. Optional 空值处理

`findFirst/min/max/reduce` 返回 `Optional`，禁止直接 `get()`，优先使用 `ifPresent/orElse/orElseThrow` 避免空指针。

### 7. limit/skip 配合有序流才有分页意义

并行无序流下 `skip/limit` 无法稳定实现分页，需要先调用 `sorted()` 保证有序。

### 8. 基础类型优先使用数值流（IntStream/LongStream）

避免频繁装箱拆箱，`mapToInt` 性能远优于 `map(Integer::intValue)`。

### 9. close 资源释放

流绑定 IO 文件、网络资源时（如 `Files.lines()`），必须手动 close 或使用 try-with-resources 自动关闭，防止文件句柄泄漏：

```java
try (Stream<String> lines = Files.lines(Paths.get("test.txt"))) {
    lines.forEach(System.out::println);
} catch (IOException e) {
    e.printStackTrace();
}
```
