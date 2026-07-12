# Record

## 引入

Java提供了很多种创建不可变类的方法。可能最直接的方法是创建一个带有 `final` 字段的 `final`类和一个构造函数来初始化这些字段。官方文档中所给的 `Point`类并没有加上`final`关键字。

如下：

```java
public final class Point {
    
    private final int x;
    private final int y;

}
```

在类的学习里面，我们知道了所有的类都隐式继承`Object`，`Point`亦是如此，故而开发者在书写`Point`类时很可能就要重写`toString()、equals()、hashCode()`。

这也就意味者，开发者很有可能一个不可改变的类就要写很长的代码，虽然很多代码可以使用IDE直接生成，但是，在实际开发中根据业务需求的更新，代码也会随之进行改动，这就需要修改很多的代码，修改这一过程本就是不被允许的，因为很容易产生BUG。

**不可修改：这里再详细补充一下，不可修改并不意味开发人员不能进行修改，不可修改” 的核心是指 对象创建后，其内部状态（字段值）无法被修改**。

## record

Java SE 14提供了`record`关键字使得一个类被声明为记录，记录就包含不可修改的特性。

如下：

```java
public record Point(int x, int y) {}
```

术语：`Point`不再称为类， 叫做**记录**，而它后面所跟的参数叫做**组件**（等同于类的字段、成员变量）

这一行代码，可以帮开服人员干很多事情，如下：

1. `Point`是一个不可变的类，且它具有两个int类型的组件`x、y`
2.  `Point`有一个规范的构造函数，用于初始化这两个字段
3. 编译器已为您创建了 `toString()、equals()`和 `hashCode（）` 方法，其默认行为与 IDE 生成的行为相对应。除非有必要，开发人员不用自己书写了，编译时会自己产生，而且会随着开发者修改 `toString()、equals()、hashCode()`也动态变化。
4. 它可以实现 `Serializable`接口，以便您可以通过网络或文件系统将 的实例发送到其他应用程序。记录的序列化和反序列化方式遵循本教程末尾介绍的一些特殊规则。



## java.lang.Record

**所有使用`record`声明的记录，都隐式继承`java.lang.Record`，所以开发者的记录不能扩展任何类。但是可以实现任意记录的接口。**



## 声明记录组件

上面知道了，组件在记录名称后的`()`中，以参数的形式声明。记录不可被修改但是可以访问，其组件是`private`的，所以需要访问器（get()）来访问。

Java规定了，**访问器与组件同名**，所以不能再像书写正常类那样书写访问器了。

但开发者可以自己书写访问器，在需要返回特定字段的防御副本的情况下，它可能很有用。

如下：

```java
public int x() {
    return this.x;
}

public int y() {
    return this.y;
}
```

**但是这些工作`record`帮开发者做了，所以无需再去书写了**。

## 在记录中禁止做的事

有三项内容不能添加到记录中：

1. 不能在记录中声明任何实例字段。您不能添加任何与组件不对应的实例字段。
2. 不能定义任何字段初始值设定项。
3. 不能添加任何实例初始值设定项。

可以使用初始值设定项和静态初始值设定项创建静态字段。

如下：

#### 不能在记录中声明任何实例字

```java
// 正确：所有字段在头部声明
public record Person(String name, int age) {}

// 错误：不能在类体中声明额外的实例字段
public record Person(String name, int age) {
    private String address; // 错误！
}
```

#### 不能添加任何与组件不对应的实例字段

```java
// 错误：类体中不能定义与头部参数无关的字段
public record Point(int x, int y) {
    private int z; // 错误！z未在头部声明
}
```

#### 不能定义任何字段初始值设定项

```java
// 错误：不能为记录的组件指定初始值
public record Rectangle(int width, int height = 10) {} // 错误！
```

#### **不能添加任何实例初始值设定项**

```java
// 错误：记录中不能使用实例初始化块
public record User(String username) {
    {
        // 实例初始化块，错误！
    }
}
```

#### 可以使用静态初始值设定项创建静态字段

```java
public record Circle(double radius) {
    // 静态字段和静态初始化块是允许的
    public static final double PI;
    
    static {
        PI = Math.PI;
    }
}
```

#### 为什么有这些限制？

记录的设计目标是**简洁地表示不可变数据**，因此：

- 所有状态由构造参数决定，避免数据不一致。
- 自动生成访问器、构造方法、`equals()`、`hashCode()` 等方法，减少样板代码。
- 静态成员不属于实例状态，因此不受限制。

