# Throwable及其子类

## Throwable

`Throwable` 类可以说是所有异常类的父类， `Throwable` 的父类是 `Object` ，如下图所示：

![](.\img\Throwable.jpeg)

如图所示可以得知， `Error、Exception` 是 `Throwable` 的直接子类，`RuntimeException` 是 `Exception` 的直接子类。

## Error

`Error` 表示错误类，当 Java 虚拟机中发生动态链接故障或其他硬故障时，虚拟机会引发 `Error`。简单的程序通常不会捕获或引发 `Error` 的实例。

## Exception

大多数程序 throw 和 catch 派生自 `Exception` 类的对象。`Exception` 表示发生了问题，但这不是严重的系统问题。您编写的大多数程序都会抛出和捕获 `Exception` 的实例，而不是 `Error`。

Java 平台定义了 `Exception` 类的许多后代。这些后代指示可能发生的各种类型的异常。例如，`IllegalAccessException`表示找不到特定方法，而 `NegativeArraySizeException` 表示程序尝试创建具有负大小的数组。

一个 `Exception` 子类 `RuntimeException` 保留用于指示 API 使用不正确的异常。运行时异常的一个示例是 `NullPointerException`，当方法尝试通过 null 引用访问对象的成员时，会发生此异常。

## 链式异常

在实际开发中，方法之间的调用时层层递进的，对于最底层的方法来说一般的做法就是将异常捕获后向上抛出，让其最顶层的方法来决定怎么处理异常。
![](.\img\链式异常.png)

这就需要用到链式异常，如下：

```java
try {

} catch (IOException e) {
    // SampleException -> 开发者自定义的类或者其他异常类
    throw new SampleException("Other IOException", e);
}
```

将 `IOException e` 作为参数传递个新的异常向上抛出。

那么什么不直接处理异常呢？这里后面讲。

## 访问堆栈跟踪异常信息

在 `Exception` 类中，Java 为开发者提供了访问堆栈的功能。

`getStackTrace()` 获取栈内所有元素，并返回 `StackTraceElement` 类型数组。

其类型内数组每一个元素就是栈的一帧。

具体使用如下：

```java
catch (Exception cause) {
    StackTraceElement elements[] = cause.getStackTrace();
    for (int i = 0, n = elements.length; i < n; i++) {       
        System.err.println(elements[i].getFileName()
            + ":" + elements[i].getLineNumber() 
            + ">> "
            + elements[i].getMethodName() + "()");
    }
}
```

```plaintext
MyClass.java:42>> methodB()
MyClass.java:25>> methodA()
Main.java:10>> main()
```

### e.printStackTrace()

Java 也提供了默认的跟踪堆栈的方法。

`printStackTrace()` 会输出更详细的信息，包括：

- 异常类名和错误消息
- 完整的包名和类名
- `Caused by:` 前缀标识的原始异常（链式异常）

在链式异常中说到方法的异常通常向上抛出，让最顶层的方法决定怎么处理异常，在最顶层方法中，一般就是 `e.printStackTrace()` ，如果开发者想用其他方式处理异常则需另外书写代码。

同时上面那段代码也解释为什么不直接处理异常，因为频繁跟踪堆栈会很大的影响程序性能

除非生产需求有严格的要求去手动跟踪堆栈，一般都使用 `printStackTrace()`。

**性能对比**：

- **`printStackTrace()`**：内部直接调用 `Writer` 输出，性能开销较小，但频繁调用仍可能影响性能。
- **手动遍历**：需要创建数组、循环处理每个元素，若自定义格式化复杂（如字符串拼接），开销可能略高。

**手动跟踪适应场景**：

- **自定义日志格式**：例如按 JSON 格式输出堆栈信息到 ELK 等日志系统。

```json
{
  "exception": "ArithmeticException",
  "message": "/ by zero",
  "stackTrace": [
    { "file": "Main.java", "line": 10, "method": "divide" },
    { "file": "Main.java", "line": 5, "method": "main" }
  ]
}
```

- **信息脱敏**：过滤类名中的敏感业务路径（如 `com.company.private`）。
- **集成监控系统**：将堆栈信息转换为特定协议发送到监控平台。
- **性能优化**：按需获取部分堆栈信息（如只取前 N 帧）。