# Bean 的详解

在前言中我们已经初步了解了 Bean 的基本概念，本节将深入讲解 Spring 容器中 Bean 的核心定义与配置细节。

## BeanDefinition

Spring IOC 容器管理的所有对象都被称为 Bean，这些 Bean 并非直接由开发者创建，而是容器根据**配置元数据**（如 XML 配置、注解等）实例化并管理的。

在 Spring 的容器内部，所有的 Bean 都会被抽象化成一个 `BeanDefinition` 对象，在源码中是这样描述的，如下：

```java
/**
 * A BeanDefinition describes a bean instance, which has property values,
 * constructor argument values, and further information supplied by
 * concrete implementations.
 * 
 * BeanDefinition 描述了一个 bean 实例，BeanDefinition 拥有 bean 的属性值，有参构造器，和所继承的信息，等等
 * ...
 */
 public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {
     ...
 }
```

在 `BeanDefinition` 中 Spring 为 Bean 定义了以下数据信息（除了开发者提供的配置元数据之外）：

1. **全限定类名**：即 Bean 对应实例的具体实现类路径（例如 `com.example.service.UserServiceImpl`）；
2. **行为配置**：定义 Bean 在容器中的运行行为，如作用域（Scope）、生命周期回调方法（初始化 / 销毁方法）等；
3. **依赖引用**：当前 Bean 工作所需的其他 Bean 引用（也称为 “合作者” 或 “依赖项”）；
4. **其他配置参数**：用于初始化 Bean 的自定义属性，例如连接池的大小限制、线程池的核心线程数等。

这些核心数据最终转化为 `BeanDefinition` 的一组可配置属性，也是我们在配置文件中定义 Bean 时的核心配置项，具体如下表所示：

| 属性                     | 解释…                                  |
| :----------------------- | :------------------------------------- |
| Class                    | 实例化 Bean 所属的类路径               |
| Name                     | Bean 命名                              |
| Scope                    | Bean Scope                             |
| Constructor arguments    | 有参构造依赖注入                       |
| Properties               | setter 依赖注入                        |
| Autowiring mode          | 注入协作者（Autowiring Collaborators） |
| Lazy initialization mode | 懒加载的Bean                           |
| Initialization method    | 初始化回调                             |
| Destruction method       | 销毁回调                               |
| id                       | Bean唯一标识                           |

这些属性是 Spring 配置 Bean 的核心，无论是 XML 配置文件还是注解配置，最终都会映射到 `BeanDefinition` 的这些属性上。

## 总结

1. Spring 容器内的 Bean 本质上是 `BeanDefinition` 对象的实例化结果，`BeanDefinition` 是 Bean 的核心元数据定义；
2. `BeanDefinition` 包含类路径、行为配置、依赖引用、自定义参数四类核心信息；
3. 表格中的属性是配置 Bean 的核心项，覆盖了 Bean 的标识、实例化、依赖注入、生命周期等全流程。
