# 容器事件的发布者 ApplicationEventPublisher

开发者在上节自定义事件后，需要事件的发布者将事件发布到容器中，下面详细讲述。

## 源码

```java
@FunctionalInterface
public interface ApplicationEventPublisher {

    default void publishEvent(ApplicationEvent event) {
        publishEvent((Object) event);
    }

    void publishEvent(Object event);

}
```

源码没有什么好说的，看使用。

## 使用

同样接上节自定义代码。

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

用户注册逻辑由业务层完成。

```java
@Service
public class UserService {
    @Autowired
    private ApplicationEventPublisher publisher;
    
    public void register(String username) {
        // 业务逻辑：用户注册
        System.out.println("用户 " + username + " 注册成功");
        
        // 发布事件
        publisher.publishEvent(new UserRegisteredEvent(this, username));
    }
}
```

在这里开发者会发现使用了注解 `@Autowired` ，对容器比较熟悉的开发者可能并没有这个疑问（这个知识点在 Spring 官方文档中只是一笔提过），容器在初始化时会额外自动初始化一些容器配置，如：事件发布者 `ApplicationEventPublisher` 以及等等。这是因为 `ApplicationContext` 继承或者实现了这些接口。

简单来讲，在容器初始化后，有些组件就已经存在在容器中了，且具体的实现细节 Spring 也帮助开发者实现了（当然也可以自定义），直接使用即可。

这也就是为什么直接在 `private ApplicationEventPublisher publisher` 上使用 `@Autowired` 的原因。