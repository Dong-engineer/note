# Bean 的 ID 和 name

这里分开介绍 Bean 的 `id` 和 `name` 属性。

## Bean 的 `id` 属性

`id` 是 Bean 的**唯一标识符**，在整个 Spring 容器中必须唯一，用于精准定位和引用某个 Bean。

如下：

```xml
<bean id="accountDaoId"  calss="com.spring.springioc.dao.impl.AccountDaoImpl"/>
```

> 说明：一个 Bean 只能有一个 `id`，且 `id` 需符合 XML 标识符规范（不能包含特殊字符如逗号、空格等）。

## Bean 的 `name` 属性

如果需要为 Bean 设置**多个名称（别名）**，可以使用 `name` 属性。`name` 属性支持同时定义多个名称，名称之间可通过逗号（`,`）、分号（`;`）或空格分隔。

如下：

```xml
<bean name="account,account2" class="com.spring.springioc.dao.impl.AccountDaoImpl"/>
```

> 说明：通过 `name` 设置的多个名称，均可用于获取该 Bean，相当于为同一个 Bean 配置了多个 “别称”。

## `<alias/>` 标签

然而，有的 Bean 可能需要多个别名且并不会再同一 `<bean/>` 标签中去定义。

这种情况通常发生在大型系统中，配置被分割到每个子系统中，每个子系统都有自己的对象定义集。在基于XML的配置元数据中，你可以使用 `<alias/>` 标签来实现这一点。下面的例子展示了如何做到这一点。

```xml
<bean name="fromName" class="com.spring.springioc.dao.impl.VipDaoImpl"/>

<alias name="fromName" alias="toName"/>
```

在这种情况下，一个名为 `fromName` 的 Bean（在同一个容器中）在使用这个别名定义后，也可以被称为 `toName`。

> 说明：配置后，原本通过 `forName` 访问的 Bean，也可通过 `toName` 访问；`<alias/>` 标签的 `name` 属性指定原 Bean 名称，`alias` 属性指定新增的别名。

## 总结

1. `id` 是 Bean 的唯一标识，容器内不可重复，用于精准定位 Bean；
2. `name` 可直接为 Bean 设置多个别名，支持逗号 / 分号 / 空格分隔；
3. `<alias/>` 标签用于为已定义的 Bean 新增别名，适合多配置文件的大型系统场景。