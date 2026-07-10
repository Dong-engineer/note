# Bean的生命周期

关于 Bean 的生命周期，开发者应该是有一定了解的，比如 Bean 是随着容器的初始化而初始化的，Bean的销毁也是随着容器的关闭而结束。

## Bean的主要生命周期

#### Bean 定义的加载与解析

- Spring 容器（如`ApplicationContext`）启动时，会扫描配置文件（`XML`）或注解（`@Component`、`@Service`等），将 Bean 的信息解析为`BeanDefinition`对象（存储 Bean 的类名、属性、依赖等元数据）。
- 此阶段不创建 Bean 实例，仅完成定义的注册。

#### Bean 实例的创建（实例化）

- 容器根据`BeanDefinition`的信息，通过反射调用 Bean 的**无参构造方法**（默认）或指定构造方法，创建 Bean 的实例（内存中的对象）。
- 若使用工厂方法（`factory-method`），则通过工厂类的方法创建实例。

#### 属性注入（依赖注入）

- 容器将配置的属性值（如`@Value`）或依赖的其他 Bean（如`@Autowired`）注入到当前 Bean 实例中。
- 注入方式包括**设值注入**（调用`setter`方法）和**构造器注入**（通过带参构造方法）。

#### BeanNameAware 接口回调

- 若 Bean 实现了`BeanNameAware`接口，容器会调用其`setBeanName(String name)`方法，传入当前 Bean 在容器中的 ID（名称）。

#### ApplicationContextAware 接口回调（仅适用于 ApplicationContext）

- 若 Bean 实现了`ApplicationContextAware`接口，容器会调用其`setApplicationContext(ApplicationContext applicationContext)`方法，传入`ApplicationContext`实例（功能比`BeanFactory`更丰富）。

#### BeanPostProcessor 前置处理（postProcessBeforeInitialization）

- 容器中的所有`BeanPostProcessor`接口实现类会被触发，调用其`postProcessBeforeInitialization(Object bean, String beanName)`方法，对 Bean 进行初始化前的增强处理（如 AOP 代理的准备）。
- 该方法可返回修改后的 Bean 实例（如替换为代理对象）。

#### 初始化方法调用

- 自定义初始化方法：若通过 `@PostConstruct` 注解、XML 的 `init-method` 属性或实现 `InitializingBean` 接口指定了初始化方法，容器会按以下顺序调用：
  1. `@PostConstruct`注解的方法（JSR-250 规范）；
  2. `InitializingBean`接口的`afterPropertiesSet()`方法；
  3. 自定义的`init-method`方法（XML 或`@Bean(initMethod = "...")`指定）。
- 初始化方法用于执行 Bean 的初始化逻辑（如连接数据库、加载配置等）。

#### BeanPostProcessor 后置处理（postProcessAfterInitialization）

- 再次触发`BeanPostProcessor`，调用其`postProcessAfterInitialization(Object bean, String beanName)`方法，对 Bean 进行初始化后的增强（如 AOP 动态代理的创建）。
- 若 Bean 需要被代理（如`@Transactional`注解的类），此阶段会生成代理对象并返回，后续使用的是代理实例。

#### **Lifecycle 启动回调（SmartLifecycle 自动启动）**

- 若 Bean 实现了`SmartLifecycle`接口且`isAutoStartup()`返回`true`，容器会在**上下文刷新完成后**（`ApplicationContext.refresh()`的最后阶段），通过`LifecycleProcessor`按`getPhase()`定义的顺序触发`start()`方法。
- 此阶段用于启动 Bean 的 “运行时逻辑”（如启动定时任务、开始消息监听等），晚于初始化阶段（初始化是 “准备”，启动是 “运行”）。

#### Bean 的使用

- 此时 Bean 已完全初始化并（若适用）启动，可被容器注入到其他 Bean 中，或通过`getBean()`方法获取并使用。

#### **Lifecycle 显式启动回调（普通 Lifecycle 或手动触发）**

- 若 Bean 仅实现普通`Lifecycle`接口（非`SmartLifecycle`），或`SmartLifecycle`的`isAutoStartup()`返回`false`，则`start()`方法需等待**显式调用**（如`ApplicationContext.start()`）才会触发，由`LifecycleProcessor`级联调用所有`Lifecycle` Bean 的`start()`。

#### 容器关闭与 Bean 销毁前的准备

- 当容器关闭（如 `ApplicationContext.close()` ）时，首先触发 `Lifecycle` 关闭回调：
  - 容器通过`LifecycleProcessor`的`onClose()`方法，按`phase`的逆序（与启动相反）调用所有`Lifecycle` Bean 的`stop()`方法（`SmartLifecycle`会调用`stop(Runnable callback)`，需在关闭完成后调用`callback.run()`以通知容器）。
  - 此阶段用于优雅停止运行时逻辑（如停止定时任务、断开消息连接等）。

#### Bean 销毁方法调用

- 容器在 `Lifecycle` 关闭回调后，触发 Bean 的销毁流程：
  - 自定义销毁方法：若通过 `@PreDestroy` 注解、XML 的 `destroy-method` 属性或实现 `DisposableBean` 接口指定了销毁方法，容器会按以下顺序调用：
    1. `@PreDestroy`注解的方法（JSR-250 规范）；
    2. `DisposableBean`接口的`destroy()`方法；
    3. 自定义的`destroy-method`方法。
  - 销毁方法用于释放资源（如关闭数据库连接、释放文件句柄等）。

## 讲述什么

Bean 的生命周期并不会完全讲完，有的阶段如果开发者认真学习了前面的知识，有些内容是不用去学习的，如：Bean 定义的加载与解析、Bean 实例的创建（实例化）、属性注入（依赖注入）。本章节，主要讲述除了这三以外的知识。

但是整个章节的流程并不会按着上面的生命周期顺序来讲述，这是因为对照 Spring 官方文档，其次也是为了开发者更好的去理解 Bean 的生命周期。