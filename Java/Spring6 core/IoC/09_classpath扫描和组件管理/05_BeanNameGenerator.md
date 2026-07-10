# BeanNameGenerator

`BeanNameGenerator` 作为 Spring 的核心接口之一，在基于注解的情况下，其可以为 Bean 生成唯一的 id 名称。

当开发者使用注解为 Bean 进行命名时，如：@Component(name="userService")、@Service(name="userService")、@Controller(name="userController") 等。

名称命名这一工作的底层原理实际是由 `BeanNameGenrator` 完成的。

## BeanNameGenerator 的源码

源码如下：

```java
public interface BeanNameGenerator {
    // 为指定的Bean定义生成唯一名称
    String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry);
}
```

Spring 就是通过该接口的实现的注解为 Bean 命名的策略，在实际开发中可能开发者会需要自己的命名策略。

那么开发者就可以自定义 `BeanNameGenerator` 。

## 自定义 BeanNameGenerator

### 默认策略

`BeanNameGenerator` 的默认命名策略，即之前在 *@Component* 一节提过的如果在注解时含有 `name` 属性，那么 Bean 的名称会以 `name` 属性的值为准，如果在注解时没有 `name` 属性（或者说默认没有生命任何字符串），那么 Bean 的名称就是其类型的首字母小写。

而这种命名的策略实际上是由两个 `BeanNameGenerator` 的命名完成的，Spring 中提供了三种 `BeanNameGenerator` 的实现，如下：

| 实现类                                      | 命名规则                                                     | 适用场景                                                     |
| ------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `DefaultBeanNameGenerator`                  | 全类名首字母小写（如 `com.example.UserService` → `userService`）； 若类名前两个字母均为大写，则保持原类名（如 `URLService` → `URLService`）。 | 组件扫描（`@ComponentScan`）时的默认生成器，适用于大多数自动检测的 Bean。 |
| `AnnotationBeanNameGenerator`               | 优先使用 `@Component` 等注解的 `value` 属性指定名称（如 `@Component("myUser")` → `myUser`）； 若未指定，则 fallback 到 `DefaultBeanNameGenerator` 的规则。 | 处理带注解的组件（`@Component`、`@Service` 等），是 `@ComponentScan` 的默认生成器（实际继承自此类）。 |
| `FullyQualifiedAnnotationBeanNameGenerator` | 直接使用**全类名**作为 Bean 名称（如 `com.example.UserService` → `com.example.UserService`）。 | 避免类名冲突（如不同包下有同名类时），可通过 `@ComponentScan(nameGenerator = ...)` 手动指定。 |

### 自定义策略

自定义策略需要开发者实现 `BeanNameGenerator` 接口，并重写 `generateBeanName` 方法。

示例如下：

```java
public class CustomBeanNameGenerator implements BeanNameGenerator {
    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        // 获取类名（如 com.example.service.UserService → UserService）
        String className = ClassUtils.getShortName(definition.getBeanClassName());
        // 自定义规则：添加模块前缀 + 首字母小写
        return "module_" + Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}
```

## 使用自定义策略

### 基于注解

使用 `@ComponentScan` 的属性即可。

```java
@Configuration
@ComponentScan(
    basePackages = "com.example",
    nameGenerator = CustomBeanNameGenerator.class // 使用自定义生成器
)
public class AppConfig {
    
}
```

### 基于 XML

使用 `<context:component scan>` 开启组件扫描，然后在其标签里面使用 `nameGenerator` 属性即可，如下：

```xml
<context:component-scan base-package="com.example" 
                        name-generator="com.example.config.CustomBeanNameGenerator"/>
```

## 注意事项

1. 必须保证 Bean 的名称在容器中是唯一的存在，不然 Spring 会抛出 `BeanDefinitionStoreException` 异常
2. 注意和注解  `@Bean` 的配合使用，Spring 会优先使用 `@Bean` 的 `name` 属性名称
3. 在使用自定义 `BeanNameGenerator` 时尽量避免复杂的计算和 IO 操作，这会影响性能
4. 对于 XML 使用 `<bean>` 标签的 `id` 属性，其为 Bean 命名的过程并不会经过 `BeanNameGenerator` ，其本质是基于 XML 解析器完成的
5.  `BeanNameGenerator` 的作用范围仅局限于**基于注解进行的组件扫描**，这意味着其不会影响 `@Bean` 或者 `BeanDefinitionRegistry`
