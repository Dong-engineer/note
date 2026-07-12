# AnnotationConfigApplicationContext

通过这节标题，开发者应该猜出这节要讲什么内容了吧。

这节讲述注解容器的实例化。在 XML 的学习中开发者知道需要使用 `ClassPathXMLApplicationContext` 进行实例化容器，Groovy 的实例化则需要 `GenericGroovyApplicationContext`。当然开发者也可以使用最灵活的实例化容器 `GenericApplicationContext`。

如果忘了这些内容可以回去在看一下，毕竟 Spring 的 Bean 和容器的知识点即将结束。

## AnnotationConfigApplicationContext 的使用

示例代码如下：

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

以上代码开发者会发现，将 `AppConfig` 这一类作为参数，与 XML 容器实例化对比，开发者可以将 `AppConfig.class` 认为是一个 XML 文件，这样看就知道了为什么需要 `AppConfig` 这个类了。

但前提是 `AppConfig` 类必须含有 `@Configuration` 这一注解。

`AnnotationConfigApplicationContext` 并不仅仅局限于 `@Configuration` ，`@Component` 等标识了容器对其扫描的注解类，都可以通过 `AnnotationConfigApplicationContext` 将其实例化，再去通过 `getBean()` 获取其容器内的 Bean 实例化对象。 

### 注册容器（register）

开发者在开发时可能需要将不同的 `AppConfig` 配置进行容器实例化，如果还像上面那样操作，那么一个 `AppConfig` 配置就会是一个容器，这对于开发者来说如果要进行后续的 `getBean()` 这会很麻烦。

Spring 提供了 `register()` 方法对 `AppConfig` 配置进行注册，可以将多个 `AppConfig` 配置注册到容器中。

示例代码如下：

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(AppConfig.class, OtherConfig.class);
    ctx.register(AdditionalConfig.class);
    ctx.refresh(); // 刷新容器
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

### 开启组件扫描

组件扫描在之前已经讲述过了，这里再重新温习一遍，如果开发者没有再 `AppConfig` 开启组件扫描，对于含有 `@Component` 的组件是没有注册到容器中的。

开启扫描可以使用注解 `@ComponentScan` 开启。

也可以使用 XML 的 `<context:component-scan/>` 开启扫描。

也可以使用容器的 `scan()` 开启扫描。

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.scan("com.acme");
    ctx.refresh();
    MyService myService = ctx.getBean(MyService.class);
}
```
