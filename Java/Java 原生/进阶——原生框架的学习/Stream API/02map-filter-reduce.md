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

 

```
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

