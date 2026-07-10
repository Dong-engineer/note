# Bean Scope

Bean Scope ——Bean的作用域，在上节其实就已经了解过，即单例 Bean、多例 Bean。

作用域和单例 Bean 、多例 Bean 有什么关系呢？

**Bean Scope 指的是单个 Bean 的定义作用的有效范围**。

而 Bean 的作用决定了以下 Bean 的属性：

1. 这个 Bean 实例何时被创建
2. 实例的存活周期（何时被销毁）
3. 哪些组件可以访问到这个实例

单例 Bean 就是单个 Bean 的定义有效范围是单个的 Bean 实例对象，即整个容器中只会包含一个该 Bean 实例对象，所以该 Bean 会随着容器初始化而初始化。

多例 Bean 就是单个 Bean 的定义有效范围是任何数量的 Bean 实例对象。

详细的内容下面一点一点讲述。

## Bean Scope有哪些

Spring 提供 6 种Bean的作用域，如下：

| Scope       | 说明                                                         |
| :---------- | :----------------------------------------------------------- |
| singleton   | （默认情况下）为每个Spring IoC容器将单个Bean定义的Scope扩大到单个对象实例。 |
| prototype   | 将单个Bean定义的Scope扩大到任何数量的对象实例。              |
| request     | 将单个Bean定义的Scope扩大到单个HTTP请求的生命周期。也就是说，每个HTTP请求都有自己的Bean实例，该实例是在单个Bean定义的基础上创建的。只在Web感知的Spring `ApplicationContext` 的上下文中有效。 |
| session     | 将单个Bean定义的Scope扩大到一个HTTP `Session` 的生命周期。只在Web感知的Spring `ApplicationContext` 的上下文中有效。 |
| application | 将单个Bean定义的 Scope 扩大到整个 Web 应用的生命周期中。只在Web感知的Spring `ApplicationContext` 的上下文中有效。 |
| websocket   | 将单个Bean定义的 Scope 扩大到 `WebSocket` 的生命周期。仅在具有Web感知的 Spring `ApplicationContext` 的上下文中有效。 |

### singleton

singleton，所有 Bean 的默认作用域。

在整个容器中只有一个该 Bean 的实例被共享，即每次请求获取 Bean 只能获取同一个 Bean 实例。

假设现有多个 Bean 实例引用 Bean A（作用域为singleton），那么这些 Bean 会从容器中引用同一个 Bean A，图解如下：

![](C:\Users\86198\Desktop\学习\Java\Spring6\05_Bean Scope——Bean的作用域\img\singleton图示.png)

根据对象的性质使用代码测试一下：

先来一个类：

```java
public class MyClass {
    
	public MyClass() {
		System.out.println("MyClass无参构造调用");
	}
    
}
```

```xml
<!-- scope="singleton" 是所有bean的默认属性，可以不用显式声明 -->
<bean id="myClass" Class="XXX.MyClass" scope="singleton"/>
```

```java
// 测试类
public class MyClassTest {

	public static void main(String[] args) {
		ApplicationContext applictationContext = new ClassPathXmlApplicationContext("XXX.xml");
        MyClass myClass = applicationContext.getBean("myClass", MyClass.class);
        System.out.println(myClass);
        MyClass myClass1 = applicationContext.getBean("myClass1", MyClass.class);
        System.out.println(myClass1);
        MyClass myClass2 = applicationContext.getBean("myClass2", MyClass.class);
        System.out.println(myClass2);
	}
	
}
```

```cmd
MyClass无参构造调用
TestClass.MyClass@20435c40
TestClass.MyClass@20435c40
TestClass.MyClass@20435c40
```

经以上观察，代码中对同一个 Been 的定义进行获取的对象均为同一个对象。

### prototype Scope

Prototype，将单个 Bean 的定义扩大至任意数量的实例对象。

在整个容器中，每当请求一次该 Bean ，就会去实例一个对象。

图示如下：

![](C:\Users\86198\Desktop\学习\Java\Spring6\05_Bean Scope——Bean的作用域\img\Prototype 图示.png)

同样根据对象的性质来测试以下：

依然使用 `MyClass`

```xml
<!-- 声明 scope 为 prototype -->
<bean id="myClass" class="xxx.MyClass" scope="prototype "/>
```

```java
// 测试类
public class MyClassTest {

	public static void main(String[] args) {
		ApplicationContext applictationContext = new ClassPathXmlApplicationContext("XXX.xml");
	    MyClass myClass = applicationContext.getBean("myClass", MyClass.class);
	    System.out.println(myClass);
	    MyClass myClass1 = applicationContext.getBean("myClass1", MyClass.class);
	    System.out.println(myClass1);
	    MyClass myClass2 = applicationContext.getBean("myClass2", MyClass.class);
	    System.out.println(myClass2);
	}

}
```

```cmd
MyClass无参构造调用
TestClass.MyClass@7aaca91a
MyClass无参构造调用
TestClass.MyClass@44c73c26
MyClass无参构造调用
TestClass.MyClass@41005828
```

上文两种 scope 的代码，显然易见，scope 不仅决定了单个 Bean 的定义可实例化的数量，还决定了其声明周期也随之变化。

singleton 只会让容器初始化一次，且初始化的时机是容器初始化的时机（在 Bean 的声明周期中会讲述到）。

prototype 会让单个 Bean 的定义在不同的请求中实例化不同的对象，且每个对象的初始化时机是请求时的时机。 

其余的 scope 会后面分节来讲，这里不再多述。

## 补充

当一个 singleton 的 Bean 依赖了一个 prototype 的 Bean ，那么对于 prototype 的 Bean 来说，其依赖注入的时机只有一次，就是 singleton 的 Bean 进行注入的时候。这也就是之前学习的 *方法注入* 的问题，详细的内容可以去看 *方法注入* 。