## 使用其规范构造函数构造 Record

上面讲述了`record`声明的类的限制，知道了，不能定义任何字段初始值设定项，那么该怎么进行对这些字段的初始化呢？

Java提供两种构造器来对记录的组件进行初始化。

1. **Canonical constructor** => 非紧凑得到规范构造函数
2.  **Compact 构造函数** => 紧凑的规范构造函数

**在默认开发者不主动书写构造器得到情况下，编译器会创建一个称为 *canonical constructor* 的构造函数**

### Compact 构造函数

代码如下：

```java
public record Range(int start, int end) {

    public Range {
        if (end <= start) {
            throw new IllegalArgumentException("End cannot be lesser than start");
        }
    }
}
```

紧凑的规范构造函数不需要声明其参数块。如果选择此语法，则无法直接分配记录的字段。例如，在需要给字段设置值时，通常是这样的 `this.start = start`，但在记录中这些操作都是由编译器来提开发者完成的。

**这也就意味着如果使用了紧凑的构造函数，开发人员没法给该记录直接赋值的，因为这些任务是编译完成的，但是开发人员可以给组件（也就是记录头部声明的参数）直接赋值，这种操作等同于 `this.start = start`，编译器会根据开发人员的操作去对对应的字段赋值。**

如下：

```java
// 紧凑的构造函数
public Range {
    
    if (start < 0)
        start = 0; // this.start = start
    if (end < 0)
        end = 0;
}
```

### Canonical constructor

非紧凑的构造函数，代码如下：

```java
public record Range(int start, int end) {

    public Range(int start, int end) {
        if (end <= start) {
            throw new IllegalArgumentException("End cannot be lesser than start");
        }
        if (start < 0) {
            this.start = 0;
        } else {
            this.start = start;
        }
        if (end > 100) {
            this.end = 10;
        } else {
            this.end = end;
        }
    }
}
```

在这种情况下，您编写的构造函数需要为记录的字段分配值。

**如果 record 的组件不是不可变的，则应考虑在 canonical 构造函数和 accessors 中制作它们的防御性副本。**

### 应用

在实际开发中。

**紧凑的规范构造函数常用于参数验证，保证组件（字段）不为null，避免空指针**

**非紧凑的规范构造函数常用于需要重新分配组件（字段），又或者需要初始化逻辑复杂的组件（字段）**

## 定义任何构造函数

**定义任何构造函数的基础，必须调用其非紧凑构造函数！！！**

以下代码，并未声明非紧构造函数，因为之前就已经讲述，编译器会默认生成一个非紧凑规范的构造函数。

```java
public record State(String name, String capitalCity, List<String> cities) {

    // 非紧凑函数
    public State {
        // 如果 record 的组件不是不可变的，则应考虑在 canonical 构造函数和 accessors 中制作它们的防御性副本
        // 这里就是制作了一个其副本
        // 这里一定要用List.copyOf(),因为List.copyOf()会创建一个不可改变的集合对象,详细介绍后面会有的
        cities = List.copyOf(cities);
    }
    // 构造函数1，这里没有传cities城市集合
    public State(String name, String capitalCity) {
        // 这里就通过this关键字调用了非紧凑规范构造方法
        this(name, capitalCity, List.of());
    }
    // 构造函数2，这里使用了`...`表示可以接受任意数量和cities类型一样的参数
    public State(String name, String capitalCity, String... cities) {
        this(name, capitalCity, List.of(cities));
    }

}
```

#### 关于任意参数 `...` 符号

```java
// 正确示例
public void validMethod(int count, String... names) { /* ... */ }

// 错误示例，会引发编译错误
public void invalidMethod(String... names, int count) { /* ... */ }
```

```java
public void process(String[] data) { /* ... */ }
public void process(String... data) { /* ... */ } // 编译错误：会导致歧义
```

优点：`...`符号使得接受的参数更加灵活，不一定非需要接受一个数组等等。

缺点：书写规范要求严格，且当参数中有使用`...`时，该函数接受的参数是可以为`null`的，很有可能会空指针。

**注意：在一个记录中非紧凑函数和紧凑函数是不能同时存在的，因为二者都是规范构造器。但是后面在*定义任何构造函数*中重载了构造函数，这是因为后面构造的函数参数不一样，相当于方法重载**

## 获取记录的组件

在声明记录组件一节中，已经讲述了，每一个组件都有自己的访问器，而且编译器会自动帮助开发者生成。

