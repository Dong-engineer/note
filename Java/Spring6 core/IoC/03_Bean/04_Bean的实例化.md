# Bean 的实例化

基于XML文件配置 Bean，有、无参构造方法共两种。

**有参构造方法：**

使用 `<constructor-arg>` 标签，调用有参构造方法。

**无参构造方法：**

使用 `<property name="" ></property>` 标签，在其里面配置数据，标识调用 setter 方法去注入。

但是，除了依赖注入外，在开发时，部分Bean 需要实现任何特定的接口，需要以特定的方式进行编码，等等。就是说需要一些特殊的操作。我们就不能在使用构造方法来实例化 Bean 了。

两种情况：

- 通常，在容器本身通过**反射**式地调用构造函数直接创建 Bean 的情况下，指定要构造的 Bean 类。
- 在不太常见的情况下，即容器在一个类上调用 `static` 工厂方法来创建 Bean 时，要指定包含被调用的 `static` 工厂方法的实际类。从 `static` 工厂方法的调用中返回的对象类型可能是同一个类或完全是另一个类。

接下来一一介绍：

- 通过构造函数实例化 Bean
- 通过静态工厂实例化 Bean
- 通过实例工厂实例化 Bean

## 一、通过构造函数实例化 Bean

通过构造函数实例化 Bean ，下面通过一个测试示例来了解：

```java
package org.dong.Beans;

/**
 * @ClassName User
 * @Description User 类型
 * @Author Dong
 * @Date 2026/2/14 12:47
 * @Version 1.0
 */
public class User {

    private String name;
    private int age;
    private String address;

    public User() {
        System.out.println("无参构造执行");
    }

    public User(String name, int age, String address) {
        System.out.println("有参构造执行");
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("我是 setter 方法，我被执行了");
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        System.out.println("我是 setter 方法，我被执行了");
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        System.out.println("我是 setter 方法，我被执行了");
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }
}
```

XML 配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--  默认执行无参构造方法  -->
    <bean id="user" class="org.dong.Beans.User"/>
    <!-- 执行 setter 进行注入 -->
    <bean id="user-1" class="org.dong.Beans.User">
        <!-- 这里会条用 setter 方法 -->
        <property name="name" value="Dong"/>
        <property name="age" value="18"/>
        <property name="address" value="China"/>
    </bean>

    <!-- 开发者如果需要指定使用有参构造则需要使用 constructor-arg -->
    <bean id="user-2" class="org.dong.Beans.User">
        <constructor-arg name="name" value="JQK"/>
        <constructor-arg name="age" value="19"/>
        <constructor-arg name="address" value="BeiJing"/>
    </bean>
</beans>
```

启动类：

```java
public class Study01 {
    public static void main(String[] args) {

        // 容器初始化时所有的 Bean 都会被 Spring 完成加工，而开发者只需要通过 getBean() 来获取即可
        // 这就是 Spring 极致的工厂思想的体现
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("Study01.xml");

        User user = applicationContext.getBean("user", User.class);
        User user1 = applicationContext.getBean("user-1", User.class);
        User user2 = applicationContext.getBean("user-2", User.class);

        System.out.println(user);
        System.out.println(user1);
        System.out.println(user2);
    }
}
```

## 二、通过静态工厂实例化 Bean

在定义一个用静态工厂方法创建的 Bean 时，使用 `class` 属性来指定包含 `static` 工厂方法的类，并使用名为 `factory-method` 的属性来指定工厂方法本身的名称。你应该能够调用这个方法并返回一个活的对象，随后该对象被视为通过构造函数创建的。这种 Bean 定义的一个用途是在遗留代码中调用 `static` 工厂。

下面的 Bean 定义规定，Bean将通过调用工厂方法来创建。该定义并没有指定返回对象的类型（class），而是指定了包含工厂方法的类。在这个例子中，`createInstance()` 方法必须是一个 `static` 方法。下面的例子显示了如何指定一个工厂方法。

```xml
<!-- class属性指定了包含工厂方法的类 -->
<!-- factory-method属性指定了类包含的工厂方法 -->
<!-- 这里我们使用了class属性，并没有将class属性预留出来，所以bean的类型在运行时是可以确定的，是一个ClientService类 -->
<bean id="clientService" class="examples.ClientService" factory-method="createInstance"/>
```

```java
public class ClientService {
    
    // 静态成员变量，用于下面工厂方法返回clientService对象
    private static ClientService clientService = new ClientService();
    
    private ClientService() {}
    
	// 静态工厂方法
    public static ClientService createInstance() {
        // 返回clientService对象
        return clientService;
    }
}
```

## 三、用实例工厂实例化 Bean

与 通过静态工厂方法进行的实例化类似，用实例工厂方法进行的实例化从容器中调用现有 Bean 的非静态方法来创建一个新的 Bean。要使用这种机制，请将 `class` 属性留空，并在 `factory-bean` 属性中指定当前（或父代或祖代）容器中的一个 Bean 的名称，该容器包含要被调用来创建对象的实例方法。用 `factory-method` 属性设置工厂方法本身的名称。下面的例子显示了如何配置这样一个Bean。

```xml
<!-- 父类 DefaultServiceLocator -->
<bean id="serviceLocator" class="examples.DefaultServiceLocator"></bean>

<!-- 因为我们需要用实例工厂方法，所以不可以定义 class 属性，因为最终该 Bean 在运行时的类型是不确定的，我们只需要使用 factory-bean 属性来确定其父类即可 -->
<bean id="clientService" factory-bean="serviceLocator" factory-method="createClientServiceInstance"/>
```

```java
public class DefaultServiceLocator {

    // 静态成员变量，用于下面工厂方法返回对象
    private static ClientService clientService = new ClientServiceImpl();

    // 这里我们无需在使用静态工厂方法，如果用了，会在static工厂中遗留代码
    public ClientService createClientServiceInstance() {
        return clientService;
    }
}
```

> 在Spring文档中，“factory bean” 是指在Spring容器中配置的Bean，它通过 实例 或   静态 工厂方法创建对象。相比之下，`FactoryBean`（注意大写字母）是指Spring特定的`FactoryBean` 实现类。

## 四、确定Bean运行时的类型

以上内容仔细阅读后，其实我们可以发现，在用实例工厂实列化 Bean 时是无法确定 Bean 在运行时最终的类型。因为 Bean 的 factory-method 所指定的方法返回的对象是动态的。

除此之外，要确定一个特定 Bean 的运行时类型是不容易的。在 Bean 元数据定义中指定的类只是一个初始的类引用，可能与已声明的工厂方法相结合，或者是一个 `FactoryBean` 类，这可能导致Bean的运行时类型不同，或者在实例级工厂方法的情况下根本没有被设置（而是通过指定的 `factory-bean` 名称来解决）。此外，AOP代理可能会用基于接口的代理来包装 Bean 实例，对目标 Bean 的实际类型（只是其实现的接口）的暴露有限。

要了解某个特定 Bean 的实际运行时类型，推荐的方法是对指定的Bean名称进行 `BeanFactory.getType` 调用。这将考虑到上述所有情况，并返回 `BeanFactory.getBean` 调用将为同一 Bean 名称返回的对象类型。

