# ReloadableResourceBundleMessageSource

在第一节 *01_MessageSource* 就已经介绍过 `ReloadableResourceBundleMessageSource ` 其作用，主要用于加载 classpath 、URL 等文件体系资源，且支持动态刷新。

在第一节 *01_MessageSource* 中说到 `ReloadableResourceBundleMessageSource ` 扩展了 `ResourceBundleMessageSource` 这并不表示 `ReloadableResourceBundleMessageSource ` 继承了 `ResourceBundleMessageSource` 。

实际上 `ReloadableResourceBundleMessageSource ` 和 `ResourceBundleMessageSource` 都继承了 `AbstractMessageSource` （`ReloadableResourceBundleMessageSource ` 实际是通过 `AbstractResourceBasedMessageSource` 继承的），除此之外 `ReloadableResourceBundleMessageSource ` 还实现了 `ResourceLoaderAware` 接口，因此说 `ReloadableResourceBundleMessageSource ` 扩展了 `ResourceBundleMessageSource` 。

下面就详细介绍。

## 源码

```java
public class ReloadableResourceBundleMessageSource extends AbstractResourceBasedMessageSource implements ResourceLoaderAware {

    // 缓存过期时间（毫秒），-1 表示永久缓存
    private long cacheMillis = -1;

    // 资源文件编码（默认 UTF-8）
    private String defaultEncoding = "UTF-8";

    // 按 basename 存储的编码映射（可针对不同 basename 配置不同编码）
    private Map<String, String> fileEncodings;

    // 资源加载器（用于读取 classpath、文件系统等资源）
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    // 缓存：key 为 "basename_locale"，value 为缓存的资源属性
    private final Map<String, PropertiesHolder> resourceBundleCache = new ConcurrentHashMap<>(64);

    // 缓存是否允许并发访问
    private boolean concurrentRefresh = false;

    // ... 构造方法与 setter 方法

    // 以下是核心方法但并不代表全部
    // 获取消息束
    protected PropertiesHolder doGetBundle(String basename, Locale locale) {
        // 生成缓存键（如 "i18n/messages_zh_CN"）
        String cacheKey = basename + '_' + locale.toString();
        PropertiesHolder holder = this.resourceBundleCache.get(cacheKey);

        // 检查缓存是否有效（未过期且存在）
        if (holder != null && !holder.isExpired()) {
            return holder;
        }

        // 缓存无效，需要刷新（加锁保证线程安全）
        synchronized (this.resourceBundleCache) {
            holder = this.resourceBundleCache.get(cacheKey);
            if (holder != null && !holder.isExpired()) {
                return holder;
            }

            // 刷新资源并更新缓存
            holder = refreshProperties(basename, locale);
            this.resourceBundleCache.put(cacheKey, holder);
            return holder;
        }
    }

    // 刷新资源
    private PropertiesHolder refreshProperties(String basename, Locale locale) {
        long lastModified = -1;
        Properties properties = new Properties();

        // 构建资源文件名（如 "i18n/messages_zh_CN.properties"）
        String filename = basename + '_' + locale.toString();
        Resource resource = this.resourceLoader.getResource(filename + ".properties");

        // 加载资源文件
        if (resource.exists()) {
            // 记录文件最后修改时间（用于后续判断是否更新）
            lastModified = resource.lastModified();
            // 读取资源内容（指定编码）
            properties = loadProperties(resource, getEncoding(basename));
        }

        // 计算缓存过期时间（cacheMillis 为 -1 时永久有效）
        long expirationTime = (this.cacheMillis < 0 ? Long.MAX_VALUE : System.currentTimeMillis() + this.cacheMillis);
        return new PropertiesHolder(properties, lastModified, expirationTime);
    }

    // 读取资源文件内容（支持指定编码）
    private Properties loadProperties(Resource resource, String encoding) throws IOException {
        Properties props = new Properties();
        InputStream is = resource.getInputStream();
        try {
            // 关键：支持显式编码（解决中文乱码）
            if (encoding != null) {
                props.load(new InputStreamReader(is, encoding));
            } else {
                props.load(is);
            }
        } finally {
            is.close();
        }
        return props;
    }

    // 内部类
    // 用于判断缓存是否过期
    private static class PropertiesHolder {
        private final Properties properties;
        private final long lastModified; // 资源最后修改时间
        private final long expirationTime; // 缓存过期时间

        public PropertiesHolder(Properties properties, long lastModified, long expirationTime) {
            this.properties = properties;
            this.lastModified = lastModified;
            this.expirationTime = expirationTime;
        }

        // 判断缓存是否过期
        public boolean isExpired() {
            // 永久缓存（cacheMillis = -1）则不过期
            if (expirationTime == Long.MAX_VALUE) {
                return false;
            }
            // 否则判断当前时间是否超过过期时间
            return System.currentTimeMillis() > expirationTime;
        }
    }
}
```

认真的开发者阅读以上代码一定会发现，在 `ReloadableResourceBundleMessageSource ` 中没有 `basenames` 属性，其实是有的，在 `AbstractResourceBasedMessageSource` 的源码中是含有 `private String[] basenames = new String[0]` 的。

## 使用示例

和 `ResourceBundleMessageSource` 的用法差不多，开发者需要自定义一个或多个配置文件，这里直接使用上节的配置文件，以便后面 `ReloadableResourceBundleMessageSource` 和 `ResourceBundleMessageSource` 二者的对比。

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
<bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
	<property name="basename" value="message"/>
    <property name="dafaultEncoding" value="UTF-8"/>
    <!-- 刷新时间 30s -->
    <property name="cacheMillis" value="30000"/>
</bean>
```

### 基于注解的配置

```java
@Configuration
public class MessageSourceConfig {

    // 配置消息源
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // 设置资源文件基础路径（支持 classpath 或文件系统路径）
        messageSource.setBasename("classpath:i18n/messages"); 
        // 设置缓存时间（30 秒刷新一次）
        messageSource.setCacheMillis(30000); 
        // 设置文件编码
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
```

### 使用

```java
@RestController
public class I18nController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/getMsg")
    public String getMessage(String name) {
        // 获取消息（参数1：消息键；参数2：占位符参数)
        return messageSource.getMessage("user.name", new Object[]{name});
    }
}
```

```cmd
getMessage("张三") -> 你的名字是：张三
```

## 工作流程

这里就省略了，其工作流程和 `ResourceBundleMessageSource` 一样。



### 区别

二者的区别就是动态刷新和资源获取定位不一样，`ResourceBundleMessageSource` 虽然也支持动态刷新，但是其动态刷新的能力有限且远不如 `ReloadableResourceBundleMessageSource` 那样效率高。

所以开发者一定要牢记以下内容：

`ResourceBundleMessageSource` ：主要用于静态的，且文件资源在 classpath 下的文件。 

`ReloadableResourceBundleMessageSource` ：主要用于动态，文件资源在 classpath、URL 、等等均可以使用。

| 特性         | ResourceBundleMessageSource              | ReloadableResourceBundleMessageSource               |
| ------------ | ---------------------------------------- | --------------------------------------------------- |
| 刷新粒度     | 全局刷新（`clearCache()` 清除所有缓存）  | 细粒度刷新（仅更新过期的 basename+Locale）          |
| 触发机制     | 依赖 `ResourceBundle` 原生缓存，被动触发 | 自主实现缓存管理，主动检查文件修改时间              |
| 外部文件支持 | 对文件系统资源的动态更新支持较弱         | 能可靠感知文件系统中资源的修改（通过 lastModified） |
| 性能影响     | 全局刷新可能导致瞬时性能波动             | 细粒度刷新，性能影响小                              |