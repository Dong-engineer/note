# Lambda表达式

Java 8 引入的 Lambda 表达式主要用于简化**函数式接口**（只有一个抽象方法的接口）的实现。

#### **基本语法**

```java
(参数列表) -> { 方法体 }
```

#### **示例**

```java
// 函数式接口
interface Greeting {
    void sayHello(String name);
}

// 使用匿名类实现
Greeting greeting1 = new Greeting() {
    @Override
    public void sayHello(String name) {
        System.out.println("Hello, " + name);
    }
};

// 等价的lambda表达式
Greeting greeting2 = (name) -> System.out.println("Hello, " + name);

// 使用
greeting1.sayHello("Alice"); // 输出: Hello, Alice
greeting2.sayHello("Bob");   // 输出: Hello, Bob
```

#### **常见用途**

- **集合操作**（如`forEach`, `stream`）：

  ```java
  List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
  names.forEach(name -> System.out.println("Hello, " + name));
  ```

- **线程创建**：

  ```java
  new Thread(() -> {
      System.out.println("Running in a thread");
  }).start();
  ```