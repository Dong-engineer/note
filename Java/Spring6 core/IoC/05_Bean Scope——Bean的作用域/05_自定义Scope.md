# 自定义 Scope

Spring 的 Bean 的 Scope 是可以扩展的，开发者可以定义属于开发者自己的 Scope ，甚至重新定义现有的 Scope ，但是 Spring 并不建议你这么做，所以 Spring 直接禁止了开发者修改  `singleton` 和 `prototype` scope。

## 创建自定义Scope

自定义的 Scope 需要实现 Spring 的 `org.springframework.beans.factory.config.Scope` 接口。这是 Spring Scope 的核心接口。

那么在创建自定义的 Scope 之前先来了解一下 Spring 的核心接口 Scope。

### org.springframework.beans.factory.config.Scope

```java
public interface Scope {
    // 从当前作用域中获取Bean实例（若不存在则通过ObjectFactory创建并存储）
    Object get(String name, ObjectFactory<?> objectFactory);
    
    // 从当前作用域中移除Bean实例
    Object remove(String name);
    
    // 注册Bean的销毁回调（作用域结束时执行，如释放资源）
    void registerDestructionCallback(String name, Runnable callback);
    
    // 解析作用域内的上下文对象（如request作用域中的HttpServletRequest）
    Object resolveContextualObject(String key);
    
    // 获取当前作用域的唯一标识（如request的ID、线程ID等）
    String getConversationId();
}
```

这是 `Scope` 接口的全部源码，每个方法的作用如下：

- `get(String name, ObjectFactory<?> objectFactory)`：核心方法，负责从作用域中获取 Bean。若当前作用域中没有该 Bean，会通过`ObjectFactory`创建实例并存储到作用域中。
- `remove(String name)`：移除作用域中指定名称的 Bean 实例，并返回被移除的实例（若存在）。
- `registerDestructionCallback(String name, Runnable destructionCallback)`：注册一个销毁回调，当作用域结束或 Bean 被移除时，Spring 会执行该回调（例如释放数据库连接）。
- `resolveContextualObject(String key)`：返回作用域内的上下文对象（可选实现，如 request 作用域可返回当前`HttpServletRequest`）。
- `getConversationId()`：返回当前作用域的唯一标识（用于区分不同作用域实例，如线程 ID、会话 ID）。

### 实现 Scope

假设现在有一个 I/O 流，这个流中只能有一个文件 Bean 实例对象，那么开发者可以自定义一个 `Scope` ，如下：

```java
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义I/O流作用域：确保同一个I/O流处理过程中只有一个Bean实例
 */
public class IOScope implements Scope {

    // 存储当前线程正在处理的I/O流对应的Bean实例
    // key: 流标识ID, value: Bean实例映射
    private final Map<String, Map<String, Object>> streamBeans = new ConcurrentHashMap<>();
    
    // 存储销毁回调
    // key: 流标识ID, value: 该流的Bean销毁回调
    private final Map<String, Map<String, Runnable>> destructionCallbacks = new ConcurrentHashMap<>();
    
    // 绑定当前线程正在处理的流标识
    private static final ThreadLocal<String> currentStreamId = new ThreadLocal<>();

    /**
     * 设置当前线程处理的流标识
     * 通常在流处理开始时调用
     */
    public static void setCurrentStreamId(String streamId) {
        currentStreamId.set(streamId);
    }

    /**
     * 清除当前线程的流标识
     * 通常在流处理结束时调用
     */
    public static void clearCurrentStreamId() {
        currentStreamId.remove();
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        // 获取当前流标识
        String streamId = currentStreamId.get();
        if (streamId == null) {
            throw new IllegalStateException("未设置当前I/O流标识，请先调用setCurrentStreamId方法");
        }

        // 获取当前流对应的Bean容器，不存在则创建
        Map<String, Object> beans = streamBeans.computeIfAbsent(streamId, k -> new HashMap<>());

        // 如果Bean不存在，则通过ObjectFactory创建并存储
        if (!beans.containsKey(name)) {
            Object bean = objectFactory.getObject();
            beans.put(name, bean);
        }

        return beans.get(name);
    }

    @Override
    public Object remove(String name) {
        String streamId = currentStreamId.get();
        if (streamId == null) {
            return null;
        }

        // 移除Bean实例
        Map<String, Object> beans = streamBeans.get(streamId);
        Object removedBean = beans != null ? beans.remove(name) : null;

        // 移除对应的销毁回调并执行
        Map<String, Runnable> callbacks = destructionCallbacks.get(streamId);
        if (callbacks != null) {
            Runnable callback = callbacks.remove(name);
            if (callback != null) {
                callback.run();
            }
        }

        // 如果流的Bean容器为空，清理整个容器
        if (beans != null && beans.isEmpty()) {
            streamBeans.remove(streamId);
            destructionCallbacks.remove(streamId);
        }

        return removedBean;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        String streamId = currentStreamId.get();
        if (streamId == null) {
            return;
        }

        // 注册销毁回调
        destructionCallbacks.computeIfAbsent(streamId, k -> new HashMap<>())
                            .put(name, callback);
    }

    @Override
    public Object resolveContextualObject(String key) {
        // 可以根据key返回当前流的上下文信息，比如流ID、流对象等
        if ("streamId".equals(key)) {
            return currentStreamId.get();
        }
        return null;
    }

    @Override
    public String getConversationId() {
        // 返回当前流的唯一标识作为会话ID
        String streamId = currentStreamId.get();
        return streamId != null ? streamId : UUID.randomUUID().toString();
    }

    /**
     * 销毁指定流的所有Bean实例
     * 通常在流处理完成后调用
     */
    public void destroyStream(String streamId) {
        if (streamId == null) {
            return;
        }

        // 执行所有销毁回调
        Map<String, Runnable> callbacks = destructionCallbacks.remove(streamId);
        if (callbacks != null) {
            callbacks.values().forEach(Runnable::run);
        }

        // 移除所有Bean实例
        streamBeans.remove(streamId);
    }
}
```

