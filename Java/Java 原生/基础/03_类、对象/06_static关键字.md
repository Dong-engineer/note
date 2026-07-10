# Static

`static` 静态的，用于修饰类的成员（字段、方法、内部类）或代码块。

`static` 的核心作用是**将类的成员与类本身绑定，而非与类的实例绑定**。这意味着：

- **静态成员（字段、方法）**：可以直接通过类名访问，无需创建类的实例；
- **静态代码块**：在类加载时执行一次，用于初始化静态资源；
- **静态内部类**：不依赖外部类的实例，可以直接创建。

**特点：**

- 属于类，所有实例共享同一个静态字段
- 可通过 `ClassName.fieldName/functionName()` 直接访问
- 类加载时初始化，生命周期与类相同
- 只能访问静态成员（静态字段、静态方法），不能访问实例成员（因无需实例）
- 主类加载时执行一次，用于初始化静态资源（如静态字段）

## 1.静态成员

```java
public class Student {
    public static int totalStudents = 0; // 静态字段，记录学生总数
    private String name;

    public Student(String name) {
        this.name = name;
        totalStudents++; // 每次创建实例时，静态字段自增
    }
}

// 使用
System.out.println(Student.totalStudents); // 0（无需创建实例）
new Student("Alice");
System.out.println(Student.totalStudents); // 1
```

`totalStudents` 被静态修饰，所以`totalStudents`该属性不属于任何一个`Student`类的实例，它只属于`Student` 类本身。所以直接用`类名.静态成员名` 访问即可。

## 2. 静态方法（类方法）

```java
public class Calculator {
    public static int add(int a, int b) {
        return a + b;
    }
}

// 使用
int result = Calculator.add(3, 5); // 直接通过类名调用
```

- 常见场景：
  - 工具类方法（如 `Math.random()`、`Arrays.sort()`）；
  - 工厂方法（如 `Integer.valueOf()`）。

## 3.静态代码块

```java
public class Config {
    public static final String API_URL;
    public static final int MAX_CONNECTIONS;

    static { // 静态代码块
        // 从配置文件或环境变量加载值
        API_URL = System.getProperty("api.url", "https://api.example.com");
        MAX_CONNECTIONS = 10;
        System.out.println("Config initialized.");
    }
}
```

## 4. **静态内部类**

下一章节会讲述

## 5.补充（一定要看）

上面讲述到，`static` 关键字**修饰的变量和函数在类加载时加载**，那么类什么时候加载呢？

类的加载过程是**按需加载**（Lazy Loading）的，即**当且仅当需要使用某个类时，JVM 才会加载该类**。

#### 1. **创建类的实例**

- 当使用`new`关键字实例化对象时，如：

  ```java
  MyClass obj = new MyClass(); // 触发MyClass的加载
  ```

#### 2. **访问类的静态成员**

- 访问类的静态字段（`static field`）或调用静态方法时：

  ```java
  System.out.println(MyClass.staticField); // 触发MyClass的加载
  MyClass.staticMethod(); // 触发MyClass的加载
  ```

#### 3. **反射调用**

- 使用反射 API 时：

  ```
  Class.forName("com.example.MyClass")
  ```

  ```java
  Class<?> clazz = Class.forName("com.example.MyClass"); // 触发MyClass的加载
  ```

#### 4. **子类加载时**

- 如果子类被加载，其父类会先被加载：

  ```java
  public class Child extends Parent {} // 加载Child时，Parent会先被加载
  ```

#### 5. **程序启动时**

- 包含`main`方法的主类（程序入口点）会在启动时立即被加载：

  ```java
  public static void main(String[] args) { ... } // 主类在程序启动时加载
  ```