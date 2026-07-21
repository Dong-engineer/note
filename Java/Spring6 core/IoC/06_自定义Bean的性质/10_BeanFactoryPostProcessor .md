# BeanFactoryPostProcessor

`BeanFactoryPostProcessor` Bean 工厂后处理器，如同 `BeanPostProcessor` 一样，都是为了对 Bean 进行操作，但二者还是有很大区别的，下面从以下几点来介绍 `BeanFactoryPostProcessor` 。

- 源码（是什么）

- 作用（为什么）
- 触发时机
- 作用范围
- Spring 中的 `BeanFactoryPostProcessor`

## 源码

`BeanFactoryPostProcessor` 定义如下：

```java
@FunctionalInterface
public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
```

观察源码可以知道，其参数接受了一个 `ConfigurableListableBeanFactory` ，即一个 `BeanFactory` 。

## 警钟！！！

在后面的学习中会学习到 `BeanFactory` ， `BeanFactory` 是 Bean 的工厂，工厂前面简短讲述过（*Bean的实例化*一节）， `BeanFactory` 可以获取 Bean ，也是 `getBean()` ，在容器的学习中其实也用到了 `getBean()` 这两个一样只不过调用的接口不同，他们都会急切的实例化 Bean。

`ConfigurableListableBeanFactory` 也是一个 `BeanFactory` 这也就意味着开发者，可以通过 `ConfigurableListableBeanFactory` 急切的实例化 Bean，这当然是允许的。

！！！但是不允许的是，不可以在 `BeanFactoryProcessor` 中实例化 Bean ，这样做违背了 Bean 的生命周期，违背了 `BeanFactoryProcessor` 的设计初衷。

## 作用

`BeanFactoryPostProcessor` 用于在容器实例化 Bean 之前修改配置元数据。即修改 Bean 在注解或者 XML 文件中的依赖信息。

这也是和 `BeanPostProcessor` 不同的地方。

开发者可以通过实现 `BeanFactoryPostProcessor` 对 Bean 的配置元数据进行修改。

代码如下：

```java
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        
        // 获取 Bean
        // 注意这里是 getBeanDefinition() ,不是 getBean()
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");
        // 修改名为"userService"的bean定义
        beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        
        // 设置属性值
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.addPropertyValue("timeout", 3000);
    }
}
```

注意这里并不是直接修改 Bean 的依赖，而是修改 配置的元数据， Bean 在这时还没有实例化。

## 触发时机

在 *作用* 中其实，阅读者应该可以知道 `BeanFactoryPostProcessor` 的触发时间在 Bean 的实例化之前，在容器加载解析 Bean 元数据之后。

## 作用范围

`BeanFactoryPostProcessor` 和 `BeanPostProcessor` 都是 `PostProcessor` 接口，所以二者的作用范围都是容器级别的，即对容器中的所有 Bean 都有作用。

## Spring 中的 `BeanFactoryPostProcessor`

### `PropertySourcesPlaceholderConfigurer`

开发者可以使用 `PropertySourcesPlaceholderConfigurer` 来对 Bean 进行动态配置信息，这也使用到了配置文件（properties 文件）。 `PropertySourcesPlaceholderConfigurer` 实现了  `BeanFactoryPostProcessor ` 。

对于 properties 文件想必开发者应该并不陌生，从配置文件中取出对应的数据，对于认真学习过 Java 原生 API 或者 Servlet 的开发者很简单，在 Spring 中同样有这样的操作，那就是 `PropertySourcesPlaceholderConfigurer` 。

#### 配置数据

假设现在需要数据库链接，那么开发者就可以如下操作。

首先，知道 `PropertySourcesPlaceholderConfigurer` 实现了  `BeanFactoryPostProcessor` （源码中没有直接实现，而是通过继承方式实现的），那么这也就需要开发者去注册一个  `PropertySourcesPlaceholderConfigurer` ，就像注册开发者自己定义的 `BeanPsotProcessor` 那样。

