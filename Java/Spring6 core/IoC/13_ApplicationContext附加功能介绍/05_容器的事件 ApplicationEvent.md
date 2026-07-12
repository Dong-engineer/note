# 容器的事件 ApplicationEvent

Spring 中的事件和前端的事件有些许类似，但并不完全相同，Spring 的事件设计初衷主要是为了 Bean 与 Bean 之间的通信。

说到 Bean 与 Bean 之间的通信，可能就有开发者疑问，Bean 与 Bean 之间的联系不是可以使用依赖来表示或者使用 `depends-on` 来表示不行吗？

可以这完全是没有任何问题的，但是开发者需要知道一旦两个 Bean 之间形成了依赖关系，那也就意味着二者具有很强的耦合度，Spring 通过定义事件，然后由事件发布者发布事件，然后监听事件就达到了解耦合的关系。

对于事件的内容作者准备分三节外加一节总结来讲述。

下面就是详细的内容。

## 源码

```java
public abstract class ApplicationEvent extends EventObject {

    private static final long serialVersionUID = 7099057708183571937L;
	
    // 事件发生的事件戳
    private final long timestamp;

	// 构造方法接受一个事件源对象，并自动记录当前时间
    public ApplicationEvent(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }

    // 允许传入自定义事件戳，一般用于测试环境
    public ApplicationEvent(Object source, Clock clock) {
        super(source);
        this.timestamp = clock.millis();
    }
    
	// 获取事件戳
    public final long getTimestamp() {
        return this.timestamp;
    }

}
```

通过以上源码开发者不难发现，`ApplicationEvent` 继承了 `EventObject` ，`EventObject` 是 JDK 中的原生类，Java 规定了其是事件模型的基准类型，也就是说开发者想要自定义事件就需要继承 `EventObjet` 类，除此之外，所有的事件源都必须包含一个属性 `source` 但是在源码中是没有的，这是因为隐式继承了 `EventObject` 中的属性（详细内容可以搜索 `EventObject` 源码）。

## 使用

Spring 规定了开发者自定义的事件需要继承 `ApplicationEvent` ，这不仅满足了 Java 原生内容的规定，同时还原有功能的基础上添加了时间戳的功能，所以在此作者夸一下 Spring（Spring 不愧是行业标杆框架）。

### 自定义事件

假设现在需要一个事件，如下：

```java
// 自定义用户注册事件
public class UserRegisteredEvent extends ApplicationEvent {
    private User user;
    
    public UserRegisteredEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
    
    // getter方法
    public User getUser() {
        return user;
    }
    
}
```

那么 `AppliactionEvent` 的工作到此结束，接下来就是 `ApplicationPublisher` 和 `ApplicationListener` 的工作了。