# ResourceBundleMessageSource

`ResourceBundleMessageSource` 基于 Java 标准的 `ResourceBundle` 实现，适用于加载 classpath 中的静态资源文件（ 如 properties 文件）。

下面详细介绍，不要漏掉细节。

再本节开始之前，先了解一些术语：

**资源束**：一种用于存储特定语言 / 地区（Locale）消息的容器，其核心作用是将程序中的硬编码文本（如提示信息、标签等）与代码分离，通过不同的资源束文件适配多语言场景。其存储消息的方式是以键值对来存储的，这也就对应了配置文件的格式。

## 源码

```java
public class ResourceBundleMessageSource extends AbstractMessageSource {

    // 资源文件名称
    @Nullable
    private String[] basenames;

    // 资源文件编码，默认基于 ISO-8859-1
    private String defaultEncoding = "ISO-8859-1";

    // 是否允许缓存资源束
    private boolean cachResource = true;

    // 资源束缓存时间, -1 表示永久缓存
    private long cachMillis = -1;

    // 当指定 Locale 无资源时，是否回退到系统默认 Locale（默认 true）
    private boolean fallbackToSystemLocalee = true;

    // 缓存资源束的 Map（key: 基础名称 + Locale，value: 资源束及元数据）
    private final Map<String, LocaleBundleHolder> cachedResourceBundles = new ConcurrentHashMap<>();

    // setter 和 构造方法
    // ...

    // 以下为：	核心方法
    @Override
    @Nullable
    protected String resolveCode(String code, Locale locale) {
        for(String basename: getBaseNames()) {
            // 加载该基础名称和 Local 对应的资源束
            ResourceBundle bundle = getResourceBundle(basename, locale);
            // 如果资源束存在且其中包含对应的 code 信息数据，那么从资源束中获取信息
            if(bundle != null && bundle.containsKey(code)) {
                return getStringOrNull(bundle, code);
            }

        }
        // 否则直接返回 null
        return null;
    }

    // 获取资源束（带缓存逻辑）
    @Nullable
    protected ResourceBundle getResourceBundle(String basename, Locale locale) {
        // 构建缓存 key（基础名称 + Locale）
        String cacheKey = basename + '_' + locale;
        LocaleBundleHolder holder = cachedResourceBundles.get(cacheKey);

        // 检查缓存是否有效（未过期）
        if (holder != null && !holder.isExpired()) {
            return holder.getResourceBundle();
        }

        // 缓存无效，重新加载资源束
        synchronized (cachedResourceBundles) {
            holder = cachedResourceBundles.get(cacheKey);
            if (holder != null && !holder.isExpired()) {
                return holder.getResourceBundle();
            }

            // 实际加载资源束的逻辑
            ResourceBundle bundle = doGetBundle(basename, locale);
            if (bundle != null) {
                // 计算缓存过期时间
                long expirationTime = (cacheMillis < 0 ? Long.MAX_VALUE : System.currentTimeMillis() + cacheMillis);
                // 更新缓存
                cachedResourceBundles.put(cacheKey, new LocaleBundleHolder(bundle, expirationTime));
                return bundle;
            } else {
                // 未找到资源束，从缓存中移除
                cachedResourceBundles.remove(cacheKey);
                return null;
            }
        }
    }

    @Nullable
    protected ResourceBundle doGetBundle(String basename, Locale locale) {
        try {
            // 尝试加载指定 Locale 的资源束
            ResourceBundle bundle = ResourceBundle.getBundle(
                basename, locale, getBundleClassLoader(), new MessageSourceControl());
            return bundle;
        } catch (MissingResourceException ex) {
            // 未找到资源束时的处理
            if (logger.isDebugEnabled()) {
                logger.debug("No resource bundle found for basename " + basename + ", locale " + locale, ex);
            }
            return null;
        }
    }

}
```

以上源码并不完整，且很复杂，需要开发者耐心阅读，下面作者会和开发者一起边阅读边做解读。

## ResourceBundleSource 的使用

根据 `ResourceBundleSource` 的核心工作内容，开发者需要定义一个配置文件，如下：

```properties
# 文件名：messages.properties(默认)
# 文件名有标准的命名规范（i18n），请开发者遵循命名规范进行开发
user.name="你的名字是：{0}"
user.age="你的年龄是：{0}"
```

```properties
# 文件名：messages_zh_CN.properties
user.name="你的名字是：{0}"
user.age="你的年龄是：{0}"
```

```properties
# 文件名：messages_en_US.properties
user.name="Your name is：{0}"
user.age="Your age is：{0}"
```

### 基于 XML 的配置

```xml
<!-- 这里对 Bean id 的命名一定要是 messageSource, 不然 Spring 容器是识别不到该 MessageSource 的-->
<bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
    <!-- 配置文件资源基础名（即不带文件扩展名的名字） -->
    <property name="basenames" value="messages"></property>
    <!-- 资源文件编码方式 -->
    <property name="defaultEncoding" value="UTF-8"></property>
    <!-- 关闭 Local 的回退 -->
    <property name="fallbackToSystemLocalee" value="false"></property>
</bean>
```

