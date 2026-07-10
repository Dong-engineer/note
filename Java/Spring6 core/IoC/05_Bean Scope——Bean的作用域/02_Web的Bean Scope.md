# Web的Bean_Scope

上节讲述 Bean Scope 其中的常用的两种 singleton 和 prototype。

这节讲述后面4种。

后面这4在非 Web 程序使用体会不到，这需要开发者去 Web 程序上去体会感知。

这也就意味着开发者不可以使用常规的容器（如：`ClassPathXmlApplictationContext`）去加载 Web 的 Bean Scope，只能使用 Spring 特定的容器（如：`XmlWebApplicationContext`）去使用 Web Scope。否则会发生 `IllegalStateException`  => 这表示有未知的 Bean Scope。

## Web Scope

在 Spring 官方的文当中讲述了一些初始 Web 配置，那些并不会影响你后面的学习，其里面的知识是关于 Servlet 的一些知识，没有学习过 Servlet 的开发者很有可能不太明白。

Spring 的文档中说了如果在 Spring MVC 去处理一个 `Htpp` 请求，开发者就无需去配置 Web 。

这里后面学习 Spring MVC 自然会学到。

如果是在 Servlet 中想要到达在一个请求中每次获取的实例均为同一个实例对象，那么开发者就需要对 Web 进行一些初始化配置，如使用过滤器 `RequestContextFilter` 或监听器 `RequestContextListene` 让获取的实例对象绑定到当前线程中（在 Web 应用中一般一个请求对应一个线程），从而实现在一个 Http 请求中每次获取的 Bean 实例是同一个。

总之无论是 Servlet 还是 Spring ，都是前辈们在开发过程中发现的种种问题，为了解决这些问题于是就衍生出后面种种框架，其解决问题的思路都是一样的。

以下内容是假设了开发者使用了 Spring MVC 。

## Request

request 将单个 Bean 的定义的作用有效范围是一次请求的生命周期。即在一个 Http 请求中每次获取的 Bean 实例是同一个。

### 基于XML

```xml
<!-- 如果不使用 Spring MVC，以下代码会报错-->
<bean id="loginAction" class="com.something.LoginAction" scope="request"/>
```

检测代码不再书写，具体的内容会在 Spring MVC 中讲述。

### 基于注解

```java
@RequestScope 
@Component
public class LoginAction {    
    // ... 
}
```

## Session

session 将单个 Bean 的定义的作用有效范围是一个 Web 端的 session 会话。即在一个 Web 端的 session 会话中每次获取的 Bean 实例是同一个。

### 基于XML

```xml
<bean id="userPreferences" class="com.something.UserPreferences" scope="session"/>
```

### 基于注解

```java
@SessionScope
@Component
public class UserPreferences {
    // ...
}
```

## Application

application将单个 Bean 的定义的作用有效范围是整个 Web 应用。即在整个 Web 应用中每次获取的 Bean 实例是同一个。

### 基于XML

```xml
<bean id="appPreferences" class="com.something.AppPreferences" scope="application"/>
```

### 基于注解

```java
@ApplicationScope
@Component
public class AppPreferences {
    // ...
}
```
