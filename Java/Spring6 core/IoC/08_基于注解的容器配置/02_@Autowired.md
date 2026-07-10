# @Autowired

`@Autowired` 自动装配，在前面已经学习过自动装配，同时也介绍过一点的注解自动装配。

自动装配开发者应该并不陌生，但注解的自动装配，可能会些疑问？

那么下面从以下几点讲述，同时还会和 XML 的自动装配进行对比。

- 基于注解的自动装配是什么
- 基于注解的自动装配怎么用

## 基于注解的自动装配是什么

基于注解的自动装配，其实就是自动注入，也就是 XML 在 <bean/> 标签中使用的 `autowire` 属性。

在 XML 文件中 `autowire` 属性有不同的值，不同的值决定了自动注入是根据什么注入的。在 XML 中开发者知道 `byType` => 根据属性自动注入，`byName` => 根据名称自动注入，`byType` 没有 `byName` 更加灵活，而 `byName` 开发者知道是根据类的 `setter` 方法进行自动注入的。

**注解中的自动注入优先使用 `byType` 进行自动注入，如果检测到多个类型相同的属性，会变为根据 `byName` 自动注入。但是注解并不是根据 `setter` 进行注入的**

## 基于注解的自动装配怎么用

首先说明一点（在XML的自动注入中忘说了），自动注入不仅仅局限于属性字段、普通方法、构造方法、参数等都可以进行自动注入，这个在前面的学习讲述过，如下为XML的普通方法自动注入：

```xml
<!-- 默认自动根据类型进行装配，不再显示声明 autowire 了 -->
<bean id="userService" class="com.example.UserService">
    <!-- 假设 userService 有一个方法 initDao(UserDao dao) -->
    <property name="initDao" ref="userDao"/>
</bean>
```

在容器初始化完成后，`getBean(userService)` 获取对象后，会自动完成参数的注入然后调用 `intitDao()` 方法。

### 普通方法

同样的基于注解，开发者就需要在被自动注装配的方法上加上 `@Autowired` 注解声明为自动装配。

如下：

```java
public class UserService {
    
    private UserDao userDao; // 属性字段

    // 对普通方法进行自动注入，默认根据类型
    @Autowired
    public void initDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
```

### 参数注入

只针对某个参数进行自动注入

```java
public class UserService {
    
    private UserDao userDao; // 属性字段

    public void initDao(@Autowired UserDao userDao) {
        this.userDao = userDao;
    }

}
```

### 字段注入

对于字段注入注解和 XML 就不一样了。

如下：

```java
public class UserService {
    
    // 对单个字段进行注入
    @Autowired
    private UserDao userDao;
}
```

**无需显式的声明 `setter` 方法，Spring 会为每个字段单独查找并注入对应的 Bean**。

如果在注入时需要详细精确到注入的 Bean 是容器中的哪一个 Bean，那么需要使用其他注解和 `@Autowired` 搭配使用。

当然开发者如果习惯了 `setter` 方法，也可声明 `setter` 方法，在 `setter` 上声明 `@Autowired` 完成自动注入。

如下：

```java
public class UserService {

    private UserDao userDao;
    
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
```

### 构造函数

如果开发者的 Bean 一开始就只有一个 Bean，那么就不再需要声明 `@Autowired` ，只有当 Bean 含有多个构造方法且需要自动注入的时候可以使用 `@Autowired` ，**至少有一个构造函数必须用 `@Autowired` 注解，以便指示容器使用哪一个**，但是有限制条件，详情请开发者继续往下阅读至 *required = false* 。

```java
public class UserService {

    private UserDao userDao;
    
    public UserService() {
        
    }
    
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
```

### 数组注入

从这里开发者需要注意，下面的注入需要注意一些细节问题。

数组注入时，需要注意以下几点：

1. 确保注入的元素和数组类型一致
2. **如果开发者的数组是一个父类类型（包括抽象类）或者接口类型，那么 Spring 会自动将其子类或实现类注入到该数组中**，这是为了遵循里氏替换原则
3. 若容器中没有匹配类型的 Bean ，那么 Spring 会默认注入一个长度为0的空数组（注意不是`null` ，这是基于 `required=false` 的情况下，否则会报错）

