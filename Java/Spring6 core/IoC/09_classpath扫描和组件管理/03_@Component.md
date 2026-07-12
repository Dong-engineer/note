# @Component

## @Component 是什么

`@Component` 是一个常见的注解，主要用于标识一个类作为 "组件"，使其能够被框架自动识别、管理和使用。

被标记的类会被 Spring 纳入容器进行管理。

同时还是后面要学习的一些重要注解的父类，具体是有哪些注解，如下：

- @Service
- @Repository
- @Controller
- 等等

## @Component 怎么用

对于 `@Component` 的使用，开发者应该不陌生，但是 `@Component` 可以和很多元注解搭配使用，同时还有上面提到的子类注解，这里详细说明。

### @Component 和元注解的搭配使用

这里以 Spring 的源码为例。

如下是 `@Service` 注解的源码：

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {

    String value() default "";

}
```

以上代码对于开发者来讲是不难的，在学习 *@Qualifier* 注解时，讲述过自定义注解，对于以上代码一行一行解释。

`@Target({Element.TYPE})` 该元注解规定了 `@Service` 该注解只能用于修饰类、接口（包括注解类）或者枚举。

`@Retention(RetentionPolicy.RUNTIME)` 规定了注解的触发时机为运行时触发。

`@Documented` 表示会被 JavaDoc 工具记录。

以上均为元注解，便于初学者基础不牢，怕对于 Java 原生内容（元注解）不熟悉，这里又补充说明。

`@Component` 开发者应该都熟悉了，在自定义的注解上生命另一注解是为了保证其可以拥有一样的操作逻辑，也就是说 `@Component` 和 `@Sercice` 的处理方式是相同的（这在 *@Qualifier* 一节讲述过）。

`@Component` 和 `@Serviece` 的区别就是二者所表达的意思不相同。

在实际开发中有经验的开发者知道，整个系统是需要分层控制的，每一个层都有自己的工作，而层层之间又需要协作， 为了更好的区别层层之间的类 Spring 规定了不同的层进行扫描时应当使用不同的注解，方便开发者阅读。

所以`@Component` 和 `@Serviece` 用于不同的层，`@Controller` 亦是如此。

### @Component 和注解的配合使用

以 Spring 中的 `@RestController` 源码为例，如下：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface RestController {
    String value() default "";
}
```

对于元注解不再解释。

重点是 `@Component` 和 `@ResponseBody` ，`@RestController` 是由 `@Controller` 和 `@ResponseBody` 组成，这也意味着，其具有二者一样的功能。

### 统一扫描

为了方便开发者书写代码，如果开发者希望 Spring 扫面某一包内的所有类并交给 Spring 管理，那么开发者可以**在启动类上声明 `@ComponentScan("路径")`**。

如果单单使用 `@Component` 在类上面声明，并不会对类型检测，这只是声明了需要扫描的组件，开发者仍需要使用 `ComponentScan()` 开启扫描

### @Component 源码解析

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Component {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

} 
```

以上代码开发者可以发现，使用时 `@Component` 可以对其属性进行赋值，如下：

```java
@Component(name="userService")
public class UserService {
    // 类的实现
}
```

无论是 `value` ，还是 `name` 他们都代表了被扫描组件 Bean 的 `id/name` 的值。

如果不对其属性赋值，那么被扫描组件的默认 `id/name` 是其类型首字母小写。

## @Component 工作原理

`@Component` 的工作主要由以下类完成：

- **`ComponentScanAnnotationParser`**：解析 `@ComponentScan` 注解的属性（如扫描路径、过滤规则等）。
- **`ClassPathBeanDefinitionScanner`**：执行类路径扫描，发现并筛选符合条件的组件类。
- **`BeanDefinitionRegistry`**：负责将解析后的 `BeanDefinition` 注册到容器中。

## 注意事项

1. 使用时为 Bean 命名规范，建议类型首字母小写
2. 被扫描的组件在整个容器中默认是单例的，即组件的 `Scope=Singleton`
3. 注意不同语义注解的使用
4. 不要忘记使用 `ComponentScan` 开启扫描