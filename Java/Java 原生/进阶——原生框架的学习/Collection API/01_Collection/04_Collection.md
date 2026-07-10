# Collection

`Collection` 集合框架的根接口，定义了处理一组对象的通用方法，它是 `List`、`Set`、`Queue` 等接口的父接口，为各种集合提供了统一操作规范。

## 源码

```java
public interface Collection<E> extends Iterable<E> {
    // 基本操作
    int size();                  // 返回集合元素数量
    boolean isEmpty();           // 判断集合是否为空
    boolean contains(Object o);  // 判断是否包含指定元素
    boolean add(E e);            // 添加元素（成功返回true）
    boolean remove(Object o);    // 移除指定元素（成功返回true）
    boolean containsAll(Collection<?> c);  // 判断是否包含指定集合的所有元素
    boolean addAll(Collection<? extends E> c);  // 添加指定集合的所有元素
    boolean removeAll(Collection<?> c);    // 移除包含在指定集合中的所有元素
    boolean retainAll(Collection<?> c);    // 仅保留包含在指定集合中的元素（交集）
    void clear();                // 清空集合
    Object[] toArray();          // 转换为Object数组
    <T> T[] toArray(T[] a);      // 转换为指定类型的数组

    // 迭代器（继承自Iterable）
    Iterator<E> iterator();

    // JDK 8+ 新增的默认方法
    default boolean removeIf(Predicate<? super E> filter) { ... }  // 批量删除满足条件的元素
    default Spliterator<E> spliterator() { ... }                  // 可分割迭代器（并行处理）
    default Stream<E> stream() { ... }                            // 获取顺序流
    default Stream<E> parallelStream() { ... }                    // 获取并行流
}
```

在源码中，开发者不难发现，泛型被广泛使用，这在泛型的章节中，已经提到过，这是为了保证类型下转型不会出现错误。

`Collection` 还继承了 `Iterable` 接口，这也就表明了其可以通过增强 for 循环和迭代器来进行元素遍历。

`Collection` 除了一些基本操作外，在 Java8+ 后还增加了一些新功能，如：`removeIf()`、`spliterator()`、`stream()`、`parallelStream()`。

当然还有 `hashCode()`、`toString()`、`equals()` ，而且这些方法是已经被重写过的。

## 使用示例

`Collection` 是一个接口，其不是类，如果开发者需要创建 `Collection` 实例，则需要通过其子类（实现类）来完成创建。

下面是一个示例。

```java
public class CollectionTest{
    public static void main(String[] args) {
        
        // 创建实例
        Collection<String> fruits = new ArrayList<>();
        
        // 添加数据
        fruits.add("苹果");
        fruits.add("香蕉");
        fruits.add("梨");
        
        // 输出
        System.out.println(fruits);
        
        // 基本操作
        System.out.println(fruits.size()); // 3
        System.out.println(fruits.isEmpty()); // false
        System.out.println(fruits.contains("猕猴桃")); // false
        
    }
}
```

```cmd
[苹果, 香蕉, 梨]
3
false
false
```

通过输出结果，开发者不难发现，对于 `Collection` 的实例输出不再是哈希地址，而是实打实的元素内容，这是因为上面代码创建的实例是 `ArrayList` ，而 Java 官方在 `Collection` 的源码中写了 `hashCode()`、`toString()`、`equals()` ，因为具体的实现类有所差异，并没有在 `Collection` 中给出这些方法的默认实现行为，而是在不同的实现类中重写了这些方法。

**增删改查后面会具体到实现类讲述**（这里标记下，因为元素的增删改查在实际开发中很有可能涉及到同步问题，也就是并发程序，导致集合遍历出现问题，这一问题放到后面的具体实现类里讲述，望开发者耐心学习），这里先讲述下 `removeIf()` ，在上面的 `removeIf()` 直接说是批量删除满足条件的元素，Java 称其为 **Filtering out Elements of a Collection with a Predicate** （翻译过来叫做：使用谓词过滤集合元素），还是批量删除满足条件元素好理解。

### `removeIf()`

`removeIf()` 源码，如下：

```java
boolean removeIf(Predicate<? super E> filter)
```

#### 谓词 `Predicate`

主要看参数，对于 `Predicate` 很多开发者很可能并不了解，源码如下：

```java
@FunctionalInterface
public interface Predicate<T> {
    
    // Predicate 的唯一方法
    boolean test(T t);
    
    // 源码还有一些，此处并未写出，有疑问开发者可自行搜索
   
}
```

`Predicate` => 过滤器、条件判断器。

其过滤的功能就是由 `test()` 来实现的，下面讲述使用。

#### 使用示例

```java
Predicate<String> isNull = Objects::isNull;
Predicate<String> isEmpty = String::isEmpty;
Predicate<String> isNullOrEmpty = isNull.or(isEmpty);

Collection<String> strings = new ArrayList<>();
strings.add(null);
strings.add("");
strings.add("one");
strings.add("two");
strings.add("");
strings.add("three");
strings.add(null);

IO.println("strings = " + strings);
strings.removeIf(isNullOrEmpty);
IO.println("filtered strings = " + strings);
```

```cmd
strings = [null, , one, two, , three, null]
filtered strings = [one, two, three]
```

在这里，又遇见一种新的写法 `Predicate<String> isNull = Objects::isNull` ，这叫做**方法引入**。

方法引入其实可以理解为另一种 `Lambda` 表达式，阅读性很差（如果开发者可以看懂另说）。

其等价于 `isNull -> Objects.isNull(Object Object)` 。

其本质就是将 `Objects.isNull(Object object)` 的具体实现代码赋给谓词 `Predicate isNull` 的 `test()` 。

而示例所给代码 `Preidcate<String> isNlull` 表示过滤不为 `null` 字符串。

创建完成谓词后，开发者直接将谓词示例传入 `removeIf()` 函数即可。

## 元素的遍历

首先是增强型 for 循环，又称 forEach 循环，如下：

```java
for (String fruit : fruits) {
    System.out.print(fruit);
}
```

```cmd
苹果
香蕉
梨
```

其次是迭代器遍历元素，如下：

```java
// 获取迭代器
// 这里要么加泛型，要么使用 instanceof 运算，要规避下转型错误
Iterator<String> iterator = fruits.iterator();
while(iterator.hasNext()) {
	
    // 这里开发者这注意下，在之前的内容并没有讲述 Iterator（第一版，后面修改了），这里作者书写代码时发生了错误 NoSuchElementException，这是因为作者调用了两次 iterator.next() ，导致指针在一次循环中移动了两次。
    // System.out.println(iterator.next());
	
    // 取出元素，使用临时变量存储
    String fruit = iterator.next();
    System.out.println(fruit);
    
}
```

```cmd
苹果
香蕉
梨
```

## 注意事项

1. `Collection` 是接口，不能直接实例化，需要借助实现类
2. 尽量使用增强型 for 循环，如果使用迭代器来遍历元素，不能使用 `Collection` 接口的 `remove()` ，开发者应当使用迭代器的 `remove()` 来删除元素，不然会报错 `ConcurrentModificationException` 。
3. 避免空指针异常，即存入元素要经过检验保证不是 `null` 。
4. 线程安全，`Collection` 大多数的实现类是非线程安全的，开发者需要注意，线程安全的集合，后面会讲述。
5. `hashCode()`、`equals()`等方法在自定义类中，一定要重写，不然在后面 `Set` 集合中的学习会有大麻烦（后面会详细讲述）。

