# @PropertySource

`@PropertySource` 是 Spring 提供的注解用于将指定的 `PropertySource` 集成到 Spring 的 `Environment` 中

## @PropertySource 源码

```java
@Target(ElementType.TYPE)
@Retention(RententionPolicy.RUNTIME)
@Document
@Repeatable(PropertySources.class)
public @interface PropertySource {
    
    // 配置源的文件所在位置
    String[] value;
    
    // 配置源的文件编码方式
    String encoding() default "";
    
    // 属性源名称
    String name() default "";
    
    // 用于解析配置文件的工厂类
    Class<? extends PropertySourceFactory> factory() default PropertySourceFactory.class;
}
```

通过以上源码可知，`@PropertySource` 注解可以在一个类上多次使用。

## @PropertySource 的使用

在 *@Value* 一节中，已经学习过了 `@PropertySource` 的使用，但并不详细，这节会非常详细的讲述其如何使用。

假设开发者现有一个 app.properites 文件，如下：

```properties
jdbc.username=root
```

```java
@Configuration
@PropertySource("classpath:xxx/app.properites")
public class AppConfig {
	
    @Bean
    public UserService userService(@Value("${jdbc.username}")String name) {
        return new UserService(name);
    }
    
}
```

这种用法在 *@Value* 一节中已经讲述过，开发者并不陌生，接下来讲述自定义的 `@PropertySource`。

### 自定义 @PropertySource

在学习前，先介绍一下 `PropertySourceFactory` 。

#### PropertySourceFactory

`PropertySourceFactory` 接口是 Spring 用于解析配置文件的工厂类，其默认实现类 `DefaultPropertySourceFactory` 就实现了对 properites 文件和 XML 文件的解析。

源码如下：

```java
@FunctionalInterface
public interface PropertySourceFactory {
    
    // 根据指定得到资源文件创建一个 PropertySource 实例
    // name -> 要创建的 PropertySource 实例的名称
    // resource -> 文件资源
    PropertySource<?> createPropertySource(String name, EncodeResource resource) throws IOException;
        
}
```

在上节的学习中，开发者学习了自定义的 `JSONFileProperty` ，这节讲述如何使用注解将 `JSONFileProperty` 添加到 Spring 的 `Environment` 中。

首先开发者需要一个自定义的可以解析 `JSONFile` 的 `Factory` Bean，代码如下：

```java
public class JSONFileFactory implements PropertySourceFactory {
    
    // 具体内容不在书写，不要忘记实现接口方法
    // 具体如何解析 JSON 文件内容，开发者可以阅读 Jackson API
}
```

然后在 `AppConfig` 上声明注解即可，如下：

```java
@Configuration
@PropertySource(value = "classpath: app-config.json", factory = JSONFileFactory.class)
public class AppConfig {
}
```

#### 补充

在书写本节过程中，作者通过 AI 阅读了很多代码，最初 AI 所给基于注解将 JSON 文件添加到 Spring 的 `Environment` 中的代码，是通过自定义注解 `@JsonPropertySource` ，具体代码如下：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Docemented
@Repeatable(JsonPropertySources.class)
public @interface JsonPropertySource {
    
        // 配置源的文件所在位置
    String[] value;
    
    // 配置源的文件编码方式
    String encoding() default "UTF-8";
    
    // 属性源名称
    String name() default "";
    
    // 用于解析配置文件的工厂类
    Class<? extends PropertySourceFactory> factory() default JSONFileFactory.class;
    
}
```

```java
// 该注解表示可以一次性写多个 JSON 配置源信息
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Docemented
public @interface JsonPropertySources{
    
    String[] values();
    
}
```

```java
@Configuration
@JsonPropertySource("classpath: app-config.json")
public class AppConfig {
} 
```

其实这种写法和 `@PropertySource` 没有太大的区别，默认解析类和语义表达不一样，但开发者再次仔细阅读代码可以发现，对于 `@JsonPropertySource` 的写法并不像自定义 `@Qualifer` 和 自定义`@Component` 那样，直接将核心的注解当作元注解声明在自定义注解头上。

这是因为：

1. 如果将 `@PropertySource` 注解当作元注解去自定义 `@JsonPropertySource`，那么 `@JsonPropertySource` 默认的解析工厂类并不是 `JSONFileFactory`
2. 语义不明确，自定义 `@JsonPropertySource` 的用意就是仅仅解析 JSON 文件，如果将 `@PropertySource` 注解当作元注解，`@PropertySource` 的解析方式并不能适配 JSON 文件

简单来说就一点就是，Spring `@PropertySource` 注解作用的目的就是为了 `properites、xml` 文件而定义，其具体解析逻辑是不适用 JSON 文件。故而要自定义 `@JsonProperySoucre`。

**那第一种注解方式能不能用呢？**

**能用的，Spring 建议开发者通过 `@PropertySource` 的 `factory` 属性去扩展配置源解析工厂类类型。但从语义清晰性、代码可读性、扩展性（如果开发者通过元注解继承了 `@PropertySource` 是无法随意修改其属性的，这也就导致第一种方式的扩展性极低）来看第二种方式为更优选择**。

总而言之，是具体开发环境情况而定，要懂得灵活变通。

学习完本节，对于 `${}` 的取值，开发者也有一定了解了，Spring 整个容器中默认包含一些核心的 Bean 实例对象的，如：`Environment` 。所以 `${}` 的取值也就是来自于 `Environment` 中的属性源。 