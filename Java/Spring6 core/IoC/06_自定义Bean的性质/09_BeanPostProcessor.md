# BeanPostProcessor

`BeanPsotProcessor` 在 Spring 的教程文档中是单独一节存在的，但是 `BeanPsotProcessor` 还是跟 Bean 的生命周期紧紧相连，而且在第一节提到了 `BeanPostProcessor` ，所以提前放到这里来讲述。

在 Spring 的原文中对 `BeanPostProcessor` 的描述是 Bean 的后处理器，注意这里的后处理器和第一节的所标志的前置处理和后置处理并不对应，Bean 的后处理器可以对 Bean 进行前置处理和后置处理。

什么是前置处理、后置处理呢？

## `BeanPostProcessor` 接口

先来看一下 `BeanPostProcessor` 的接口定义，如下：

```java
public interface BeanPostProcessor {

    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
```

这是 `BeanPostProcessor` 的两大核心方法。`postProcessBeforeInitialization()、postProcessAfterInitialization()` 。

这两个方法也对应着前置处理和后置处理，什么是前置处理、什么是后置处理，在 *01_Bean的生命周期* 里面有提到，下面主要讲前置处理、后置处理应该干些什么。

### 前置处理（postProcessBeforeInitialization()）

看完 `BeanPostProcessor` 接口的源码后，返回值是一个 Object，但是 **Spring 有明确且严格的规定该方法必须返回一个非 null 的对象，它可以是原 Bean 实例，也可以是其代理对象。** 。

前置处理发生在 Bean 的初始化回调前，在初始化会回调前可以对 Bean 进行一些扩展处理（不能对其进行修改，后面有专门的）。

其实到这里，可能就有开发者（无经验）就会有疑问了，对 Bean 进行处理有 `ApplicationContextAware` 和 `BeanNameAware` ，为什么还要用 `BeanPostProcessor` ？

这个问题作者会单独出一节，讲述其区别。

### 后置处理（postProcessAfterInitialization()）

后置处理大致和前置处理一样，区别就是触发时机不一样。

无论是前置处理还是后置处理，都是为了能更加灵活的让开发者在 Bean 的生命周期中去做一些更加完善的工作。

### 怎么用

`BeanPostProcessor` 开发者可以认为是一个全局工具 Bean，开发者无需在实体类中单独继承，只需要开发者去定义一个工具 Bean 即可，容器会在初始化时去检测哪些 Bean 实现 `BeanPostProcessor` 接口。

假设现在开发者需要跟踪 Bean 的初始化回调，那么可以如下操作：

```java
public class TailAfterPostProcessor implements BeanPostProcessor{
	
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("正在跟踪初始化前的 Bean：" + beanName);
        System.out.println("Bean 的类型为：" + bean.getClass().getSimpleName());
        
        // 对 Bean 的其他操作
        
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("正在跟踪初始化后的 Bean：" + beanName);
        System.out.println("Bean 的类型为：" + bean.getClass().getSimpleName());
        
        // 对 Bean 的其他操作
        
        return bean;
    }
}
```

假设现在有业务类 `UserSrvice`，那么在 `UserService` 的初始化前后，会打印日志信息，如下：

```java
public class UserService {
    private String name;
    
    // 构造方法
    public UserService() {
        System.out.println("UserService构造方法执行");
    }
    
    // 初始化方法，将在XML中配置
    public void init() {
        System.out.println("UserService初始化方法执行，当前名称: " + name);
    }
    
    // 业务方法
    public void doService() {
        System.out.println("用户服务执行: " + name + "的服务");
    }
    
    // getter和setter
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

}
```

```xml
<!-- 注册自定义的BeanPostProcessor -->
<bean class="xxx.TailAfterPostProcessor"/>

<!-- 定义业务Bean，并指定初始化方法 -->
<bean id="userService" class="xxx.UserService" init-method="init">
    <property name="name" value="测试用户"/>
</bean>
```

```java
public static void main(String[] args) {
    // 加载XML配置文件初始化Spring容器
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

    // 获取Bean并调用业务方法
    UserService userService = (UserService) context.getBean("userService");
    userService.doService();
}
```

## Ordered

`Ordered` 接口，和 `Phased` 一样都是用作与决定 Bean 的执行先后顺序，但二者有很大的区别。同样的放在单独一节来讲述。

在本节开发者需要牢记，对 `BeanPostProcessor` 的执行顺序有影响的，只有 `Ordered`。

`Ordered` 接口定义如下：

```java
package org.springframework.core;

public interface Ordered {

    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    int getOrder();

}
```

实现其接口通过重写 `getOrder()` 来决定执行先后顺序，和 `Phased` 的 `getPhase()` 一样。

## AutowiredAnnotationBeanPostProcessor

`AutowiredAnnotationBeanPostProcessor` 即 @Autowired（这里目前暂时没讲过这个注解，但阅读者对XML的 `autowired` 属性应该并不陌生，@Autowired 是其注解形式，后面会出一节单独讲述 Spring 中的注解）。

将回调接口或注解与自定义 `BeanPostProcessor` 实现结合起来使用，是扩展Spring IoC容器的一种常见手段。一个例子是Spring的 `AutowiredAnnotationBeanPostProcessor` — 一个 `BeanPostProcessor` 实现，它与 Spring distribution 一起，自动注入注解字段、setter方法和任意的配置方法。
