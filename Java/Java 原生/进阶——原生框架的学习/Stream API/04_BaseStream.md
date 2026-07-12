# BaseStream

`BaseStream` 是整个 `Stream API` 顶层父接口，下面可以通过其源码了解流的基础行为。

## 源码

```java
/**
 * 所有流的顶层基础接口，定义流通用行为，实现 AutoCloseable 支持 try-with-resources 自动关闭流
 * @param <T> 流中存储的元素类型
 * @param <S> 流自身的实现类型，用于链式调用返回当前流对象
 */
public interface BaseStream<T, S extends BaseStream<T, S>>
        extends AutoCloseable {

    /**
     * 获取当前流的迭代器，用于传统 for 循环遍历流元素
     * @return 包含流所有元素的 Iterator 迭代器
     */
    Iterator<T> iterator();

    /**
     * 获取分流迭代器，并行流底层分片遍历依赖 Spliterator 实现分片迭代
     * @return 当前流的 Spliterator 分流器
     */
    Spliterator<T> spliterator();

    /**
     * 判断当前流是否为并行流
     * @return true：并行流 | false：串行流
     */
    boolean isParallel();

    /**
     * 将当前流转为串行流，链式调用返回自身
     * @return 转换后的串行流
     */
    S sequential();

    /**
     * 将当前流转为并行流，底层使用 ForkJoinPool 执行分片计算，链式调用返回自身
     * @return 转换后的并行流
     */
    S parallel();

    /**
     * 标记当前流为无序流，去除遍历、聚合时的顺序约束，并行场景可提升性能
     * @return 无序标记后的流
     */
    S unordered();

    /**
     * 注册流关闭时的回调处理器，多个 handler 会按注册顺序依次执行
     * @param closeHandler 流关闭时执行的任务
     * @return 注册完关闭处理器后的流
     */
    S onClose(Runnable closeHandler);

    /**
     * 关闭流，执行所有通过 onClose 注册的回调任务，释放流占用的资源
     * 实现 AutoCloseable 接口，支持 try-with-resources 语法自动调用关闭
     */
    @Override
    void close();
}
```

`BaseStream extends AutoCloseable` 可知所有的流对象都是可以自动关闭的，其对应的关闭接口为 `close()` 。同时 `BaseStream` 因为继承了 `AutoCloseable`，流支持 `try-with-resources` 语法，代码块执行完毕后会自动调用 `close()` 释放文件、IO 等流绑定的资源，无需手动调用关闭方法。

`BaseStream` 具有一个 `iterator()` 接口，这也说明开发者可以通过 while 或 for 循环语句对流对象进行迭代操作。

`spliterator()` 该接口会返回一个 `Spliterator<T>` 分流迭代器，该接口在 `Collection API` 中很常见。在并行处理的底层中，分流迭代器会将流分为多个子分片，配合不同线程进行并行处理。

以下方法为流的常用方法：

`isParallel()`：布尔判断当前流是并行流还是串行流；

`sequential()`：将流转为串行模式，返回流自身支持链式调用；

`parallel()`：将流转为并行模式，底层基于 ForkJoinPool 执行任务；

`unordered()`：标记流为无序流，丢弃原有元素顺序约束，并行计算时可减少排序开销、提升执行效率。

 