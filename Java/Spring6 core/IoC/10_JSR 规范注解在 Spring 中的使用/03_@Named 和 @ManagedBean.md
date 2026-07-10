# @Named 和 @ManagedBean

本节主要讲述组件的装配扫描。

使用 JSR-330 标准注解想要标记组件，开发者可以使用 `@Named` 和 `@ManagedBean`。

## @Named

在上节讲述过，其可以像 `Qualifier` 那样为依赖赋予限定符，它还可以标记组件。

## @ManagedBean

用法和 `@Named` 一样，既可以为依赖赋予限定值，还可以标记组件

## 开启组件扫描

开发者可以使用 Spring 的注解以达到开启组件扫描的目的，开启组件扫描后，会识别所有的 `@Named、@ManagedBean`。

示例代码：

```java
@Named // 也可使用 @ManagedBean
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

开启组件扫描：

```java
@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig  {
    // ...
}
```