但是在某些情况下开发者需要定义自己的访问器，比如：当一个记录中，有一个组件是`int`类型的`arr`数组，代码如下：

```java
public record MyRecord(int[] arr) {
    // 编译器自动生成的方法（等效代码）
    public int[] arr() {
        return this.arr; // 直接返回数组引用
    }
}
```

请记住这里返回了一个数组，当开发者在外部访问该数组的时候，也就意味该数组可以被修改，如下

```java
public static void main(String[] args) {
    int[] original = {1, 2, 3};
    MyRecord record = new MyRecord(original);

    // 通过访问器获取数组并修改
    int[] arrCopy = record.arr();
    arrCopy[0] = 999;

    System.out.println(record.arr()[0]); // 输出：999，记录内容被修改
}
```

这样做就破坏了`record`的核心原则——不可修改，所以这时候就不能使用编译器默认生成的访问器了，在上一节，已经做过示范了，当开发者需要访问该组件时，应当对其进行防御副本，或者只能访问其防御副本，代码如下：

```java
public record MyRecord(int[] arr) {
    // 手动重写访问器，返回副本
    public int[] arr() {
        return List.copyOf(arr); // 返回数组副本，防止外部修改
    }
}
```

## 序列化记录

如果您的记录类实现 `Serializable`，则可以对记录进行序列化和反序列化。不过，也有限制。

1. 可用于替换默认序列化流程的任何系统都不可用于记录。创建 `writeObject（）` 和 [`readObject（）` 方法没有任何效果，实现 `Externalizable` 也没有效果。
2. 记录可以用作代理对象来序列化其他对象。`readResolve（）` 方法可以返回一条记录。也可以在记录中添加 `writeReplace（）。`
3. 反序列化记录*始终*调用规范构造函数。因此，您可以在此构造函数中添加的所有验证规则都将在反序列化记录时强制执行。

这使得 records 成为在应用程序中创建数据传输对象的非常好的选择。

## 实例应用

此节内容和代码均来自Java官方文档，一定要看。

记录是一个通用的概念，您可以在许多上下文中使用。

第一种方法是在应用程序的对象模型中携带数据。您可以将记录用于其设计目的：充当不可变的数据载体。

由于您可以声明本地记录，因此还可以使用它们来提高代码的可读性。

让我们考虑以下用例。您有两个建模为记录的实体：`City`和`State` 。

```java
public record City(String name, State state) {}
public record State(String name) {}
```

假设您有一个城市列表，并且需要计算城市数量最多的州。您可以使用 Stream API 首先构建各州的直方图，其中包含每个州拥有的城市数量。此直方图由 `Map` 建模。

```java
List<City> cities = List.of();

Map<State, Long> numberOfCitiesPerState =
    cities.stream()
          .collect(Collectors.groupingBy(
                   City::state, Collectors.counting()
          ));
```

获取此直方图的最大值是以下通用代码。

```java
Map.Entry<State, Long> stateWithTheMostCities =
    numberOfCitiesPerState.entrySet().stream()
                          .max(Map.Entry.comparingByValue())
                          .orElseThrow();
```

最后一段代码是技术性的;它不具有任何商业含义;，因为它使用 `Map.Entry` 实例对直方图的每个元素进行建模。

使用本地记录可以大大改善这种情况。以下代码创建一个新的记录类，该类聚合一个州和该州的城市数。它有一个构造函数，该构造函数将 `Map.Entry` 的实例作为参数，用于将键值对流映射到记录流。

因为你需要按城市数量来比较这些聚合，所以你可以添加一个工厂方法来提供这个比较器。代码将变为以下内容。

```java
record NumberOfCitiesPerState(State state, long numberOfCities) {

    public NumberOfCitiesPerState(Map.Entry<State, Long> entry) {
        this(entry.getKey(), entry.getValue());
    }

    public static Comparator<NumberOfCitiesPerState> comparingByNumberOfCities() {
        return Comparator.comparing(NumberOfCitiesPerState::numberOfCities);
    }
}

NumberOfCitiesPerState stateWithTheMostCities =
    numberOfCitiesPerState.entrySet().stream()
                          .map(NumberOfCitiesPerState::new)
                          .max(NumberOfCitiesPerState.comparingByNumberOfCities())
                          .orElseThrow();
```

您的代码现在以有意义的方式提取最大值。您的代码更具可读性、更易于理解、不易出错，从长远来看，更易于维护。
