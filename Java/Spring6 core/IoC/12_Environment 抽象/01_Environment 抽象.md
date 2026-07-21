# Environment 抽象

`Environment` 实际是一个接口，Spring 将应用程序的环境（application environment）集成到其里面。用于表示应用程序运行时的环境配置，它管理了外部配置和环境标识，为应用提供了一致的配置方式。

其核心建模主要是两个方面：（配置文件）`profiles` 、（属性）`properites` 。

下面就分开来讲。

## profiles

`profiles` -> 配置文件，配置文件开发者并不陌生，典型的以 `properites` 为尾椎的文件，就是配置文件，当然开发者所学习的 XML 文件也是配置文件，还有 Groovy 文件也是配置文件，JSON 文件等等。

在实际开中，配置问价广发应用，但是在使用时很容易将这些配置问价交织在一起，XML 配置 Bean 的定义需要使用 `<import/>` 引入其他 XML 文件就是很好的例子。开发者在开发时可以很清楚的知道配置文件的具体内容什么意思、哪些配置文件什么时候用、用在哪里，但对于后面的维护者来说，这是一门相当吃力的工作。

Spring 集成了应用程序中所有 profiles ，开发者可以通过代码将这些配置文件进行隔离。

## properites

`properites` -> 属性，在应用程序开发时，开发者需要用到其开发环境或者生产环境的系统环境、JVM系统环境、JNDI等等一系列的环境属性来获取值。

对于上面的 profiles 也可以理解为环境的一种（配置环境）。

Spring 帮助开发者集成所有的环境，开发者只需要通过 `Environment` 接口及其实现类来获取即可。



接下来呢就详细讲述 `Environment` 接口具体内容。

## Environment 的工作原理

`Environment` 接口继承了 `PropertyResolver` ，其子接口是 `ConfigurableEnvironment` 。

`Environment` 的主要功能来自于 `PropertyResolver`，二者源码如下：

```java
public interface PropertyResolver{
    
    // 判断是否包含某个属性
    boolean containProperty(String key);
    
    // 获取属性值(字符串)，不存在返回 null
    String getProperty(String key);
    
    // 获取属性值（指定类型），不存在则返回默认值
    <T> T getProperity(String key,Class<T> type,T defaultValue);
    
    // 获取必须存在的属性（不存在则抛出异常）
    String getReqiredProperty(String key) throws IllegalStateException;
    
    // 解析包含占位符的字符串（如"${app.name}"）
    String resolvePlaceholders(String text);
}
```

```java
public interface Environment extends PropertyResolver{
    
    // 这里注意 Environment 继承了 PropertyResolver，所以 PropertyResolver 的方法也能用
    // Environment 在 PropertyResolver 的基础上扩展了 profile 标识功能（这是一个注解），后面会讲述的
	
    // 获取当前激活的 profiles (如["dev"])
    String[] getActiveProfiles();
    
    // 获取默认的 profiles (当没有激活的 profiles 时使用)
    String[] getDefaultProfiles();
    
    // 判断当前环境是否接受指定的 profiles
    boolean acceptsProfiles(Profiles profiles);
    
}
```

其实仔细阅读以上代码，开发者不难发现 `Environment` 的属性解析功能实际来自于接口 `PropertyResolver` ，而对于 profiles 进行的建模就是扩展的功能，即 `@profile` 标识功能。

## ConfigurableEnvironment

`ConfigurableEnvironment` 是 `Environment` 的子接口，该接口是一个可配置接口，提供了修改环境配置的能力（如设置激活的 `profiles`、添加属性源等）。

其源码如下：

```java
public interface ConfigurabelEnvironment extends Environment, ConfigurableProperResolver{
    
    // 注意，这指书写了 COnfigurableEnvironment 的核心方法，其继承了 Environmen 和 ConfigurableProperResolver 的方法并未书写出来
    
    // 设置激活的 profiles
    void setActiveProfiles(String... profiles);
    
    // 添加激活的 profiles
    void addActiveProfiles(String profiles);
    
    // 设置默认的profiles
    void setDefaultProfiles(String... profiles);
    
    // 获取属性源的管理对象
    MutablePropertySources getPropertySources();
    
    // 获取系统环境变量
    Map<String, Object> getSystemEnvironment();
    
    // 获取JVM系统属性
    Map<String, Object> getSystemEnvironment();
}
```

## Environment 的实现类

### AbstractEnvironment

`AbstractEnvironment` 是所有 `Environment` 类的所有基类，实现了 `ConfigurableEnvironment` 接口的核心逻辑，具体源码不再展示，这里作了解即可。

### StandardEnvironment

`StandardEnvironment` 是适用于非 Web 应用的默认环境实现，继承了 `AbstractEnvironment` 。其会默认加载两个属性源（按优先级排序）：

- `systemProperites`：JVM 系统属性（如 `-Duser-name=xxx-`）。
- `systemEnvironment`：操作系统环境变量（如 `path`）。

### StandardServletEnvironmen

`StandardServletEnvironmen` 是 Web 应用的环境实现（如 Spring MVC），继承了 `StandardEnvironment` ，其在 `StandardEnvironment` 的基础上添加了 Web 相关的属性源。

- `servletConfigInitParams`：ServletConfig 的初始化参数（`web.xml` 中 `<servlet>` 的 `<intit-param>`）。
- `servletContextInitParams`：ServletContext 的初始化参数（`web.xml` 中 `<context-param>`）。
- `jndiProperites`：JNDI 属性（如 Java 容器中的配置）。

Web 的属性源优先级高于系统属性源，所以 Web 的属性源会覆盖系统属性的变量值。

## Environment 的使用

对于 `Environment` 的使用这里不讲述，优先讲一些关于 `Environment` 注解的东西，然后分章节讲述 `Environment` 接口及其实现类或子类的使用。

