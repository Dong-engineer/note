# @Scope

`@Scope` 基于注解的作用域范围。

`@Scope` 也是一个 `BeanPostProcessor` 。

在前面学习了基于注解 Bean 的注入、组件扫面，那么这些 Bean 被注册到 Spring 容器中，其作用范围呢？

## 基于注解的默认的作用范围

Spring 规定了基于注解的默认作用范围是 `singleton` 。这也就表示基于注解的所有的 Bean 都是单例 Bean。

## @Scop 的使用

在开发时功能需求很有可能需要多例 Bean 或者是其他作用范围的 Bean，那么在纯注解的开发中，开发者可以使用 `@Scope` 来声明 Bean 的作用范围。

### `@Scope` 源码

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

	@AliasFor("scopeName")
	String value() default "";

	@AliasFor("value")
	String scopeName() default "";

	ScopedProxyMode proxyMode() default ScopedProxyMode.DEFAULT;

}
```

以上源码可以发现其含有三个属性，下面就详细讲解。

#### value、scopeName

二者都是一样的效果，但互为别名，默认是空字符串，表示 Bean 的 `scope` 为 `singleton`。

其他值：

- `ConfigurableBeanFactory.SCOPE_PROTOTYPE`：原型（每次请求创建新实例，即单例）
- `WebApplicationContext.SCOPE_REQUEST`：请求作用域
- `WebApplicationContext.SCOPE_SESSION`：会话作用域

#### proxyMode

`proxyMode` 用于指定是否创建作用域代理以及代理的类型，代理对象即 AOP ，前面提到一点很少，后面还会详细讲述 AOP的，这里就不再多述。

该属性是一个枚举类型，即 `ScopedProxyMode` 是一个枚举类，其值包括：

- `DEFAULT`：默认值，通常表示不创建代理
- `NO`：不创建代理
- `INTERFACES`：创建基于接口的 JDK 动态代理
- `TARGET_CLASS`：创建基于类的 CGLIB 代理

## 注意事项

1. 一定要注意不要让多例 Bean 依赖单例 Bean，这会造成程序和功能要求不一致。

