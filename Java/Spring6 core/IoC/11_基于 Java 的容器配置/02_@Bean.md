# @Bean

`@Bean` 是 Java 在 Spring 中的核心工件之一，来表示一个方法实例化、配置和初始化了一个新的对象，由Spring IoC容器管理。

在前面的学习，开发者见过了在方法上声明的 `@Bean` ，这表明了该方法会返回一个 Bean 对象并将给 Spring 管理。

## @Bean 的源码

```java
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    /**
     * 等同于 name() 属性，用于指定 Bean 的名称或别名
     */
    @AliasFor("name")
    String[] value() default {};

    /**
     * 指定 Bean 的名称或别名，默认为空数组（此时使用方法名作为 Bean 名称）
     */
    @AliasFor("value")
    String[] name() default {};

    /**
     * 指定 Bean 的初始化方法名称（对应 XML 配置中的 init-method）
     */
    String initMethod() default "";

    /**
     * 指定 Bean 的销毁方法名称（对应 XML 配置中的 destroy-method）
     */
    String destroyMethod() default AbstractBeanDefinition.INFER_METHOD;
}
```

以上源码，开发者不难发现，`@Bean` 有四个属性 `value、name、initMethod、destroyMethod`。

对于 `value` 和 `name` 不再分开讲述，二者用法一样。

## @Bean 的使用

`@Bean` 注解开发者者可以等同地认为是 XML 文件的 `<bean/>` 。

示例代码如下：

```java
@Configuration
public class AppConfig {
    // 将返回的DataSource对象注册为Spring容器中的Bean
    // 默认 Bean 的 name 为方法名
    @Bean
    public DataSource dataSource() {
        return new DruidDataSource();
    }
    
    // 定义一个服务Bean，依赖上面的dataSource Bean
    @Bean
    public UserService userService(DataSource dataSource) {
        UserService service = new UserService();
        service.setDataSource(dataSource);
        return service;
    }
}
```

### @Bean 命名

`@Bean` 也可以为 Bean 进行命名，使用其 `name` 属性即可。

```java
@Configuration
public class AppConfig {
    
    @Bean(name="dataSource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }
    
    @Bean(name="userService")
    public UserService userService(DataSource dataSource) {
        UserService service = new UserService();
        service.setDataSource(dataSource);
        return service;
    }

}
```

### @Bean 的初始化回调和销毁回调

当开发者需要配置 Bean 的初始化回调和销毁回调时，可以如下操作：

```java
public class BeanOne {

    public void init() {
    }
}

public class BeanTwo {

    public void cleanup() {
    }
}

@Configuration
public class AppConfig {

    @Bean(initMethod = "init")
    public BeanOne beanOne() {
        return new BeanOne();
    }

    @Bean(destroyMethod = "cleanup")
    public BeanTwo beanTwo() {
        return new BeanTwo();
    }
}
```

## @Bean 的工作原理

`@Bean` 的实际工作是由一个 `BeanFactoryPostProcessor` 来完成的，即 `ConfigurationClassPostProcessor` ，其负责解析配置类（即带有 `@Configuration` 的类）中的 `@Bean` 方法，将其转化为 `BeanDefinitoin` 注册到容器中。

## @Bean 与 @Component的区别

开发者不难发现 `@Bean` 和 `@Component` 二者都是将类注册到 Spring 容器中，但二者却有天大的差别。

1. 工作原理不同，`@Bean` 是通过 `BeanFactoryPostProcessor` 完成工作的，而 `@Component` 是通过classpath 扫描（多个 Spring 原生接口实现类完成的）
2. `@Bean` 是方法级别的注解，`@Component` 是类级别注解，也就是说 `@Bean` 用于方法声明， `@Componet` 用于类声明
3. 除以上之外还有一些注解混合搭配使用的需要注意，主要是混合搭配使用后，Spring 对依赖、代理等处理的方式会由差别

