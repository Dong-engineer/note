# PropertySource 抽象

`PropertySource` 是对配置源的抽象，其封装了键值对形式的配置数据（如系统属性、环境变量、配置文件等），是 `Environment` 抽象实现配置访问能力的组件。准确的来说，在 `Environment` 接口中管理了一组 `PropertySource` （实际是 `PropertyResolver` 中管可以通过方法获取一组 `PropertySource` 对象），开发者可以通过方法搜索这些 `PropertySource` 。

所有的外部配置（无论是 `application.properties` 、命令行参数、数据库）都通过 `PropertySource` 统一接入 Spring 的环境体系。

而 `PropertySource` 将这些数据封装成了键值对，如：`{"app.name": "myapp","server.port": "8080"}` 。

## PropertySource 源码

```java
public abstract class PropertySource<T> {
    // 配置源的名称
    protected final String name;
    
    // 配置源底层数据对象
    protected final T source;
    
    // 构造方法
    public PropertySource(String name,T source) {
        Assert.hashText(name,"Property source name must contain at least one character");
        Assert.notNull(source, "Property source must be not null");
        this.name = name;
        this.source = source;
    }
    
    // 获取配置源名称
    public String getName() {
        return this.name;
    }
    
    // 获取底层对象
    public T getSource() {
        return this.source;
    }
    
    // 判断是否包含指定键（默认返回 true）
    public boolean containsProperty(String name) {
        return true;
    }
    
    // 获取指定键的值（抽象方法，由子类实现具体逻辑）
	public abstract Object getProperty(String name);
    
   // 重写的 hashCode() 和 equals()
}
```

## 如何获取 PropertySource 对象

在第一节 *Environment 抽象* 中开发者学习过，`Environment` 接口实际是通过 `PropertyResolver` 接口实现的属性解析工作，开发者要想获取 `PropertySource` 对象需要通过 `Environment` 接口来获取，具体代码如下：

```java
ApplicationContext ctx = new GenericApplicationCOntext();
Environment env = ctx.getEnvironment();
boolean containMyProperty = env.containProperty("my-property");
System.out.println("我的环境是否含有 my-property 属性：" + containMyProperty);
```

## 默认的配置环境

Spring 会为开发者自动配置默认的环境，在第一节 *Environment 抽象* 中也有讲述。

如：`StandardEnvironment` 、`StandardServletEnvironment` 默认包含一部分配置源的。

下面分开讲述。

### StandardEnvironment

`StandardEnvironment` 是 `Environment` 的子接口，其使用于非 Web 环境，默认包含以下两个配置源：

- JVM 系统属性（`System.getProperites()`，该方法可以获取，如：`-Duser.name=xxx` 启动参数）
- 操作系统环境变量（`System.getenv()`，该方法可以获取，如：Path变量）

### StandardServletEnvironment

`StandardServletEnvironment` 是 `StandardEnvironment` 的子接口，其使用于 Web 环境，在 `StandardEnvironment` 的基础上，其还默认包含了：

- `ServletConfig` 参数（`web.xml` 中 `<servlet>` 的 `<init-param>`）

- `ServletContext` 参数（`web.xml` 中 `<context-param>`）

- JNDI 环境变量（`java:comp/env/` 下的配置）

## 配置源的优先级

前面说到 `PropertySource` 是通过键值对也就是 `Map` 集合来存储配置源信息的，不同的配置源一般其对应的键名是不一样的，如果配置源的名称一样了，**优先级高的会优先返回**。

下面是配置源的优先级（从高到低）：

1. `ServletConfig` 参数
2. `ServletContext` 参数
3. JNDI 环境变量
4. JVM 系统属性
5. 操作系统属性

假设开发者在 JVM 和 操作系统的环境变量中都定义了 `my-property` 属性，那么会优先返回 JVM 中名为 `my-property` 的属性。

## 自定义配置源

开发者可以自定义配置源的优先级、集合，在自定义配置源之前，先为开发者介绍一下 `PropertySource` 的子接口 `EnumerablePropertySource` 。

还有 `PropertySources` ，以及其实现类 `MutablePropertySources` 。

### EnumerablePropertySource

`EnumerablePropertySource` 接口集成了 `PropertySource` ，在 `PropertySource` 基础上 `EnumerablePropertySource` 能更好的知道自身拥有那些键，源码如下：

```java
public interface EnumerablePropertySource<T> extends PropertySource {
    
    String[] getPropertyNames();
    
}
```

### PropertySources

`PropertySources` 多个配置源接口，用于管理一组配置源的集合，不同于 `PropertySource` ，`PropertySource` 是单个配置源的管理。

源码如下：

```java
public interface PropertySources extends Iterator<PropertySource<?>> {
    
    // 判断 PropertySources 中是否包含对应名称的 PropertySource
    boolean contain(String name);
    
    // 根据名称获取单一的配置源实例
    PropertySource<?> get(String name);
    
    // 返回所有包含 PropertySource 的迭代器
    @Overrid
    Iterator<PropertySource<?>> iterator();
}
```

### MutablePropertySources

`MutablePropertySources` 用于管理配置源（`PropertySource`）的核心类，实现了 `PropertySources` 接口，提供了对配置源集合动态操作能力。

源码如下：