同时 Spring 还在  `PropertySourcesPlaceholderConfigurer` 中添加了 `locations` 字段（其实是在 `PropertySourcesPlaceholderConfigurer` 的父类中加的）用于文件定位 ，即 Bean 是根据哪个配置文件进行动态获取元数据的。

具体代码如下：

```xml
<bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
    <property name="locations" value="classpath: xxx.properties"/>
</bean>
<bean id="dataSource" destroy-method="close"
        class="xxx.BasicDataSource">
    <property name="driverClassName" value="${jdbc.driver}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>
```

```properties
# 这里注意，一定要在 user 前面去加上 jdbc，因为 PropertySourcesPlaceholderConfigurer 实现了 EnvironmentAware 接口，EnvironmentAware聚合很多的环境变量，包括系统环境变量，加上 jdbc 是为了更好的让 PropertySourcesPlaceholderConfigurer 区分具体的 user 变量。
jdbc.user=root
jdbc.password=1146299837wd
jdbc.url=jdbc:mysql://localhost:3306/Dong
jdbc.driver=com.mysql.cj.jdbc.Driver
```

### 取值

配置数据已经学习过了，那么如何取出对应的值呢？

在上面的代码已经将方法呈现出来，即通过 `${变量名}` 来进行取值。

在容器加载 Bean 的定义和解析 Bean 时，会识别  `${变量名}` ，通过 `BeanFactoryPostProcessor` 的核心方法修改 Bean 的配置元数据，再去完成 Bean 的实例化。

### `PropertyOverrideConfigurer`

`PropertyOverrideConfigurer` 是另一个 Bean factory 的后处理器，与 `PropertySourcesPlaceholderConfigurer` 相似，但与后者不同的是，原始定义可以为Bean属性设置默认值或根本没有值。如果覆盖的 `Properties` 文件中没有某个Bean属性的条目，就会使用默认的上下文定义。

对于 `PropertySourcesPlaceholderConfigurer` ，它侵入了开发者的XML文件，即 `${}` ，但这种侵入对开发者的代码影响很小，而 `PropertyOverrideConfigurer` 的重写机制并不会侵入开发者的XML文件。

### 配置数据

```java
public class DatabaseConfig {
    // 默认值
    private String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/defaultDB";
    private String user = "defaultUser";
    private String password = "defaultPass";

    // getter和setter
    // ...
}
```

```properties
# 和 XML 文件中 Bean的id或name 保持一致，字段名也得保持一致
dataBase.user=root
dataBase.password=1146299837wd
dataBase.url=jdbc:mysql://localhost:3306/Dong
dataBase.driver=com.mysql.cj.jdbc.Drive
```

```xml
<bean class="org.springframework.beans.factory.config.PropertyOverrideConfigurer">
    <property name="locations" value="classpath: xxx.properties"/>
</bean>

<bean id="dataBase" class="com.example.DatabaseConfig">
    <!-- 这里可以设置默认值，也可以不设置（使用类中定义的默认值） -->
</bean>
```

以上代码因为使用的是 `PropertyOverrideConfigurer` ，无需再像 `PropertySourcesPlaceholderConfigurer` 那样使用 `${}` 来进行占位，`id=dataBase` 的 Bean 在初始化时会根据 `classpath:` 所指定文件中的数据信息重写属性字段。

**但是要保证 classpath 所指定文件中的变量名称及其字段名称分别与 XML 文件 Bean 的 id 和类所拥有的字段名一致**。

### 取值

`PropertyOverrideConfigurer` 的取值逻辑和 `PropertySourcesPlaceholderConfigurer` 不一样，`PropertySourcesPlaceholderConfigurer` 是通过识别占位符 `${}` 来进行取值。

`PropertyOverrideConfigurer` 必须保证配置文件中的格式为 `Bean的id(name).字段名=值` 才能覆盖字段默认或原有值。且字段名必须和 `setter、getter` 保持一致。

## 补充

同样的 `BeanFactoryProcessor` 也受 `Ordered` 接口影响。