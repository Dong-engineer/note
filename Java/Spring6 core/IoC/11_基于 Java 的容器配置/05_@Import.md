# @Import

在 XML 的学习中，开发者知道当一个文件 A 中的 Bean 依赖了文件 B 的 Bean ，那么开发者需要使用 `<import/>` 引入外部文件。

同样的在基于注解的开发中，如果 `AppConfig` 中的 Bean 依赖了另一 `AppConfig` 中 Bean 就需要使用 `@Import`。

## @Import 的使用

示例代码如下：

```java
@Configuration
public class ConfigA {

    @Bean
    public A a() {
        return new A();
    }
}

@Configuration
@Import(ConfigA.class)
public class ConfigB {

    @Bean
    public B b() {
        return new B();
    }
}
```

### @Import 和 @Autowired 的配合使用

当 `AppConfig` 类中的 Bean 依赖了其他 `AppConfig` 的 Bean，对于 XML 来说，开发者可以直接使用 `ref=""` 引入外部 Bean。

基于注解，开发者可以使用 `@Autowired` 或者 `@Resource` 进行自动注入。如果使用了自动注入的注解，开发者需要在容器一一注册每一个 `AppConfig` 。

而 `@Import` 可以省去注册的操作，只需要在 `AppConfig` 引入其他 `AppConfig` 即可，Spring 会自动帮助开发者将其里面的 Bean 注入到容器中。

示例代码如下：

```java
@Configuration
public class ServiceConfig {

    @Bean
    public TransferService transferService(AccountRepository accountRepository) {
        return new TransferServiceImpl(accountRepository);
    }
}

@Configuration
public class RepositoryConfig {

    @Bean
    public AccountRepository accountRepository(DataSource dataSource) {
        return new JdbcAccountRepository(dataSource);
    }
}

@Configuration
@Import({ServiceConfig.class, RepositoryConfig.class})
public class SystemTestConfig {

    @Bean
    public DataSource dataSource() {
    }
}

public static void main(String[] args) {
    
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SystemTestConfig.class);
    
    TransferService transferService = ctx.getBean(TransferService.class);
    transferService.transfer(100.00, "A123", "C456");
    
}
```

