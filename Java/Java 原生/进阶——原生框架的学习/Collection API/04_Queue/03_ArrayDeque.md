# ArrayDeque

`ArrayDeque` 可扩容动态队列。

在上节开发者已经见识过 `ArrayDeque` 的使用，本节着重学习其源码。

在学习之前，提醒开发者，`ArrayDeque` 的底层内容非常多，但也很值得借鉴学习，希望开发者耐心学习。

## 源码

```java
public class ArrayDeque<E> extends AbstractCollection<E>
    implements Deque<E>, Cloneable, Serializable
{
    // 底层由数组实现，这里的数组是循环数组，开发者要牢记
    transient Object[] elements;
    // 头指针
    transient int head;
	// 尾指针
    transient int tail;
	// 队列最大的安全容量
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	// 无参构造方法
    public ArrayDeque() {
        elements = new Object[16 + 1];
    }
	/**
	* 有参构造方法
	* param:
	* 		numElements: 队列的容量值
	*/
    public ArrayDeque(int numElements) {
        elements =
            new Object[(numElements < 1) ? 1 :
                       (numElements == Integer.MAX_VALUE) ? Integer.MAX_VALUE :
                       numElements + 1];
    }
	/**
	* 有参构造方法
	* param:
	* 		c: 其他类型的集合
	*/
    public ArrayDeque(Collection<? extends E> c) {
        this(c.size());
        copyElements(c);
    }
    /***** 静态方法 *****/
    // 静态方法很关键，是 ArrayDeque 用于增删改查的基础，且也很适合开发者去借鉴，在实际开发中如何去优化算法
    
    /**
    * 在源码的注释中，Java 官方说明了该方法是取模运算，但很明显，在方法体中并没有明显的取模运算。
    * 该方法本质是通过取模运算以此来判断 ArrayDeque 中的元素是否超出其长度，但是 ArrayDeque 是可扩容动态队列（开发者要牢记这一点），所以 ArrayDeque 逻辑上来说是无界的，所以在源码中没有显式的方法去判断 ArrayDeque 是否已满（暂时了解这么多，后面会在讲述 ArrayDeque 的无参构造时讲述 ArrayDeuqe 如何判断已满）。
    * 而这段代码在这里是可以等价于取模运算（并不是说遇见取模运算都可以这样去优化，要看条件的）。
    */
    
    /**
    * 指针先后移，再取模运算。
    * 前提条件是 0 < i < modulus
    */
    static final int inc(int i, int modulus) {
        if (++i >= modulus) i = 0;
        return i;
    }
	/**
    * 指针先前移，再取模运算。
    * 前提条件是 0 < i < modulus
    */
    static final int dec(int i, int modulus) {
        if (--i < 0) i = modulus - 1;
        return i;
    }
	/**
    * 指针先后移指定数值，再取模运算
    * 前提条件是 0 < i < modulus
    */
    static final int inc(int i, int distance, int modulus) {
        if ((i += distance) - modulus >= 0) i -= modulus;
        return i;
    }
	/**
    * 指针先前移指定数值，再取模运算
    * 前提条件是 0 < i < modulus
    */
    static final int sub(int i, int j, int modulus) {
        if ((i -= j) < 0) i += modulus;
        return i;
    }
    /**
    * 静态方法，获取指定数组中指定位置的元素
    * return：
    *		E => 泛型
    */
    @SuppressWarnings("unchecked")
    static final <E> E elementAt(Object[] es, int i) {
        return (E) es[i];
    }
    
    static final <E> E nonNullElementAt(Object[] es, int i) {
        @SuppressWarnings("unchecked") E e = (E) es[i];
        if (e == null)
            throw new ConcurrentModificationException();
        return e;
    }
    
    /**
    * ArrayDeque 的扩容方法
    * param:
    * 		needed: 开发者所需要额外扩容的容量值
    */
    private void grow(int needed) {
        // 旧容量
        final int oldCapacity = elements.length;
        // 新容量（未赋值）
        int newCapacity;
        /**
        * 增长值（jump 和 needed 并非一样，ArrayDeque 实际扩容的值需要二者协调配合）
        * 两种增长逻辑：
        * 		1.旧容量 < 64  | 	增长值 = 旧容量 + 2
        * 		2.旧容量 >= 64 |	增长值 = 旧容量/2（向下取整）
        */
        int jump = (oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1);
        /**
        * 这里需要认真阅读，在这里对编程语言不是很熟悉的开发者也许有个疑问，就是 newCapacity 的初始化（即 newCapacity 什么时候完成的初始化的，下面就请保持耐心阅读吧）
        * if 的判断条件语句是短路或，二者有一真即可。
        * 1. jump < needed 成立则后面短路不再执行，直接执行方法体，newCapacity 被初始化。
        * 2. jump < needed 不成立，执行后面的语句 (newCapacity = (oldCapacity + jump)) - MAX_ARRAY_SIZE > 0，此时 newCapacity 被初始化并进行计算判断，此时又需要进行分情况讨论，请看 3、4。
        * 3. jump < needed 不成立，(newCapacity = (oldCapacity + jump)) - MAX_ARRAY_SIZE > 0 同样成立，执行方法体， newCapacity 重新被赋值。
        * 4. jump < needed 不成立，(newCapacity = (oldCapacity + jump)) - MAX_ARRAY_SIZE > 0 同样不成立，方法体不执行，注意此时 newCapacity 已经被初始化过了，所以后续的代码是没有问题的（这里最容易误判 newCapacity 并未初始化）。
        */
        if (jump < needed
            || (newCapacity = (oldCapacity + jump)) - MAX_ARRAY_SIZE > 0)
            newCapacity = newCapacity(needed, jump);
        
        // 这里使用了数组的拷贝重新对 ArrayDeque 的底层数组 elements 赋值，同时又将 elements 的内存地址赋值给新变量 es（es 被 final 修饰，无法更改指针）。所以后续只有对 es 变量的操作，当 es 变量发生变化时 elements 同样也变化。
        final Object[] es = elements = Arrays.copyOf(elements, newCapacity);
        // 处理环形队列指针冲突问题，为什么会产生环形冲突问题，这里无法用图片解释，后面会有讲述
        if (tail < head || (tail == head && es[head] != null)) {
            
            int newSpace = newCapacity - oldCapacity;
            System.arraycopy(es, head,
                             es, head + newSpace,
                             oldCapacity - head);
            for (int i = head, to = (head += newSpace); i < to; i++)
                es[i] = null;
        }
    }

    /**
    * 计算新容量的值，在上面的注释中说过，ArrayDeque 的扩容是基于 needed 和 jump 二者配合来完成的
    * param:
    * 		needed:	所需容量值
    * 		jump:	增长值
    */
    private int newCapacity(int needed, int jump) {
        // 旧容量 oldCapacity 
        //  minCapacity （未初始化），数组所需最小容量
        final int oldCapacity = elements.length, minCapacity;
        /**
        * 初始化 minCapacity ，其值为 oldCapacity 和 needed 的和
        * 1. minCapacity > MAX_ARRAY_SIZE 成立，直接返回 Interger.MAX_VALUE
        * 2. minCapacity < 0 这里本意是判断 minCapacity 是否过大，但是对计算机组成原理有了解的开发者应该知道，当 Integer>MAX_VALUE + 1 后会变为负数，而不是溢出报错，这是符号位从 0 变成了 1，导致整体数值变为负数。所以这里是判断 minCapacity < 0 ,但是会抛出 Sorry, deque too big。
        */
        if ((minCapacity = oldCapacity + needed) - MAX_ARRAY_SIZE > 0) {
            if (minCapacity < 0)
                throw new IllegalStateException("Sorry, deque too big");
            // 这里返回了 Integer.MAX_VALUE 开发者绝对有一定的疑问，后面会讲述，可以跳转至其标题所示位置
            return Integer.MAX_VALUE;
        }
        // needed > jump ，所需值大于增长值，那么直接返回 minCapacity
        if (needed > jump)
            return minCapacity;
        // 默认扩容逻辑，旧容量 + 增长值，如果超出了范围直接返回 ArrayDeque 的 MAX_ARRAY_SIZE，
        return (oldCapacity + jump - MAX_ARRAY_SIZE < 0)
            ? oldCapacity + jump
            : MAX_ARRAY_SIZE;
    }
    
    // 后面还有一些 Deque 接口的方法的实现，前面章节讲述过使用示例，所以后面也不再展示。如果开发者想要知道其具体是如何实现的，可以自行看源码，ArrayDeque 的增删改查基本都是基于以上静态方法完成的。
}
```