```java
public class MovieRecommender {

    @Autowired
    private MovieCatalog[] movieCatalogs;

    // ...
}
```

### 泛型

Spring 注入泛型的原理：

首先，提到泛型，泛型的特点不要忘记，**类型擦除**机制会将参数化类型在编译后擦除，这也就意味着 Spring 是无法识别到参数化的类型是什么（Java 是知道的，不要忘了类型推断）。

Spring 通过类型保留机制，达到自动注入泛型的目的，即将参数化的类型单独保留出来，然后进行判断。

- 对于 `List<?>` 这种参数化类型不确定的泛型，Spring 无法通过类型保留机制来判断注入的类型，Spring 就默认注入容器中的 `Object` 类型的 Bean
- 对于 `List<Integer>` 这种参数化类型机制，Spring 通过**保留的泛型元数据**识别具体类型，才能正确匹配
- Spring 在注入参数化类型确定的逻辑和数组相同，即如果开发者的定义的参数化类型是一个父类或者接口，那么 Spring 在注入时会自动检测其所有子类或者实现类并将其注入到里面

```java
public class MovieRecommender {

    private Set<MovieCatalog> movieCatalogs;

    @Autowired
    public void setMovieCatalogs(Set<MovieCatalog> movieCatalogs) {
        this.movieCatalogs = movieCatalogs;
    }

    // ...
}
```

### 泛型集合

普通集合的注入不再讲述，实际开发中集合和泛型是密切且相互搭配使用的，这里直接讲述泛型集合的注入原理，这是和普通的注入逻辑不太一样的。

Spring 在对泛型集合进行注入时，先看 key ，无论是在 XML 还是注解，都要对 Bean 的 id/name 命名。而这些命名默认就是 `String` 类型，**如果开发者的集合 key 为 `String` 类型，如下：**

```java
public class MovieRecommender {

    private Map<String, MovieCatalog> movieCatalogs;

    @Autowired
    public void setMovieCatalogs(Map<String, MovieCatalog> movieCatalogs) {
        this.movieCatalogs = movieCatalogs;
    }

    // ...
}
```

**Spring 会检测所有容器中的 Bean（请开发者记住，当 key 为 `String` 时），然后再去判断容器中的 Bean 类型是否匹配，最后将类型匹配的 Bean 注入到其中，而 key 则是 Bean 的 id/name（优先id）**。

如果泛型集合 key 不为 `String` 类型，那么就需要开发者手动注入了（即通过 Java 原生 API 添加到集合中）。

## required=false

无论是什么注入，都必须保证至少有一个类型匹配的值，否则在进行自动注入时 Spring 会抛出异常（`NoSuchBeanDefinitionException`），为了避免这种情况， Spring 在注解 `@Autowired` 添加了一个属性，即 `required` 。

如果在自动注入时开发者想要跳过一个不可被满足的注入点（即无法注入成功），那么开发者可以使用 `@Autowired(required = false)` 来跳过本次注入（**前提条件是无法注入成功**）。

具体使用代码如下：

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Autowired(required = false)
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

在以上代码中， `setMovieFinder`方法被标记为`required = false`，意味着：

- 如果存在匹配的`MovieFinder`类型 Bean，正常注入（调用方法并赋值）。
- 如果没有匹配的 Bean，**该方法根本不会被调用**，字段`movieFinder`会保持默认值（如`null`）。

### 构造方法的 `required`

在构造方法中讲述到了 `required`，在实际开发中一个 Bean 很可能会有多个构造方法，前面说到了至少得有一个构造方法使用 `@Autowired` 才能进行自动注入，那么当多个构造方法被声明为构造方法呢？

正常情况下，多个构造方法被声明为  `@Autowired` ，那也就意味着每个方法都会去尝试自动注入且必须注入成功，这样是冗余的操作且存在抛出异常的可能。

如果有一个构造方法被声明为 `@Autowired` (即 `@Autowired(required = ture)`) ，那么其他的构造方法被声明为 `@Autowired` 是冗余的，且一旦 Spring 无法匹配成功，程序会抛出异常（`NoSuchBeanDefinitionException`）。

所以 **Spring 规定了：如果有一个构造方法被声明为 `@Autowired` ，那么其他构造方法无需再去声明为 `@Autowired` 。如果需要多个构造方法自动注入，那么可以使用 `@Autowired(required = false)` 在需要自动注入的构造方法上声明。 **

