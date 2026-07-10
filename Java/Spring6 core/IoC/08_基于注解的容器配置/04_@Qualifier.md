# @Qualifier

经过上一节的学习，`@Primary` 并不能完美解决精确注入的问题，为此 Spring 提供了注解 `@Qualifier` 。

`@Qualifier` 可以通过限制符将特定得到参数依赖联系起来，缩小匹配范围，从而为每个依赖提供特定的 Bean 。

具体示例代码：

```java
public class MovieRecommender {
    @Autowired
    @Qualifier("main") // 限定符值为"main"
    private MovieCatalog movieCatalog;
}
```

注解不仅仅可以使用在字段上，还可以是方法，参数列表等等，下面就开始详细介绍。

## @Qualifier 的使用

### 字段

```java
public class MovieRecommender {
    @Autowired
    @Qualifier("main") // 限定符值为"main"
    private MovieCatalog movieCatalog;
}
```

### 普通方法

```java
public class MovieRecommender {
    private final MovieCatalog movieCatalog;
    
    @Autowired
    @Qualifier("main") // 限定符值为"main"
    public void prepare(MovieCatalog movieCatalog) {
        this.movieCatalog = movieCatalog;
    }
}
```

### 方法参数

```java
public class MovieRecommender {
    private final MovieCatalog movieCatalog;
    
    @Autowired
    public void prepare(@Qualifier("main") MovieCatalog movieCatalog) { // 限定符值为"main"
        this.movieCatalog = movieCatalog;
    }
}
```

### 泛型

`@Qualifier` 对于泛型的使用，后面单独一节讲述。

### XML 文件的配置

紧接上文代码，定义完了 `@Qualifier` ，那怎么进行注入呢？

**这里是注解和 XML 的混合搭配使用，也可以纯注解，但是纯注解需要用到 `@Component` ，这个注解后面会讲述到。纯 XML 的下文也会讲述（其实大家已经学习过了）**。

需要引用 `<qualifier/>` 去作为 `<bean/>` 的子元素，XML 文件具体代码如下：

```xml
<beans>
    <!-- 这里需要注意，如果 XML 和注解同时使用需要开启注解识别，也就是引入annotation-config -->
    <context:annotation-config/>
    
    <!-- 限定符值为"main"的Bean -->
    <bean class="example.SimpleMovieCatalog">
        <qualifier value="main"/> 
    </bean>
    
    <!-- 限定符值为"action"的Bean -->
    <bean class="example.SimpleMovieCatalog">
        <qualifier value="action"/> 
    </bean>
</beans>
```

在 XML 文件中 `<bean class="example.SimpleMovieCatalog">` 被 `<qulifier/>` 赋予了限定值，一共两个 Bean ，一个限定值为 `main` ，一个限定值为 `action` 。

当 Spring 进行自动注入的时候，限定值匹配成功的会进行依赖注入，否则不会去进行注入。

## Bean 默认的限定值

开发者可以不用显式声明 `<qualifier/>` 标签，**Spring 规定了 Bean 默认的限定符值是其 Bean 的id值**。

如下：

若 Bean 定义为 `<bean id="main" class="example.SimpleMovieCatalog"/>`，则其默认限定符值为 `main` ，可与 `@Qualifier("main")` 匹配。

但是请开发者牢记 Bean 的id值并不等于限定符，**限定符核心概念是缩小匹配范围，这也就相当于一个过滤器，将限定值无法匹成功的 Bean 过滤掉**。

## 自定义 Qualifier

Spring 允许开发者可以自定义自己的 `qualifier` 注解，但在自定义的时候需要使用 Spring 中原有的 `@Qualifier` 。

如下：

```java
@Target({ElementType.FIELD, ElementType.PARAMETER}) // 元注解
@Retention(RetentionPolicy.RUNTIME) // 元注解
@Qualifier
public @interface Genre {

    String value();
}
```

假设开发者有以下代码：

```java
public class MovieRecommender {

    @Autowired
    @Genre("Action")
    private MovieCatalog actionCatalog;

    private MovieCatalog comedyCatalog;

    @Autowired
    public void setComedyCatalog(@Genre("Comedy") MovieCatalog comedyCatalog) {
        this.comedyCatalog = comedyCatalog;
    }

    // ...
}
```

