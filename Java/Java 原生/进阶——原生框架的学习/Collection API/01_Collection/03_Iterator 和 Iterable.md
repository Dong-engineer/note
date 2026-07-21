# Iterator

`Iterator` 迭代器，这里开发者要注意区分 `Iterable` 和 `Iterator` 两者的区别。

`Iterator` 是一个工具类——迭代器，开发者可使用改工具类对可迭代对象进行遍历。

## 源码

```java
public interface Iterator<E> {
    /**
     * 判断集合中是否还有下一个元素。
     * @return 如果还有元素可以迭代，则返回 true
     */
    boolean hasNext();

    /**
     * 返回集合中的下一个元素，并将迭代器的指针向后移动。
     * @return 集合中的下一个元素
     * @throws NoSuchElementException 如果没有更多元素
     */
    E next();

    /**
     * 移除迭代器最后一次返回的元素。
     * 此方法只能在每次调用 next() 之后调用一次。
     * @throws IllegalStateException 如果 next() 方法尚未被调用，或者 remove() 方法在上一次调用 next() 之后已经被调用过
     * @throws UnsupportedOperationException 如果迭代器不支持 remove 操作
     */
    default void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
```

## 注意事项

1. `Iterator` 并不属于 Java 集合框架，但其作用对集合框架非常重要。
2. `Iterator` 中的 `next()` 必须基于其 `hasNext()` 来调用，简单来说即优先使用 `hasNext()` 判断是否含有下一元素，然后再去使用 `next()`，否则很容易触发 `NoSunchException`。
3. 注意 `next()` 的使用，避免连续多次使用该方法，导致指针出现错误或发生 `NoSunchException` ，如果是开发需求需要，则注意指针位置即可。

# Iterable

`Iterable` 是可迭代接口，如果某个类或接口实现了 `Iterable` 那么改类或接口是必定可以进行迭代、遍历操作的。

## 源码

```java
public interface Iterable<T> {
    /**
     * 获取迭代器
     */
    Iterator<T> iterator();
	
    /**
     * 对该Iterable中的每一个元素执行给定操作，直至所有元素处理完毕，或该操作抛出异常为止。
     * 若迭代存在指定顺序，则操作会按迭代顺序执行。操作抛出的异常会直接传递给调用方。
	 * 若该操作产生副作用、修改了底层元素源集合，则本方法的行为未定义；除非重写该方法的子类规定了并发修改策略。
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }
    
    /**
 	 * 获取当前Iterable对应的分割迭代器Spliterator，用于流式并行遍历
 	 * 默认实现基于集合普通迭代器Iterator构建，不明确元素总量与大小特征
 	 * @return 一个未知元素数量的Spliterator分割迭代器
 	 */
	default Spliterator<T> spliterator() {
    	return Spliterators.spliteratorUnknownSize(iterator(), 0);
	}
}
```