### 为什么 `MAX_ARRAY_SIZE` 是最大安全容量？

在 JVM 中，**数组对象会预留头部 8 字节用于存储自身信息**（如：数组长度、数组中所包含的类型等等）。

所以 `MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8` 。

### `ArrayDeque` 的无参构造

![hellow](.\img\ArrayDeque\ArrayDeque 无参构造.png)

如图所示为 ArrayDeque 的初始化，图中所示和实际并不完全相符，下面会做讲述，请一字一字阅读。

ArrayDeque 的无参构造，如下：

```java
// Java 8+
public ArrayDeque() {
	elements = new Object[16];
}

// Java 17+
public ArrayDeque() {
	elements = new Object[16 + 1];
}
```

二者区别，很明显，Java 8 中 ArrayDeque 初始化了 16 位长度，Java 17 初始化了 16+1 为长度。下面会解释其原因。

ArrayDeque 是一个环形队列，也正如图所示。
在 Java 8 中：
当队列为空时，指针 `head == tail`（即头指针和尾指针重叠），此时记作：空队列 => `head == tail`。
当队列为满时，`tail` 指向最后一个元素的下一位，这也就意味着，`tail == head` （即位指针和头指针重叠）。
那么当开发者判断队列是否已满，这就很容易出现错误，故此 Java 在 `ArrayDeque` 中预留了一位，这里暂且称其为预留位。
这也就是说物理容量为 16，而实际允许使用的容量为 15。

