# ApplicationContextAware

如果开发者想在 Bean 中获取容器，那么 `ApplicationContextAware` 是个不错的选择，Spring 不允许在 Bean 中直接获取容器对象，那样做的话违反了 IOC 原则，因为 Bean 感知到自身被管理的容器所在，并且尝试访问容器中的内容。

在实际开发中仍有一些问题常规的依赖注入是无法完成的，这就需要开发者的 Bean 实现 `ApplicationContextAware` 接口从而完成动态对依赖的注入，显然这也违背了 IOC ，但不得不这样做。 实现 `ApplicationContextAware` 接口会让开发者的 Bean 更加灵活。

接口（部分）定义如下：

```java
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
```

```java
@Component
public class MyApplicationContextAware implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        // 保存容器引用
        applicationContext = context;
    }
}
```

实现 `setApplicationContext()` 后无需手动调用获取容器引用。

因为 Spring 设置了当容器初始化时，会检测所有实现 `ApplicationContextAware` 的 Bean，容器会自动调用 `setApplicationContext()` 并将自身作为参数传入。

其具体的调用时间在 *01_Bean的生命周期* 有所讲述。

`ApplicationContextAware` 具有强大的功能，它能让开发者的 Bean 与 Bean 之间进行通信，还提供了对文件资源的访问，发布应用程序事件，以及访问 `MessageSource`，具体的方法就不再讲述，可以去参考 Spring API 文档。

## 注意事项

过度使用 `ApplicationContextAware` 会让开发者的代码和 Spring 耦合在一起，所以在使用时应当小心使用。

这会非常考研开发者对 Spring 的熟悉度。
