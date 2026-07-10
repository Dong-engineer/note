# Bean的继承

Bean 的继承不同于 Java 的继承，但基本的还是一样的，下面详细讲述。

Java 中的继承是类与类之间的继承，但是子类并不能直接继承父类的属性值，但是 Bean 可以。

**Spring 规定了一个子 Bean（这里注意是 Bean ，而不是类） 可以继承父 Bean 的构造函数参数、属性值和容器特有的信息，如初始化方法、静态工厂方法名称等等。**

请注意子 Bean 继承父 Bean 并不意味着，二者的类是继承关系，Bean 的继承更强调的是属性、方法、构造函数的复用，而非类型的继承关系。

在 Spring 中子 Bean 统一是由 `ChildBeanDefinition` 类表示。但是 Spring 不建议开发者通过 `ChildBeanDefinition` 来进行对子 Bean 的开发工作。

## 基于XML

开发者可以通过XML文件的方式，声明子 Bean 和 父 Bean。

如下：

```xml
<bean id="inheritedTestBean" abstract="true"
        class="org.springframework.beans.TestBean">
    <property name="name" value="parent"/>
    <property name="age" value="1"/>
</bean>

<bean id="inheritsWithDifferentClass"
        class="org.springframework.beans.DerivedTestBean"
        parent="inheritedTestBean" init-method="initialize">  (1)
    <property name="name" value="override"/>
    <!-- 这里没有重写 age 属性，默认会继承父 Bean 的 age 属性 -->
</bean>
```

观察以上代码可以发现二者的 `class` 类型并不相同，这是允许的，但是有条件。

子 Bean 继承父 Bean 的前提条件，子 Bean对父 Bean 的一切都必须具有兼容性，即子 Bean 的一切的数据类型都必须对应能接受父 Bean 的一切数据类型。

## `abstract`

上面使用 `abstract` 属性声明了父 Bean 为抽象的，当让也可以不用，如果父 Bean 没有声明 `class` ，那么父 Bean就必须使用  `abstract` 属性声明为抽象的。

被 `abstract` 属性声明的 Bean 不能进行实例化，只能作为父 Bean 被继承。