判断队列容量已满的方法就从：`tail == head` 变为了 `tail + 1  == head` （但学习过 408 的开发者应该知道其最常用的判断方式是 尾指针&队列长度）。

#### 为什么在 Java 17 中变成了 16 + 1?

主要是因为 ArrayDeque 队列在 Java 8 中含有隐式逻辑问题。

所以 Java 官方在源码中显示的使用 16 + 1 表示可用容量为 16 位，1 位为预留位。

### `grow()` 中的指针冲突问题



### `newCapacity()` 为什么返回了 `Integer.MAX_VALUE` ？

Java  官方允许开发者使用 `Integer.MAX_VALUE` ，这是一种非常极端的情况下，`MAX_ARRAY_SIZE` 无法满足 `ArrayDeque` 对开发者的需求，需要使用 `Integer.MAX_VALUE` ，但是这样情况下存储的数据有可能是不安全的，建议开发者不要使用。

## 注意事项

1. `ArrayDeque` 的线程是不安全的。
2. `ArrayDeque` 不支持存储 `null` 元素。
3. `ArrayDeque` 虽说是无界，但在物理使用时是有界，安全的前提下最大值为 2147483647 - 8，极端情况下为 2147483647。
4. 正如上一章所说，`ArrayDeque` 是一个队列，Java 给其定位是高性能双端队列，所以开发者在实际开发时要遵守队列的使用规范。虽然 Java 给 `ArrayDeque` 提供了很多中间元素的操作，但是不建议开发者频繁使用这些函数，这些函数的性能很差。

