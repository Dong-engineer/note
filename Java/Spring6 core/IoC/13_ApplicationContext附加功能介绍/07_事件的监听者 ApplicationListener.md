# 事件的监听者 ApplicationListener

事件发布后，就是事件监听者的工作了，下面详细介绍。

## 源码

```java
@FunctionalInterface
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
	
    // 监听事件的方法，接受一个事件对象
    void onApplicationEvent(E event);

}
```

通过源码可以得知，事件的监听者本质其实也是一个事件，不过监听者在事件的原有基础上扩展了监听功能。而 `EventListener` 是 JDK，即 Java 原生的事件监听基准类型。

## 使用

事件监听者应当监听某一类事件，这样说可能部分开发者理解有误，下面就详细解释。

在 *容器事件 ApplicationEvent* 一节中自定义了事件，细心的开发者会发现以下代码，

```java
// 自定义用户注册事件
public class UserRegisteredEvent extends ApplicationEvent implements UserEvent{
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

将用户注册给定义成一个事件类，如果继续向上抽象，还可以抽象成 `UserEvent` 用户事件，可以让 `UserRegisteredEvent` 继承抽象类 `UserEvent` 用于表示这是一个用户事件（显然这是错误的，因为已经继承了 `ApplicationEvent` 但是开发者可以定义一个 `UserEvent` 接口），然后开发者定义一个事件监听者，专门监听 `UserEvent` 这一类事件。

如下：

事件发布

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

事件监听

```java
@Component
public class UserListener implements ApplicationListener<UserEvent> {
    
    // 重写方法，处理用户相关事件
    @Override
    public void onApplicationEvent(UserEvent event) {
        // 可以根据具体事件类型进行不同处理
        if (event instanceof UserRegisteredEvent) {
            handleUserRegisteredEvent((UserRegisteredEvent) event);
        }
        // 可以添加其他用户事件的处理逻辑
        // else if (event instanceof OtherUserEvent) { ... }
    }
    
    // 处理用户注册事件的具体方法
    private void handleUserRegisteredEvent(UserRegisteredEvent event) {
        User user = event.getUser();
        System.out.println("监听到用户注册事件：");
        System.out.println("执行后续操作，如发送欢迎邮件给用户：" + user.getUsername());
        // 这里可以添加实际业务逻辑，如发送邮件、记录日志等
    }
}
```

### 基于注解的实现

Spring 提供注解以便于开发者进行开发者在监听者所需要监听的方法上使用 `@EventListener` 即可监听者无需再实现 `ApplicationListener` 接口，如下：

```java
@Component
public class UserListener {
    
    // 该方法也无需再重写了
    // public void onApplicationEvent(UserEvent event)
    
    @EventListener(UserEvent.class)
    // 处理用户注册事件的具体方法
    private void handleUserRegisteredEvent(UserRegisteredEvent event) {
        User user = event.getUser();
        System.out.println("监听到用户注册事件：");
        System.out.println("执行后续操作，如发送欢迎邮件给用户：" + user.getUsername());
        // 这里可以添加实际业务逻辑，如发送邮件、记录日志等
    }
}
```

### 基于 XML 的实现

基于 XML 的实现需要用到事件监听映射配置接口 `SimpleApplicationEventMulticaster` 该接口通过 `map` 集合方式实现事件的监听，如下：

```xml
<!-- 注册事件监听器 -->
<bean id="userListener" class="com.example.UserListener"/>

<!-- 配置事件监听映射：将事件与监听器方法关联 -->
<bean id="applicationEventMulticaster" class="org.springframework.context.event.SimpleApplicationEventMulticaster">
    <property name="eventListeners">
        <map>
            <!-- 键：事件类型全限定名，值：监听器bean和方法的映射 -->
            <entry key="com.example.UserRegisteredEvent">
                <bean class="org.springframework.context.event.MethodInvokingEventListener">
                    <property name="targetObject" ref="userListener"/>
                    <property name="targetMethod" value="handleUserRegisteredEvent"/>
                </bean>
            </entry>
        </map>
    </property>
</bean>
```

## 监听的同步与异步

事件经过 `ApplicationPublicsher` 发布后，监听者并不会说监听到了该信息直接进行信息反馈，这是因为事件的所有监听是同步的。也就是说事件的监听实际是由顺序的，优先监听的事件会等待顺序低的事件监听后，同时进行监听者的信息处理逻辑。

开发者希望事件的监听是异步的，那么可以通过注解 `@Async` 进行异步监听，即监听者不会再等待所有事件全部完成后进行信息处理。

开发者选择异步后，事件可以通过 `@order` 注解进行事件顺序的设置。
