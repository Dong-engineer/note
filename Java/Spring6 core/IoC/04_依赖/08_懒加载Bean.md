# 懒加载（Lazy Loading）

**懒加载（Lazy Loading）是一种设计模式，其核心思想是延迟加载资源，直到需要时才进行加载**。

在Spring6中，容器`ApplicationContext`**默认急切地创建和配置所有的 单例Bean**，作为容器初始化过程的一部分。

Spring6这样做大大提高编程效率，加入开发者在开发时对于某个Bean的使用或者配置有错误，但没有及时发现并更改，这会可能导致重大的错误，所以Spring6在容器初始化时，急切地创建和配置了所有单例Bean。

当然在开发时，开发人员确保了Bean的使用无误，并且希望该Bean不再随着容器初始化加载，那么可以时如下操作：

```xml
<bean id="lazy" class="com.something.ExpensiveToCreateBean" lazy-init="true"/>
<bean name="not.lazy" class="com.something.AnotherBean"/>
```

以上使用了`lazy-init=true`配置了`id="lazy"`的Bean，这样做Bean就不会再随着容器加载而加载，只有当其使用时才会被创建加载。

也可以通过使用 `<beans/>` 元素上的 `default-lazy-init` 属性来控制容器级的懒加载，如下例所示。

```xml
<beans default-lazy-init="true">
</beans>
```