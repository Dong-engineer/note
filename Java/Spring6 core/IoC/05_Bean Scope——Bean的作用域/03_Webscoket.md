# Websocket

本节内容可以安全跳过，等阅读者对 Web Socket 有一定了解再来阅读。

## Websocket

websocket 不同于其他的 Scope ，它是 Spring 提供的特殊的 Bean 作用域，其生命周期与 WebSocket 会话（WebSocket Session）严格绑定，主要用于基于 WebSocket 的 STOMP（Simple Text Oriented Messaging Protocol）应用程序中，用于管理需要在整个 WebSocket 会话期间保持状态的 Bean 实例。

在 Spring 的文档中介绍很少，下面的内容来自网络。

### 使用 websocket 的前提

在使用 websocket 前提，需要引入其依赖，如下：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-websocket</artifactId>
    <version>5.3.x</version> <!-- 版本需与Spring核心一致 -->
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-messaging</artifactId>
    <version>5.3.x</version> <!-- 支持STOMP协议 -->
</dependency>
```

需要在 Spring 配置中注册`WebSocketScope`实例，并通过`@EnableWebSocketMessageBroker`启用 WebSocket 消息代理（STOMP 依赖）：

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.scope.WebSocketScope;

@Configuration
@EnableWebSocketMessageBroker // 启用STOMP消息代理
public class WebSocketConfig {

    // 注册WebSocket Scope
    @Bean
    public WebSocketScope webSocketScope() {
        return new WebSocketScope();
    }

    // 其他STOMP配置（如消息代理、端点映射等）
    // ...
}
```

通过`@Scope`注解指定作用域为`"websocket"`，并设置`proxyMode`（通常为`ScopedProxyMode.TARGET_CLASS`，确保依赖注入时能正确代理）：

```java
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChatSessionContext {
    private String username; // 当前会话的用户名
    private List<String> chatHistory = new ArrayList<>(); // 聊天记录

    // getter/setter
    // ...
    
    public void addMessage(String message) {
        chatHistory.add(message);
    }

}
```

### 注意事项

1. **依赖注入限制**：
   WebSocket Scope 的 Bean 不能被单例（singleton）Bean 直接依赖（单例 Bean 初始化时，WebSocket 会话可能尚未创建）。需通过`ScopedProxyMode`生成代理对象，延迟获取实际实例。
2. **会话隔离性**：
   每个 WebSocket 会话的 Bean 实例完全隔离，不同会话的 Bean 状态互不干扰（例如用户 A 的聊天记录不会混入用户 B 的会话）。
3. **STOMP 与 WebSocket 的绑定**：
   STOMP 协议是基于 WebSocket 的上层协议，其会话（STOMP Session）通常与 WebSocket 会话一一对应，因此 WebSocket Scope 对 STOMP 消息处理完全适用。
