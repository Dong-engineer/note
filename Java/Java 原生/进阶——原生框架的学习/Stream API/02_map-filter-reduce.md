# map-filter-reduce

`map-filter-reduce` ，是掌握 `Stream API` 的三个诀窍。

在 Java 的官方教学中，其使用 `record Sale` 的例子简述了流处理数据的大致流程。这里省去这一部分，直接进入 `map-filter-reduce` 。

## map

上一节内容中，开发者知道 `strings` 引用使用了 `stream()` 获取 `Stream` 对象后，紧接使用 `collect` 获取集合映射。

看如下代码：

```java
List<String> strings = List.of("one", "two", "three", "four");

Function<String, Integer> toLength = String::length;

Stream<Integer> ints = strings.stream().map(toLength);

IO.println("Done processing");
```

假设开发者的目的是获取 `strings` 的中各个对象的长度。
开发者可以看到以上代码优先创建了方法句柄 `Function<String, Integer> toLength = String::length` 很显然这一个 Lambda 表达式，`strings` 通过 `stream()` 获取到 `stream` 流对象，虽然 `strings.stream()` 获取的流对象可以进行统计各个对象的长度，但是开发者可以更简便的达成目的，通过流的 ` map()` 做映射，将 `strings` 流具有特性通过 `Function` 对象映射到新的流对象（该对象具有 `strings` 原数据的部分特性/性质）。

## filter

过滤流，当开发者需要对集合进行筛选操作时，过滤流是一个不二的选择。如下代码。

```java
List<String> strings = List.of("one", "two", "three", "four");

long count = strings.stream()
                    .map(String::length)
                    .filter(length -> length == 3)
                    .count();

IO.println("count = " + count);
```

以上代码，为了筛选出 `strings` 集合中长度为 3 的元素使用了 `filter(length -> length == 3)` 。然后使用 `count()` 返回统计数量。

## reduce

 在 Java 的官方教程中，使用了一个统计示例：统计国家各个人口的数量。如下代码：

```java
record City(String name, int population) {}
record Country(String name, List<City> cities) {}

City newYork= new City("New York", 8_258); 
City losAngeles = new City("Los Angeles", 3_821);
Country usa = new Country("USA", List.of(newYork, losAngeles));

City london = new City("London", 8_866); 
City manchester = new City("Manchester", 568);
Country uk = new Country("United Kingdom", List.of(london, manchester));

City paris = new City("Paris", 2_103); 
City marseille = new City("Marseille", 877);
Country france = new Country("France", List.of(paris, marseille));

List<Country> countries = List.of(usa, uk, france);

int totalPopulation = 0;
for (Country country: countries) {
    for (City city: country.cities()) {
        totalPopulation += city.population();
    }
}

IO.println("Total population = " + totalPopulation);
```

以上代码，通过嵌套 for 循环语句统计了国家的人口数量。但是在实际开发中，嵌套的的 for 循环的方法体会非常臃肿，去计算各种数据。
为了扁平化嵌套 for 循环，以上面的代码为例，开发者可以将 Country 和 City 各个类的对象存放到一个集合里面，如下：

```text
正常情况：countries=[usa, uk, france]

在每一个 Country 类下，都会包含多个城市对象，进而集合演变成如下：
[[city1,city2], [city3,city4]]

但是我们完全可以先去展平集合，这样集合看起来会舒服很多，如下：
[city1, city2, city3, city4]
```

**reduce 就是这样一个过程并称其为——归约**。逻辑上开发者展平集合也需要两次 for 循环，使用流也是如此，如下代码：

```java
int totalPopulation =  
    countries.stream()           
    .flatMap(country -> country.cities().stream())           
    .mapToInt(City::population)           
    .sum();
```

两次 `stream()` ，使用 `flatMap()` 展平映射后的集合，统计出各个国家的人口。

# 补充

Stream 的学习到这里，开发者可能感觉 Stream 不就是 for 循环吗？

实际上不是的，差别在以下几点：

1. 便捷性。Stream API Java 官方封装好了很多数据统计方法，开发更加高效

2. 可读性。开发者如果熟练使用 Stream API ，那么开发者具有两种解决问题的思路，首先考虑 for 循环，如果代码过于冗长，可以考虑使用 Stream API 进行重写，扁平 for 循环提高代码可读性。

3. 性能方面。**在数据量极大情况下，大数据量开启并行流，Stream 并行远快于手写单 for**。

   - 单线程场景：简单运算下普通 for 循环性能小幅优于串行 Stream（存在少量 lambda、流迭代开销），数据量大时差距感知微弱；基础类型专用流优化后二者差距极小。

   - 并行处理：原生 for 循环天然串行，想要多线程分片计算，需要手动处理分片、线程池、结果合并，开发成本高、容易出错。

   - Stream 天然具有并行能力，底层基于 ForkJoinPool 自动分片、多线程并发执行、自动合并结果，几乎无额外编码成本。

但是并行并非一定更快，小数据量时分片、线程调度开销会抵消收益，大数据集才推荐使用并行流。

Stream 的优缺点会在后面的正式学习中逐渐提出，至此开发者对 Stream 有了一定的了解。请开发者牢记 `map-filter-reduce` 这会大大减少开发者从学习 -> 掌握的成本。