阅读完以上内容其实会有新的疑问，如果开发者使用了 `@Autowired(required = false)` 跳过自动注入，那么 Bean 的依赖值应该是什么呢？

### required=false 失败后，依赖的值

当自动注入没有匹配成功跳过后，依赖的值保持默认（即开发者自定义），如果开发者没有对依赖进行实例化，那么失败后，依赖的值为 `null`。

值为 `null` 的话很容易发生 `NullPointException` 。

Spring 为解决这个问题，提供了另一种方式来完成 `required=false` 一样的工作，但依赖的值不会为 `null`。

### `java.util.Optional`

在 Java 8 中，`java.util.Optional<T>` 是一个容器类，用于表示一个值可能存在或不存在（避免 `null` 引用）。它的主要目的是解决 `NullPointerException` 问题，让代码更清晰地表达 "值可能缺失" 的语义。

#### Optional 的核心思想

- 不再用 `null` 表示 "无值"，而是用 `Optional.empty()`
- 用 `Optional.of(value)` 或 `Optional.ofNullable(value)` 包装可能为 `null` 的值
- 通过明确的方法判断值是否存在，避免直接使用 `null` 判断

示例代码如下：

```java
@Autowired
private MovieFinder movieFinder; // 如果容器中没有 MovieFinder 类型的 Bean，启动报错

@Autowired
private Optional<MovieFinder> movieFinderOpt; // 容器中有无 MovieFinder 都不报错

// 使用时判断是否存在
if (movieFinderOpt.isPresent()) {
    MovieFinder finder = movieFinderOpt.get(); // 存在则获取
    // 业务逻辑
} else {
    // 处理依赖不存在的情况（如使用默认实现）
}
```

- `Optional` 仅对 `@Autowired` 注入的**类型匹配**有效，如果依赖类型存在但有多个候选 Bean（如同一接口的多个实现），仍可能抛出 `NoUniqueBeanDefinitionException`（需配合 `@Qualifier` 解决）。
- 不要过度使用这种方式，只有确实需要 "可选依赖" 时才用，否则会降低代码的明确性。

### `@Nullable`

自 Spring 5.0 后开发者可以使用注解 `@Nullable` 来达到和 `java.util.Optional` 一样的效果。

## 自动装配的顺序（`@Order()`）

一提到顺序，开发者应该还没有忘记 `Phased` 接口和 `Ordered` 接口。

同样的如果开发者想要决定依赖注入顺序那么可以实现 `Ordered` 接口，也可以通过注解 `@Order()` 来决定依赖注入的顺序。

```java
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// 第一个组件，排序值为1
@Component
@Order(1)
public class FirstComponent implements MyComponent {
    @Override
    public void doSomething() {
        System.out.println("First component working");
    }
}

// 第二个组件，排序值为2
@Component
@Order(2)
public class SecondComponent implements MyComponent {
    @Override
    public void doSomething() {
        System.out.println("Second component working");
    }
}

// 接口定义
public interface MyComponent {
    void doSomething();
}

// 注入使用
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {
    
    // 注入MyComponent类型的数组，会按@Order排序
    @Autowired
    private MyComponent[] components;
    
    public void process() {
        // 遍历数组时，会按FirstComponent -> SecondComponent的顺序执行
        for (MyComponent component : components) {
            component.doSomething();
        }
    }
}
```

## @Autowired 使用注意

到这里可能有开发者猜测 `@Autwired` 和 `BeanPostProcessor` 有关，不然为什么 `Ordered` 接口会决定其执行顺序呢？

没错 `@Autwired` 和 `BeanPostProcessor` 的确有关，在 *BeanPostProcessor* 一节中讲述过 ` AutowiredAnnotationBeanPostProcessor` 。

**所以 `@Autowired` 无法对 `BeanPostProcessor` 或 `BeanFactoryPostProcessor` 去进行自动注入，因为基于注解自动装配就是依靠 `BeanPostProcessor` 来完成的。同时这也就意味着同时期工作的 Bean 也不能进行自动装配。对于这些 Bean 的注入开发者必须手动在 XML 文件中声明，又或者使用 `@Bean` 来完成**。