```java
public class MutablePropertySources implements PropertySources {
    
	private final List<PropertySource<?>> propertySources = new CopyOnwriteArrayList();
    
    // 构造方法
    public MutablePropertySources() {
        this.propertySources = new CopyOnWriteArrayList();
    }
    
   	public MutablePropertySources(PropertySources propertySources) {
        // 调用无参
        this();
        
        // 将参数 PropertySources 中的 PropertySource 一一添加到 private final List<PropertySource<?>> propertySources 中
        Iterator var2 = propertySources.iterator();
        
        while(var2.hasNext()) {
            PropertySource<?> propertySource = (PropertySource) var2.hasNext();
            this.addLast(propertySource);
        }
        
    }
    
    // 实现 PropertySources 接口的方法
    @Override
    public boolean contains(String name) {
        ...
    }
    
    @Override
    public PropertySource<?> get(String name) {
        ...
    }
    
    // 除此之外，MutablePropertySources 还额外提供了丰富的增删改用于操作 PropertySources 集合
    // 这里并没有书写详细的代码，如果开发者详细看了源码，会发现每一个方法都使用了 synchroized 关键字，这是为了保证线程的同步，准确来讲是为了保证数据实更新
    public void addFirst(PropertySource<?> propertySource) {
        ...
    }
    
    public void addLast(PropertySource<?> propertySource) {
        ...
    }
    
    public void addBefore(String relativePropertySource, PropertySource<?> propertySource) {
        ...
    }
    
    public void addAfter(String relativePropertySource, PropertySource<?> propertySource) {
        ...
    }
    
    public void replace(String relativePropertySource, PropertySource<?> propertySource) {
        ...
    }
    
    public void remove(String name) {
        ...
    }
    
    // 当然还有一些其他方法，但对于开发者来说以上的代码足够使用了，剩下的方法不在书写
}
```

### 自定义配置源具体写法

在开发时，开发者自定义的配置源需要通过继承 `EnumerablePropertySource`，然后再将其添加到 `PropertySource` 中。

具体代码如下：

假设开发者需要一个 JSON 配置文件中的数据，开发者应进行如下操作：

首先，开发者需要一个 `JSONFile` 的类，准确的来说需要一个 `JSONFilePropertySource` 类，用于读取配置文件信息，如下：

```java
public class JSONFilePropertySource extends EnumerablePropertySource{
    
    // 开发者定义了该数据源，目的是为了更见方便且对于整个系统的 Environment 具有作用
    // 既然是为了方便获取数据，开发者可以将数据存放至 Map 集合中，如同 Spring 原生的 PropertySource 那样做
    // 这里的泛型建议使用 <String, T>，为什么作者没有用呢？因为作者懒
    // 注意 fianl ，这是为了保证数据的安全性，不可被修改，因为要修改的话 JSON 文件中的数据也要随之改变，这需要 IO 操作，与其使用代码让文件数据改变，不如让代码随着数据变化
    private final Map<String, Object> properties;
    
    // 构造方法，不可或缺的
    public JSONFilePropertySource(String name, Resource resource) {
        super(name, resource);
        
        // 判断文件是否存在
        Assert.isTrue(resource.exists(), resource +"文件资源不存在");
        
        // Assert 是 Spring 的工具类
        // notNull() 是校验文件资源内容是否为空，如果为空会抛出异常 IllegaArgumentException，并显示消息文件内容为空
        Assert.notNull(resource, "文件内容为空");
    }
    
    // 解析 JSON 文件
    private void loadProperties() {
        try {
            // 这里主要是通过 Jackson 库来解析 JSON 文件数据
            // ObjectMapperJackson 核心工具类
            ObjectMapper om = new ObjectMapper();
            
            // 存储解析的数据
            // 遇见了泛型，开发者一定要考虑类型擦除后，代码是否可以正常运行
            // TypeReference 是 Jackson 用来保存泛型的工具类
            // 注意 Java 中没有 Map<String, Object>.class 的写法，经过类型擦除机制后，只有 Map.class
            Map<String, Object> tempProperites = om.readValue(getSource().getInputStream(), new TypeReference<Map<String, Object>>)() {});
            
            properites.putAll(tempProperites);
        }
        catch(IOException e) {
            throw new RuntimeException("文件解析异常：" + e);
        }
    }
    
    // 检测是否包含键
    @Override
    public boolean containsProperity(String name) {
        return properites.containsKey(name);
    }
    
    
    // 根据键获取值
    @Override
    public Object getProperty(String name) {
        return properites.get(name);
    }
    
    // 获取所有键
    @Override
    public String[] getAllProperityKey() {
        return properites.keySet().toArray(new String[0]);
    }
    
}
```

然后开发者需要使用获取属性源管理对象，这在 *Environment 抽象* 一节中的 `ConfigurableEnvironment` 接口中讲述过，可以通过 `ConfigurableEnvironment` 接口获取属性源管理对象 `MutablePropertySources` ，通过 `MutablePropertySources` 对象将 `JSONFileProperites` 添加到 `Environment` 中。

```java
ApplicationContext applicationContet = new AnnontationConfigApplicationContext();
MutablePropertySources sources= application.getEnvironment().getPropertySources();
sources.addFirst(new JSONFilePropertySources());
```

