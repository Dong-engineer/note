# @Value

在 XML 的学习中，开发者学习过引入外部配置文件（properties 文件），本节开始学习注解的方式引入外部配置文件的数据信息。

## @Value 是什么

`@Value` 常用于引入外部配置文件信息，它还有一些其他操作，如使用 `SpEl` 表达式获取一些系统配置等其他操作。

`@Value` 的核心是通过 **静态值、外部属性、`SpEL` 表达式** 为字段或参数注入值。

那么 `@Value` 怎么用？

## @Value 的使用

### 正常的使用

正常的使用也就是引入外部配置文件 Bean 的数据信息。

#### 注解的方式注册 `PropertySourcesPlaceholderConfigurer`

在 XML 的引入配置文件的学习中，开发者知道在引入外部 Bean 的前提条件需要实例一个 `PropertySourcesPlaceholderConfigurer` Bean 并在其属性字段（`classpath:`）中指定配置文件的路径。

 使用注解的的方式同样需要 `PropertySourcesPlaceholderConfigurer` 的协助，但并不用在 XML 文件中实例化 Bean，开发者需要定义一个 `Config` 类，如下：

```java
// 这里面可能会有很多的代码开发者并不知道是什么，下面注释会讲述
// 但具体注意事项等后面讲述到会详细讲述
@Configuration // 标记配置类
@PropertySource("classpath:application.properties") // 声明配置文件路径
public class AppConfig {
    
    @Bean // 将对象注册到 ApplicationContext 中，并交给 Spring 容器管理
    // !!!!!!!!!!
    // 注意，一定要保证该方法是 static
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
}
```

以上代码等同于 XML 文件如下：

```xml
<bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
    <property name="locations" value="classpath: application.properties"/>
</bean>
```

#### `@Value`

如果开发者使用的是 `PropertySourcesPlaceholderConfigurer` 来进行获取配置文件信息，那么同 XML 文件一样使用 `${}` 来进行占位，然后获取信息。

配置文件：

```properties
catalog.name=MovieCatalog
```

代码如下：

```java
public class MovieRecommender {

    private final String catalog;

    public MovieRecommender(@Value("${catalog.name}") String catalog) {
        this.catalog = catalog;
    }
}
```

以上代码，开发者在实际开发中很可能需要动态注入，如 `${catalog.name} + ':Dong'` ，但是 `${}` 并没有这种用法。

Spring 在 EL 表达式的基础上改出了 `SpEL` 表达式，能更好的适配 Spring 生态。

#### `@Value` 注入的属性默认值

如果开发者在最初时并未定义配置文件信息，**也就是说没有配置文件的信息下**，开发者可以通过下面的方式定义注入的属性默认值，如下：

```java
public MovieRecommender(@Value("${catalog.name:defaultCatalog}") String catalog) {
    this.catalog = catalog;
}
```

以上代码，如果 Spring 并未检测到 `catalog.name` 在配置文件中的值，那么 Spring 将会默认将 `defaultCatalog` 进行注入。

### `SpEL` 表达式的使用（动态注入）

开发者在实际开发中很有可能需要动态注入，也就是说开发者在注入时需要进行一定的计算后才进行注入，Spring 提供了 `SpEL` 表达式（`#{}`）。

代码如下：

```java
@Component
public class MovieRecommender {
    
    private final String catalog;

    // 动态拼接系统属性user.catalog与字符串"Catalog"
    public MovieRecommender(@Value("#{systemProperties['user.catalog'] + 'Catalog'}") String catalog) {
        this.catalog = catalog;
    }
    
}
```

这里进行注入时会优先进行计算后，才会进行注入。

#### 注入复杂类型

`SpEL` 表达式本质其实 Spring 表达式，而 Spring 表达式的本质是 Java 代码，这也就意味着 `#{}` 的括号中是允许使用 Java 表达式的，即在大括号中书写方法、类型参数、运算等等。

假设现在需要注入一个 Map 集合，开发者可以书写如下代码：

```java
@Component
public class MovieRecommender {

    private final Map<String, Integer> countOfMoviesPerCatalog;

    public MovieRecommender(
            @Value("#{{'Thriller': 100, 'Comedy': 300}}") Map<String, Integer> countOfMoviesPerCatalog) {
        this.countOfMoviesPerCatalog = countOfMoviesPerCatalog;
    }
}
```



## @Value 的注意事项

无论是正常的使用还是 `SpEL` 表示进行注入，`@Value` 都需要注意一下几点。

1. 声明 `PropertySourcesPlaceholderConfigurer` 。开发者在实际开发中如果不声明 `PropertySourcesPlaceholderConfigurer` ，然后去使用 `@Value` ，程序不会抛出异常，但会发生致命错误。
   - 如果开发者不声明 `PropertySourcesPlaceholderConfigurer` ，`@Value` 依然会注入成功，前提是 Spring 可以在容器中找到 `${}` 所指定的内容，不然 Spring 会将 `${...}` 当作值直接注入。
2. `@Vaule` 不单单只是注入外部配置文件，只是常用于注入外部配置文件。除此之外 `@Value` 可以注入系统属性、 注入环境变量、注入资源文件、注入其他 Bean 的属性或方法返回值（这个可以使用 `@Autowired` 或者 `@Resource`）。