自定义的 Scope 已经完成，那么怎么让 Spring 识别到该 Scope呢？

### 将自定义 Scope 注册到 Spring 中

#### 常规配置

 Spring 为开发者提供了注册方法—— `registerScope(String scopeName, Scope scope)`。

首先，肯定要将自定义的 `scope` new出来，因为 `registerScope(String scopeName, Scope scope)`，接受一个 `Scope` 类型的参数，同时还为其取名为 `scopeName`。

如下：

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Spring配置类：用于注册自定义的I/O流作用域(IOScope)
 * 作用：使Spring容器能够识别并管理使用@Scope("ioScope")注解的Bean
 */
@Configuration
public class IOScopeConfig {

    /**
     * 定义自定义Scope的Bean
     * 该方法会创建IOScope实例并注册到Spring容器中
     * 
     * @return 自定义的I/O流作用域实例
     */
    @Bean
    public IOScope ioScope() {
        return new IOScope();
    }

    /**
     * 注册自定义Scope到Spring容器
     * 通过BeanFactoryPostProcessor在容器初始化阶段完成作用域注册
     * 
     * @return 用于注册自定义作用域的BeanFactoryPostProcessor实例
     */
    @Bean
    public BeanFactoryPostProcessor scopeRegistrar() {
        // 返回BeanFactoryPostProcessor的匿名实现类
        return new BeanFactoryPostProcessor() {
            /**
             * 在Bean工厂初始化后处理配置，注册自定义作用域
             * 
             * @param beanFactory Spring的Bean工厂实例
             */
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
                // 将BeanFactory转换为可配置类型，以使用注册作用域的方法
                ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
                // 注册名为"ioScope"的自定义作用域，关联到之前定义的IOScope实例
                // 后续可通过@Scope("ioScope")注解使用该作用域
                configurableBeanFactory.registerScope("ioScope", ioScope());
            }
        };
    }
}
```

以上代码你会发现，`registerScope()` 方法是通过 `BeanFactory` 来调用的，这是因为 `registerScope()` 是在 `ConfigurableBeanFactory` 接口上声明的，但是 `BeanFactory` 实现了其大部分功能。

其次，开发者就可以在 XML 文件中使用了：

```xml
<bean id="..." class="..." scope="IOScope">
```

#### 基于 XML 注册 Scope

```xml
<!-- 1. 定义自定义Scope实例Bean -->
<bean id="ioScope" class="com.example.IOScope"/> <!-- 替换为实际包路径 -->

<!-- 2. 注册自定义Scope到Spring容器 -->
<bean class="org.springframework.beans.factory.config.CustomScopeConfigurer">
    <!-- 通过property注入自定义Scope映射 -->
    <property name="scopes">
        <map>
            <!-- 
                key: 自定义Scope的名称（后续用@Scope("ioScope")引用）
                value-ref: 引用上面定义的ioScope实例Bean
            -->
            <entry key="ioScope" value-ref="ioScope"/>
        </map>
    </property>
</bean>

<!-- 3. 定义使用自定义Scope的Bean（可选，也可通过注解定义） -->
<bean id="fileProcessor" class="com.example.FileProcessor" scope="ioScope">
    <!-- 如果需要，可配置属性注入 -->
</bean>
```

以上代码无需百分之百完全弄懂，不懂的地方可以跳过，后面会详细讲述每一个类使用来干什么的。