那么开发者就需要在 XML 中明确声明自己所需要使用的 `qualifier` 具体的类，通过属性 `<qulifier type="">` 来声明，如下：

```xml
<!-- 这个一定不要忘记 -->
<context:annotation-config/>

<bean class="example.SimpleMovieCatalog">
    <qualifier type="Genre" value="Action"/>
</bean>

<bean class="example.SimpleMovieCatalog">
    <!-- 和上面的自定义 Genre 注解是同一个，但是限定符不同 -->
    <qualifier type="example.Genre" value="Comedy"/>
</bean>

<bean id="movieRecommender" class="example.MovieRecommender"/>

```

### 无值注解

开发者同样可以定义无值的 `@Qualifier` ，如下：

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Offline {
}
```

无值注解通常用于表示组件的状态，具有某一特定功能或属性，开发者可以自定义无值注解用来一表示其特有的状态。

### 多值注解

开发者可以在注解类中定义字段属性，来限制注解的匹配范围，如下：

```java
public enum Format {
    VHS, DVD, BLURAY
}
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface MovieQualifier {

    String genre();

    Format format();
}
```

使用如下：

```java
public class MovieRecommender {

    @Autowired
    @MovieQualifier(format=Format.VHS, genre="Action")
    private MovieCatalog actionVhsCatalog;

    @Autowired
    @MovieQualifier(format=Format.VHS, genre="Comedy")
    private MovieCatalog comedyVhsCatalog;

    @Autowired
    @MovieQualifier(format=Format.DVD, genre="Action")
    private MovieCatalog actionDvdCatalog;

    @Autowired
    @MovieQualifier(format=Format.BLURAY, genre="Comedy")
    private MovieCatalog comedyBluRayCatalog;

    // ...
}
```

同样的开发者需要在 XML 文件中对其进行注入，如下：

```xml
<context:annotation-config/>

<!-- 
在以下 bean 的注入中，开发者会发现前两个 bean 使用的是<qualifier>,但是在后面两个 bean 使用的是<meta>,下面就讲述以下二者的区别：
<qualifier>：bean 专门的限定符修饰标签
<meta>：bean 通用元数据（在实际开发中一般做后备数据使用）修饰标签

区别：<qualifier> 的优先级要高于 <meta>
二者的作用都是让 Spring 能够精确找到所要注入的依赖
-->
<bean class="example.SimpleMovieCatalog">
    <qualifier type="MovieQualifier">
        <attribute key="format" value="VHS"/>
        <attribute key="genre" value="Action"/>
    </qualifier>
</bean>

<bean class="example.SimpleMovieCatalog">
    <qualifier type="MovieQualifier">
        <attribute key="format" value="VHS"/>
        <attribute key="genre" value="Comedy"/>
    </qualifier>
</bean>

<bean class="example.SimpleMovieCatalog">
    <meta key="format" value="DVD"/>
    <meta key="genre" value="Action"/>
</bean>

<bean class="example.SimpleMovieCatalog">
    <meta key="format" value="BLURAY"/>
    <meta key="genre" value="Comedy"/>
</bean>
```

## 注意事项

1. 无论是 `@Primary` ，还是 `@Qualifier` ，大家都需要牢记二者都只是缩小匹配范围，本质上是不能够去进行自动注入的，需要搭配 `@Autowired` 使用。既然是需要搭配 `@Autowired` 去使用那也就说明，其底层原理还是通过类型去匹配的 Bean。
2. 注意和其他注解的搭配使用，详细内容后面会讲述到。
3. 不要在 XML 文中试图定义两个（id不同）含有同样 `<qualifier>或<meta>` Bean 进行相互注入，那是很愚蠢的。这很有可能导致两个 Bean 之间循环依赖。

## 纯 XML 文件的 @Qualifier

其实纯 XML 文件是没有必要使用限定符的，因为开发者完全可以使用 `<ref>或者ref=""` 来进行 Bean 的注入，哪怕是自动注入其实也是多余的。

以上注解和 XML 的混合搭配使用是为了更好的方便开发者阅读代码。

在实际开发中通常还是会以纯注解或纯 XML 进行开发，除非需要必须用到混合使用，一般不使用，因为这会降低代码可读性。