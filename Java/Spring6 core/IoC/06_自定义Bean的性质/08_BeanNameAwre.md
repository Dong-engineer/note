# BeanNameAware

和 `ApplicationContextAware` 接口一样，都是在 Bean 中尝试去获取容器中的内容，但是 `BeanNameAware ` 的影响非常小。

接口（部分）定义如下：

```java
public interface BeanNameAware {

    void setBeanName(String name) throws BeansException;
}
```

`setBeanName()` 可以让 Bean 获取其自身在容器中的 `name`。

同样的，当 Spring 容器初始化 Bean 时，会检测该 Bean 是否实现了 `BeanNameAware` 接口，如果实现了，容器会自动调用 `setBeanName()` 方法。

其具体的调用时间在 *01_Bean的生命周期* 有所讲述。