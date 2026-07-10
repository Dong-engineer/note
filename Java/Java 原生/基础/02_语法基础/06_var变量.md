# var变量

java SE 10版本后，可以使用 `var` 来动态声明变量，让编译器决定您创建的变量的实际类型是什么。创建后，此类型无法更改。

如下：

```java
// 正常声明变量
String message = "Hello world!";
Path path = Path.of("debug.log");
InputStream stream = Files.newInputStream(path);

// var动态声明变量，声明后不可以修改变量的数据类型
var message = "Hello world!";
var path = Path.of("debug.log");
var stream = Files.newInputStream(path);

```

## 使用 var 的限制

类型标识符的使用存在限制。

1. 您只能将其用于方法、构造函数和初始化器块中声明的*局部变量*。
2. `var`不能用于字段，也不能用于方法或构造函数参数。
3. 编译器必须能够在声明变量时选择类型。由于 has no type，因此变量必须具有初始化器。

遵循这些限制后，以下类不会编译，因为字段或方法参数不能用作类型标识符。



在这种情况下，编译器无法猜测 的真实类型，因为缺少初始值设定项。所以这段代码也不编译器。

```java
public String greetings(int message) {
    var greetings; // 编译错误
    if (message == 0) {
        greetings = "morning";
    } else {
        greetings = "afternoon";
    }
    return "Good " + greetings;
}
```

