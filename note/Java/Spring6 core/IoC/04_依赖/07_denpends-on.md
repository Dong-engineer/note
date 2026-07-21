# depends-on

如果一个Bean是另一个Bean的依赖，这通常意味着一个Bean被设置为另一个Bean的一个属性。一般可以直接在XML文件中中使用`<ref/>`或者`ref=`属性来引入外部Bean。然而，有时Bean之间的依赖关系并不那么直接。

一个例子是当一个类中的静态初始化器需要被触发时，比如数据库驱动程序的注册。`depends-on` 属性可以明确地强制一个或多个Bean在使用此元素的Bean被初始化之前被初始化。下面的例子使用 `depends-on` 属性来表达对单个 bean 的依赖性。

代码示列：

```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager"/> 
<bean id="manager" class="ManagerBean" />
```

如果要依赖多个Bean，如下：

```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager,accountDao">    
    <property name="manager" ref="manager" /> 
</bean> 
<bean id="manager" class="ManagerBean" /> 
<bean id="accountDao" class="x.y.jdbc.JdbcAccountDao" />
```

> `depends-on` 属性可以指定初始化时间的依赖关系，而在 [单例](https://springdoc.cn/spring/core.html#beans-factory-scopes-singleton) Bean的情况下，也可以指定相应的销毁时间的依赖关系。与给定Bean定义了 `depends-on` 的依赖Bean会在给定Bean本身被销毁之前被首先销毁。因此，`depends-on` 也可以控制关闭的顺序