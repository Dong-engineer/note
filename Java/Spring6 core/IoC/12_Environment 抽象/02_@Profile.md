# @Profile

`@Profile` 表明当一个或多个指定的配置文件处于活动状态时，该文件中的组件或 Bean 就有资格注册到容器中。

## 为什么使用 @Profile

在实际开发中，开发者很有可能需要不断变换开发环境，不同的开发环境也就意味着会有不同的数据源。一般情况下，开发者的应用程序上下文只会使用一个数据源（当然也有可能是多个），这也就需要开发者控制容器中 Bean 定义的数据源。

如下：

```java
// 测试环境
@Configuration
public class TestDataConfig{
    
    @Bean
    public UserService userService() {
    	return new UserService();
    }
    
}
```

```java
// 生产环境
@Configuration
public class ProductionConfig{
    
    @Bean
    public UserService userService() {
        return new UserService();
    }
    
}
```

而 `@Profile` 就帮助开发者定义 Bean 定义的数据源。

## @Profile 源码

```java
@Target(ElementType.TYPE, Element.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ProfileCondition.class) // 核心：通过 ProfileCondition 实现条件判断
public @interface Profile {
    
    // 指定生效的 Profile 的名称，可以使用逻辑运算符
    String[] value();
}
```

### 工作原理

由以上代码可知，`@Profile` 的源码实际上是 `@Conditional(ProfileConition.class)` 的具体体现，实际的工作原理是通过 `ProfileCondition` 来完成的。

关于 `ProfileCondition` 的源码，这里不再讲述，`ProfileCondition` 是 `@Profile` 注解的条件判断器，实现了 Spring 的 `Condition` 接口，负责校验当前激活的 profiles 是否满足 `@Profile` 注解的条件。

## @Profile 的使用

### 基于注解的使用

以上代码为例：

```java
// 测试环境
@Configuration
@Profile("Test")
public class TestDataConfig{
    
    @Bean
    public UserService userService() {
    	return new UserService();
    }
    
}
```

```java
// 生产环境
@Configuration
@Profile("Production")
public class ProductionConfig{
    
    @Bean
    public UserService userService() {
        return new UserService();
    }

}
```

 以上代码使用了 `@Profile` 注解标识了两个 `AppConfig` 的 Bean 定义配置来自不同的环境（准确来讲是来自不同的配置文件）。

当让开发者的 `AppConfig` 有可能需要多个不同的配置文件，那么开发者可以使用逻辑运算符来表示：

```java
// 生产环境
@Configuration
@Profile("Production & Test") // 这表示 Production 和 Test 两个文件必须同时激活 DifferentEnvironmentConfig 中的 Bean 才会被注册到容器中
public class DifferentEnvironmentConfig{
    
    @Bean
    public UserService userService() {
        return new UserService();
    }

}
```

```java
// 生产环境
@Configuration
@Profile("Production | Test") // 这表示 Production 或 Test 两个文件，只要激活一个文件， DifferentEnvironmentConfig 中的 Bean 才会被注册到容器中

@Profile("{Production, Test}") // 也可以这样写
public class DifferentEnvironmentConfig{
    
    @Bean
    public UserService userService() {
        return new UserService();
    }

}
```

```java
// 生产环境
@Configuration
@Profile("!Production") // 这表示 Production 文件未激活时， DifferentEnvironmentConfig 中的 Bean 才会被注册到容器中
public class DifferentEnvironmentConfig{
    
    @Bean
    public UserService userService() {
        return new UserService();
    }

}
```

`@Profile` 不仅可以使用在类上，还可以使用在含有 `@Bean` 的方法上，如下：

```java
// 生产环境
@Configuration
public class DifferentEnvironmentConfig{
    
    @Bean
    @Profile("Production")
    public UserService userService() {
        return new UserService();
    }

}
```

### 基于 XML 的使用

基于 XML 的使用，主要依靠 `profile` 属性，该属性应用于 `<beans/>` 标签中。

```xml
<beans profile="Production"
       ...>
    
    <bean id="userService" class="xxx.xxx.UserService"></bean>
    
</beans>
```

在 XML 中并不支持 `profile` 的逻辑运算符，但开发者可以使用 `!` 运算符，除此之外开发者同样可以使用嵌套的方式达到 `and(即&)` 运算符的效果，如下：

```xml
<beans profile="Production"
       ...>
    <beans>
    	<bean id="userService" class="xxx.xxx.UserService"></bean>
    </beans>
    
</beans>
```

## 激活 profiles

以上开发者明白了 `@Profile` 注解如何工作、使用，在以上内容中说到了只有当 `@profile` 所声明的文件处于激活状态时才会将 Bean 注册到容器中，那么 profiles 如何激活呢？

### 默认的 @Profile

```java
// 生产环境
@Configuration
@Profile // 即 @Profile("default")
public class DifferentEnvironmentConfig{
    
    @Bean
    public UserService userService() {
        return new UserService();
    }

}
```

默认情况下 Spring 会自动启动 default profiles 文件。

开发者可以在容器时，自定义默认激活的 profiles 文件，如下：

```java
public static void main(String[] args) {
    SpringApplication app = new SpringApplication(DifferentEnvironmentConfig.class);
    app.getEnvironment().setDefaultProfiles("Production");
    app.run(args);   
}
```

### 激活 profiles

Spring 规定了开发者必须手动指定需要激活的 profiles ，如果开发者在 profiles 未激活的状态下开启容器，那么会抛出一个 `NoSunchBeanDefinitionException` 抛出。

#### API 激活

激活 profiles 最直接的方式就是通过 API 的调用进行激活，如下：

```java
ApplicationContext applicationContext = new AnnotationConfigApplicationContext();
ctx.getEnvironment().setActiveProfiles("Production");
ctx.register(UserService.class);
ctx.refresh();
```

#### 通过属性激活

这里的通过属性激活主要指的的是 `spring.profiles.active` 属性，如下就是一个通过配置文件设置激活 profiles 的名称：

```properties
spring.profiles.actives=Prodection
```

## 自定义 @Profile

自定义的 @Profile 和自定义的 `@Qualifer` 一样，一般用于标识特殊含义，其作用效果是不变的，如下：

```java
@Target(Element.TYPE, Element.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Profile("Production")
public @interface Production {
}
```

