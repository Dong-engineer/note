# @Configuration

`@Configuration` 类级别注解，在前面的学习开发者对其多少有一点了解，如：`@Configuration` 用在哪里？

在前面的示例代码中 `@Configuration` 经常用于 `AppConfig` 类。

`@Configuration` 表示 Bean 定义的来源，其需要 `@Bean` 的搭配使用。

## @Configuration 源码

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

	@AliasFor(annotation = Component.class)
	String value() default "";

	@Deprecated
	boolean proxyBeanMethods() default true;

}
```

以上源码，开发者可以发现 `@Configuration` 具有 `@Component`，开发者可以将 `@Configuration` 理解为特殊的 `@Component` 。具体详细的细节阅读下面内容。

## @Configuration 的使用

`@Configuration` 的使用示例代码如下：

```java
@Configuration
public class AppConfig {

    @Bean
    public BeanOne beanOne() {
        return new BeanOne(beanTwo());
    }
    
    @Bean
    public BeanTwo beanTwo() {
        return new BeanTwo();
    }

}
```

下面讲述其与 `@Bean` 混合搭配使用细节问题。

### 自动注入

以上代码，开发者不难发现 `beanOne` 依赖了 `beanTow` ，这时就需要 `@Autowired` 来进行自动注入的，但方法中明显没有使用注解 `@Autowired`。

这和 `@Configuration` 的工作原理有着密切的关系，后面会详细讲述 `Configuration` 的工作原理。

### 方法注入

在 *方法注入* 一节已经详细讲述过方法注入，当然也包括基于注解的方法注入。

这里主要讲述方法注入和 `@Bean` 的配合使用，示例代码如下：

```java
public abstract class CommandManager {
    public Object process(Object commandState) {

        Command command = createCommand();
        command.setState(commandState);
        return command.execute();

    }

    protected abstract Command createCommand();
}

@Configuration
public class AppConfig{

    @Bean
    @Scope("prototype")
    public AsyncCommand asyncCommand() {
        AsyncCommand command = new AsyncCommand();
        return command;
    }

    @Bean
    public CommandManager commandManager() {

        return new CommandManager() {
            protected Command createCommand() {
                return asyncCommand();
            }
        }
    }

}
```

以上代码，开发者在 *方法注入一节* 已经学习过了，这是通过匿名类的方式，将多例 Bean 注入到单例 Bean 中，这时就会有开发者想到注解 `@Lookup`，但上面代码并没有使用 `@Lookup`。

这是因为 `@Lookup` 是通过 CGLIB 技术实现的，具有一定的局限性（具体什么局限，请看 *方法注入* 一节）。

匿名类的方式更加灵活，其适用 `@Bean` 的适用场景，如果对于业务需求并由太过复杂的要求，那么开发者可以使用 `@Lookup` 完成方法注入。

**方法注入核心内容工作是为了解决单例 Bean 依赖多例 Bean 的问题，但这并不是方法注入的本质思想，其本质思想：将对象所需的依赖项通过特定方法显式传入，而非在对象内部自行创建或通过构造函数注入，从而实现依赖的解耦和外部控制**。

要灵活应用其思想。

### @Configuration 的属性

对于 `value` 属性不再讲述，和 `@Component` 的一样用法。

主要是 `proxyBeanMethods` ，源码中其是一个布尔类型，其作用是是否开启代理模式。默认是 `true`。具体什么是代理模式，请看下面的*工作原理*。

可能还有开发者注意到了其上面的 `@Deprecated` ，这表示弃用的意思，但在 Spring 5.2 后移除了该注解，这也就表该属性可以稳定使用，之所以在之前的版本中设有 `@Deprecated`，Spring 一直在对于进行完善探索。

### 补充

这里补充一些术语：

**Full** 模式：`proxyBeanMethods = true`，启用 CGLIB 代理

**Lite** 模式：`proxyBeanMethods = false`，不启用代理

## @Configuration 工作原理

在上节提到了 `ConfigurationClassPostProcessor` ，对于 `@Configuration` 声明的 Bean 的解析工作就是其完成的。

最为核心的特性是 **CGLIB 动态代理**，Spring 会通过 CGLIB 动态代理技术为含有 `@Configuration` 类型创建代理对象（AOP），而该代理的核心内容就是确保 `@Bean` 方法之间不会重复创建对象，从而导致类爆炸（准确来说是堆内存占用过多）。`@Bean` 方法之间所依赖的对象均确保来自于 Spring 容器中。

同时，CGLIB 动态代理完成了自动注入的功能，这也是为什么 `@Component` 和 `@Bean` 使用时不能自动注入的原因。

通过 `@Configuration` 的源码，开发者应该明白其也是通过 classpath 扫描发现 `@Configuration` 类型，然后进行解析。

## 注意事项

这里就强调一点，对于 `@Configuration` 注解的类，其在容器中的体现只是一个 Bean，这也就意味着其他 Bean 可以将其作为依赖注入，或者该Bean 注入其他依赖。

总之正常 Bean 可以进行的操作，其都可以，不过要注意书写代码的逻辑是否正确。