### 基于注解的配置

```java
@Configuration
public class MessageConfig {

    // 这里返回的类型也可以使用多态
    // public ResourceBundleMessageSource resourceBundleMessageSource() {}
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setfallbackToSystemLocale(false);
        return messageSource;
    }
}
```

### 使用

```java
@Service
public class UserService {
    @Autowired
    private MessageSource messageSource;

    public String getWelcomeMessage(String username) {
        // 获取中文消息
        return messageSource.getMessage("user.welcome", new Object[]{username}, Locale.CHINA);
    }

    public String getAgeMessage(int age) {
        // 获取英文消息
        return messageSource.getMessage("user.age", new Object[]{age}, Locale.US);
    }
}
```

```cmd
getWelcomeMessage("张三") -> 你的名字是：张三
getAgeMessage(20) -> Your age is 20
```

## 工作原理

`ResourceBundleSource` 的工作逻辑是：从消息代码和 Locale 文件中加载并返回对应的消息。

1. **接受消息**：外部通过 `getMassage()` 方法请求消息

2. **查找资源束**：

   - 根据 `basenames` 配置的名称（假设名称是 `Dong`）和接受的 `Locale` （zh_CN） ，构建资源文件名称（结果为：`Dong/message_zh_CN.properties`）

   - 尝试加载 `Dong/message_zh_CN.properties` 该资源文件，若不存在，则按规则回退（以 `Dong/message_zh_CN.properties` 为例子，`Dong/message_zh_CN.properties` -> `Dong/message_zh.properties` -> `Dong/message.properties`）
   - 若开发者将 `fallbackToSystemLocale` 设定为了 `true`  ，当 Locale 资源都不存在时，会尝试加载系统默认 Local 对应的资源。

3. **解析消息**：从匹配的资源束中读取 `code` 对应的消息文本，使用 `MessageFormat` 处理参数替换（如 `Hello {0}` → `Hello World`）。

4. **缓存资源**：加载的资源束会被缓存（按 `cacheMillis` 配置），避免重复 IO 操作。

下面用上面的代码为示例，详细讲述其工作流程（ `ResourceBundleMessageSource` 再厉害他也是个 Bean，是 Bean 就有 Bean的生命周期）：

### 解析 Bean 的定义

Spring 在解析 XML 文件和注解时，识别到开发者定义了 `id` 为 `messageSource` 的 `ResourceBundleMessageSource` 的 Bean，接下来看容器启动阶段

### 容器启动

容器启动，Spring 会创建一个 `ResourceBundleMessageSource`  的实例并注册到容器中。

### 依赖注入

注入 `ResourceBundleMessageSource`  所需要的依赖，如：`basenames`、`defaultEncoding`、`fallbackToSystemLocalee`、等等。与此同时 `ResourceBundleMessageSource`  内部会初始化 `cachedResourceBundles` 缓存。

接下来是一些接口的回调，源码中是没有实现特殊生命周期回调接口的，初始化和销毁回调需要开发者自定义，所以省略这一部分的讲述。

### Bean  的使用

即 `ResourceBundleMessageSource` 的使用，开发者使用 `getMessage()` 来获取消息，根据 `getMessage()` 接受的参数，`ResourceBundleMessageSource` 会处理参数信息，并跟据配置文件返回对应信息。

这中间会构建资源束，Spring 重载了很多 `getMessage()` 包括不含 `Locale` 参数的 `getMessage()`，如果没有参数 `Locale` ，那么 `ResourceBundleMessageSource` 会在 `basenames` 中寻找没有任何 i18n 尾椎的 `message` 文件，如果有参数 `Locale`，那么 `ResourceBundleMessageSource` 会在 `basenames` 中寻找符合 i18n标准规范的  `message` 文件。

然后是格式化消息，如果配置文件中的消息是格式化，那么 `ResourceBundleMessageSource` 会通过 `MessageFormate` 根据参数 `new Object[]{}` 中每一个元素的下标对配置文件中的占位符进行替换。

最后返回结果。

下面的生命周期不在讲述因为并没有实现特殊的生命周期控制接口。

## ResourceBundleMessageSource 的缓存机制

`ResourceBundleMessageSource` 对配置文件内容的处理，离不开 IO 操作，其本质也是 IO 操作，对配置文件消息内容的读取需要大量重复的 IO 操作，这非常影响系统的运行速率。

Spring 在 `ResourceBundleMessageSource` 中内部定义了缓存 `cachedResourceBundles` ，首次读取过配置文件后，`ResourceBundleMessageSource` 会构建很对资源束，这些资源束则被存储到了 `cachedResourceBundles` 缓存中。在那之后再读取配置文件信息，就会直接从缓存中读取。

在其源码中定义了 `cacheMillis` ，开发者可以自定义其时间，用来刷新缓存，默认 -1 表示永久存储。
