# @Primary对基于注解自动装配的微调

基于注解的自动装配已经学习过了，但是开发者在开发时会发现，还是不够灵活，需要对注入的 Bean 进行筛选或者优先注入。

从这开始，就要学习自动装配的微调了，这块的知识在 XML 文件的学习中并没有讲述到，这里会做补充。

## @Primary

在进行依赖注入时，开发者会发现很有可能会有多个 Bean 作为依赖的选择，但是现在开发者不需要这么多的 Bean 作为候选者来进行依赖注入，那么 `@Primary` 可以帮助开发者实现该目的，

`@Primary` 表示，当多个Bean是自动注入到一个单值（single value）依赖的候选者时，应该优先考虑一个特定的Bean。如果在候选者中正好有一个主要（primary）Bean存在，它就会成为自动注入的值。

具体示例如下：

```java
// 定义一个接口
public interface MessageService {
    String getMessage();
}

// 实现类1
@Service
public class EmailService implements MessageService {
    @Override
    public String getMessage() {
        return "This is an email message";
    }
}

// 实现类2，使用@Primary指定为优先选择
@Service
@Primary
public class SmsService implements MessageService {
    @Override
    public String getMessage() {
        return "This is an SMS message";
    }
}

// 在需要注入的地方
@Controller
public class MessageController {
    // 当存在多个MessageService实现时，会优先注入被@Primary标注的SmsService
    @Autowired
    private MessageService messageService;
    
    // 使用messageService...
}
```

在以上代码中，`EmailService` 和 `SmsService` 都实现了 `MessageService` 接口。由于 `SmsService` 被标注了 `@Primary`，当使用 `@Autowired` 注入 `MessageService` 时，Spring 会优先选择 `SmsService`。

## 注意事项

在使用 `@Primary` 时需要注意以下几点：

1. 同类型的类，只能有一个类型声明为 `@Primary` 。

   - 在上节讲述过 `Autowired` 是根据类型进行自动注入的，所以如果有多个类型被声明为  `@Primary` ，Spring 会抛出 `NoUniqueBeanDefinitionException` 。

2. 不要过度使用 `@Primary` ，这会让代码降低可读性。

   - 这里可能就会开发者疑问，有第一条的注意事项的限制怎么会过度使用呢？

     在实际开发中，开发者很有可能遇见同一接口或抽象类的子类或者实现类，会成为其他 Bean 的依赖，这也导致每一个子类或者实现类都有可能会被声明为 `@Primary` （这显然并没有违背第一事项，子类与子类之间是不同类型的），这样做的话会大大降低代码可读性。

     **若多个实现类都可能作为 "默认"，应改用 `@Qualifier` 显式指定，而非滥用 `@Primary` **。（后面马上讲述不用着急）

3. 注意和其他注解的搭配使用（后面讲述到其他注解，同样会在注意事项里面详细讲述，请注意）。

## XML 的实现

对于注解 `@Primary` ，XML 的实现如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 定义EmailService Bean -->
    <bean id="emailService" class="com.example.EmailService"/>
    
    <!-- 定义SmsService Bean，并设置primary="true"使其成为优先选择的Bean -->
    <bean id="smsService" class="com.example.SmsService" primary="true"/>
    
    <!-- 定义MessageController Bean，Spring会自动注入MessageService -->
    <bean id="messageController" class="com.example.MessageController"/>

</beans>
```

