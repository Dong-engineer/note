# @Resource

`@Resource` 这是 Java EE 生态中一个通用且规范的注解，这是 Jakarta EE 中对 CDI（也是依赖注入，但和 Spring 的思想并不同，现代的 Jakarta EE 的 CDI 同样借鉴了 Spring 的 DI 思想）所使用的注解，Spring 在早期为了兼容这种功能，适配了 `@Resource` 注解。

## @Resource 是什么

前面的学习过了自动装配（`@Autowired`），其默认是根据类型进行的装配，但是开发者在学习 XML 的自动注入的时候除了 `byType`，还学习了 `byName`。

`@Resource` 的效果和 `byName` 一样，二者都是根据名称进行装配。

但二者还是有不同的，`@Resource` 的功能更加强大。

## 怎么使用 @Resource

### 两大核心属性

`@Resource` 含有两大核心属性，`name、type`。

如同 `@Qualifier` 的限定符一样，都是起到一个能够精确匹配的作用。

下面讲述其具体使用的方式。

### 字段注入（仔细看，一个学会了其他都一样）

```java
public class SimpleMovieLister {

    @Resource
    private MovieFinder movieFinder;

}
```

#### @Resource 的默认行为

以上代码，使用了注解 `@Resource` 声明了字段，但是并没有给属性 `name` 和 `type` 赋值，除此之外，在之前的学习中知道，私有的字段要进行依赖注入必须又 `setter` 方法，但是这里并没有 `settter` 方法。

**这是因为 Spring 规定第如果依赖使用 `@Resource` 对字段进行自动装配的话，就无需有 `setter` 方法了**。

**同时 Spring 还规定了如果 `@Resource` 的 `name` 属性并没有进行声明，那么 `name` 的默认值就是其变量名或者是 `setter` 的方法名（方法名去掉 set 单词后，首字母单词小写）**。

以上代码等同于下面代码：

```java
public class SimpleMovieLister {

    @Resource(name="movieFinder")
    private MovieFinder movieFinder;

}
```

### 方法注入

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;
    
    @Resource
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

}
```

等同于下面代码：

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;
    
    @Resource(name="movieFinder")
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

}
```

以上代码会在容器中寻找 `id/name` 为 `movieFinder` 的 Bean ，注入到该方法中。

如果开发者还想要进一步缩小匹配范围，那么可以使用 `type=""` 声明匹配 Bean 的类型。

如下：

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;
    
    @Resource(name="movieFinder", type="MovieFinder")
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

}
```

## @Resource 匹配原理

### 定义 name （默认行为）

`@Resource` 会根据开发者定义的 `name` 属性的值进行匹配，从容其中寻找 `id/name` 对应的值，然后进行注入。

如若 Spring 并没有根据 `id/name` 找到对应的 Bean，那么 `@Resource` 会转换为根据类型寻找，类型为 `name` 值首字母大写。

### 定义 type

使用 `@Resource` 不建议只定义 `type` ，如果只定义了 `type`，如果 Spring 寻找 Bean 的最终结果是多个，那么会直接报异常 `NoUniqueBeanDefinitionException`。

`@Resource` 注解并不能像 `Autowired` 那样可以和其他注解配合使用。

### 定义 type 和 name

开发者对 `@Resource` 的两个核心属性都进行了定义，那么 Spring 会按其对应的值进行匹配，如果匹配到多个 Bean ，会直接报异常 `NoUniqueBeanDefinitionException`。

### 不定义 type 和 name（这是常用的）

不定义任何属性的注入逻辑和 *定义 name* 的注入逻辑一样，区别就是：后者会根据 `name` 属性的默认值去进行匹配。

### 工作原理

#### CommonAnnotationBeanPostProcessor 

`@Resource` 实际的工作流程是由 `CommonAnnotationBeanPostProcessor ` （它是专门处理 JSR-250 规范注解的后处理器）来完成的。

该后处理器会通过与它关联的容器将 `name` 属性值解析为容器中具体的 Bean。

这也就表明了开发者不用关心 Bean 是如何完成依赖注入的，这些完完全全都是由 Spring 来进行管理的。

#### SimpleJndiBeanFactory

`SimpleJndiBeanFactory` 是 Spring 提供的一个特殊的 bean 工厂，用于集成 JNDI（Java Naming and Directory Interface，Java 命名和目录接口，通常用于查找外部资源，如数据库连接池、EJB 等）。

如果显式配置了 `SimpleJndiBeanFactory`，`@Resource` 的名称解析逻辑会改变：此时 `@Resource` 指定的名称不会再默认查找 Spring 容器内的 bean，而是会被路由到 JNDI 环境中去查找对应的资源（比如从应用服务器的 JNDI 上下文里找一个数据源）。

这种配置适用于需要从外部 JNDI 环境获取资源的场景（例如在 Java EE 容器中）。

#### 总结

Spring 并不建议开发者去定义 `SimpleJndiBeanFactory` ，原因如下：

核心原因是 **“保留代理的级别”**：Spring 的很多核心功能（如事务管理、AOP 增强、安全控制等）都是通过 “代理” 实现的。当一个 bean 被 Spring 管理时，Spring 会为它创建代理对象（如事务代理），并通过容器内部的 bean 注册表管理这些代理。

如果通过 `SimpleJndiBeanFactory` 让 `@Resource` 直接从 JNDI 解析资源，获取到的可能是原始对象（而非 Spring 代理后的对象），这会导致 Spring 的代理功能失效（比如事务控制不生效）。

因此，Spring 建议：

- 优先依赖默认行为：让 `@Resource` 通过 `ApplicationContext` 解析 Spring 容器内的 bean，确保代理机制正常工作。
- 如果确实需要访问 JNDI 资源，应使用 Spring 原生的 JNDI 查询功能（如 `JndiObjectFactoryBean` 或 `@JndiLookup`），这些功能会将 JNDI 资源 “接入” Spring 容器管理，使其像普通 Spring bean 一样被代理和增强，从而保留代理级别。

## 注意事项

1. `@Resource` 的使用位置是否正确，其只能用在属性字段、方法上，并不能像 `@Autowired` 那样使用在参数列表中。
2. `@Resource` 位于 `jakarta.annotation.Resource` 包（早期为 `javax.annotation.Resource`），需注意：
   - Java 8 及以下：`javax.annotation` 包通常包含在 JDK 中，无需额外依赖。
   - Java 9 及以上：由于 Java EE 模块被移除，需手动引入依赖（如 Maven）。
3. `@resource` 的核心逻辑是根据 `name` 去匹配 Bean，如果需求要使用到双重验证那么，可以 `name、type` 一起使用，但是不能滥用（这很有可能影响系统的性能）。

