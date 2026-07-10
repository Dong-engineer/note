# MessageSource

`MessageSource` 是 Spring 的核心接口，其主要功能，如下：

1. 多语言管理
2. 消息动态获取与参数替换
3. 层级化消息查找
4. 灵活的资源加载

这些功能由 `MessageSource` 不同的实现类完成具体细节。

`MessageSource` 的三个核心实现类：

1. `ResourceBundleMessageSource`
2. `ReloadableResourceBundleMessageSource`
3. `StaticMessageSource`

Spring 提供 `Message` 接口的目的是提供国际化（i18n）消息访问。

后面会分节讲述。

## MessageSource 源码

```java
public interface MessageSource {
    
    // 根据 code、参数、默认消息和 Locale 获取消息（找不到默认返回 defaultMessage）
    String getMessageSource(String code,@Nullable Object[] args, @Nullable String defaultMessage, Locale locale);
    
    // 根据 code、参数和 Locale 获取消息（无默认值，找不到则抛出异常）
    String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException;
    
    // 基于 MessageSourceResolvable 对象获取消息（支持更复杂的解析逻辑）
    String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException;
    
}
```

## MessageSource 的工作原理

当 `ApplicationContext` 被加载时，它会自动搜索定义在上下文中的 `MessageSource` Bean 。这个 Bean 必须有 `messageSource` 这个名字。如果 `ApplicationContext` 没有找到名称为 `messageSource` 的 Bean，容器会优先向上搜寻（也就说会通过父容器搜寻）。如果都没有找到，那么 Spring 会默认实例化一个空的 `DelegatingMessageSource`。

而 `DelegatingMessageSource` 仅仅作为 `MessageSource` 的空实例，不具备任何处理消息得到能力，仅是为了在调用 `getMessage()` 方法时确保不会抛出 `NoSuchMessageSourceException`。

`MessageSource` 的工作具体是由其实现类完成的，关于实现类的详细介绍本节不讲述，后面一一单独讲述。

每一个实现类的核心工作内容如下：

- `ResourceBundleMessageSource` 适用于加载 classpath 中的静态资源文件（如 properites 配置文件，这里可能会有开发者疑问，加载 properites 文件不是有 @PropertySource 或者 `PropertySourcesPlaceholderConfigurer` 吗？那么就请开发者带着疑问继续向下学习）
- `ReloadableResourceBundleMessageSource` 扩展了 `ResourceBundleMessageSource` 支持动态刷新资源文件，可加载文件系统，classpath 或 URL 中的资源
- `StaticeMessageSource` 简单的静态消息源，消息通过代码硬编码，主要用于测试或临时消息配置，不依赖外部资源
- 层级化消息查找功能主要是来自于 `HierarchicalMessageSource` 接口，该接口继承了 `MessagerSource` 

下面就分节详细讲述每一个类的作用
