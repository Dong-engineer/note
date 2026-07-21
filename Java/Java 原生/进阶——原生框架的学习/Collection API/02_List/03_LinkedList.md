# LinkedList

`LinkedList` 是一个双向链表，与 `ArrayLsit` 形成鲜明对比， `ArrayList` 是单向的，这也就导致二者在增删改查方面的操作相率并不相同，开发者需要根据实际使用情况来决定数据结构的使用。

`LinkedList` 的底层不是连续的内存地址空间，而是通过将一个节点分为三部分，分别为前驱节点、数据域、后去节点来存储结构，指针域存储下一节点的存储地址。

在 Java 中 `LinkedList` 还实现了 `Deque` 接口，这也就意味这其也可以作为队列来使用使用，具体看源码。

## 源码

```java
public class LinkedList<E> 
    extends AbstractSequentialList 
    implements  List<E>,Dequen<E>,java.io.Serializable {

    // 链表头节点
    transient Node<E> first;
    // 链表尾节点
    transient Node<E> end;

    // 链表长度
    transient int size;

    // 集合内部类，用于表示链表的各个节点
    private static class Node<E> {

        Node<E> prev; // 上一节点
        NOde<E> next; // 下一节点

        E item; // 当前节点数据
        // 构造器
        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
    
    // 添加方法
    public boolean add(E e) {
        
        linkLast(e);
        return true;
        
    }
    
    // 获取指定位置元素
    public E get(int index) {
        // 检查是否越界
        checkElementIndex(index);
        
        return node(index).item;
    }
    // 获取指定位置元素核心方法
    Node<E> node(int index) {
        // 核心方法优化
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        } else {
            Node<E> x = end;
            for (int i = size - 1; i > index; i++) {
                x = x.prev;
            }
            return x;
        }
    }
    
    // 指定位置插入元素
    public void add(int index, E e) {
        // 检查插入位置
        checkPostionIndex(index);
        
        if(index == size);
        	// 末尾插入元素
        	LinkLast(e);
        else 
            // 在某个节点前插入元素
            LinkBefore(e, node(index));
    }
    
    // 在指定节点前插入新节点 (O(1) 操作)
    void linkBefore(E e, Node<E> succ) {
       	
        final Node<E> pred = succ.prev;
        final Node<E> newNode = new Node<>(pred, e, succ);
        succ.prev = newNode;
        if (pred == null)
            first = newNode;
        else
            pred.next = newNode;
        size++;
        modCount++;
    }
    
}
```

以上代码并不完全，还有很多的增删改查方法，具体仍需要以 Java 源码为主。

以上代码片段对于学习过 408 的开发者来说并不难理解，这里也不再详细讲述，毕竟 408 是每一个开发者必修的课程。

对于以上代码片段有一点开发者需要注意， Java 将很多方法进行了优化，如：获取指定位置元素。Java 判断了元素位置是在集合前半段还是后半段，这样大大减少了查询元素的时间。

## 使用实例

```java
package Mastering_the_API.Collection;


import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName Test03
 * @Description TODO
 * @Author 86198
 * @DATE 2025/9/28 16:14
 * @Version 1.0
 */
public class Test03 {

    public static void main(String[] args) {
        List<String> LinkedList = new LinkedList<>();
        String[] strings = {"你好", "中国", "Hello", "World", "!"};
        long addStartTime = System.nanoTime();
        LinkedList.add(strings[0]);
        LinkedList.add(strings[1]);
        LinkedList.add(strings[2]);
        LinkedList.add(strings[3]);
        LinkedList.add(strings[4]);
        long addEndTime = System.nanoTime();
        System.out.println("添加程序耗时：" + (addEndTime - addStartTime));
        System.out.println(LinkedList);
        System.out.println("====================");

        System.out.println("添加元素中（默认添加到尾元素），请稍等...");
        long indexStartTime = System.nanoTime();
        LinkedList.add(1, "dong");
        long indexEndTime = System.nanoTime();
        System.out.println("添加耗时:" + (indexEndTime - indexStartTime));
        System.out.println(LinkedList);
        System.out.println("====================");

        System.out.println("删除元素中，请稍等...");
        long delStartTime = System.nanoTime();
        LinkedList.remove(1);
        long delEndTime = System.nanoTime();
        System.out.println("添加耗时:" + (delEndTime - delStartTime));
        System.out.println(LinkedList);
        System.out.println("====================");

        System.out.println("查询元素中，请稍后...");
        long selStartTime = System.nanoTime();
        String elementOne= LinkedList.get(0);
        long selEndTime = System.nanoTime();
        System.out.println("查询结果：" + elementOne);
        System.out.println("查询耗时：" + (selEndTime - selStartTime));
        System.out.println("====================");

        System.out.println("替换元素中，请稍后...");
        long setStartTime = System.nanoTime();
        LinkedList.set(1,"China");
        long setEndTime = System.nanoTime();
        System.out.println(LinkedList);
        System.out.println("替换耗时：" + (setEndTime - setStartTime));
        System.out.println("====================");
    }

}
```

```cmd
添加程序耗时：48300
[你好, 中国, Hello, World, !]
====================
添加元素中（默认添加到尾元素），请稍等...
添加耗时:18800
[你好, dong, 中国, Hello, World, !]
====================
删除元素中，请稍等...
添加耗时:9800
[你好, 中国, Hello, World, !]
====================
查询元素中，请稍后...
查询结果：你好
查询耗时：5800
====================
替换元素中，请稍后...
[你好, China, Hello, World, !]
替换耗时：6600
====================
```

## 注意事项（重点）

对于 `LinkedList` 的注意事项，需要很多的文字才能完全讲述完，主要是 `ArrayList` 和 `LinkedList` 的区别，还有一些线程问题。

### `ArrayList、LinkedList` 区别

在上节学习过了 `ArrrayList` 。

`ArrayList` 的底层是通过动态数组实现的，每次添加或删除元素时，都需要对目标元素的前面的所有元素或者后面的所有元素进行移动。

`LinkedList` 的底层是双向链表实现的，这使得 `LinkedList` 链表在进行增删操作时无需进行大量的元素移动操作，只需要修改节点的前驱、后驱节点即可。

理论上 `LinkedList` 的增删操作速度是远远优于 `ArrayList` 。

但是开发者认真阅读这两节的使用示代码片段的话会发现，`LinkedList` 的执行速度并没有预期的操作速度快，这是为什么？

首先应当是其他因素对实现的影响，在两个使用示例中尽量减少其他因素对实验结果的影响，采取了控制变量方法，无论是添加（删除）方法的调用还是参数，都保持一致。

其次是源码的实现，在二者的源码中，开发者认真阅读的话会发现，`LinkedList` 在进行增删时会进行依次遍历操作 => `LinkBefore(e, node(index))` ，`node(index)` 会进行遍历查询。

这也就是为什么 `LinkedList` 的增删操作慢于 `ArrayList` 的原因。

除此之外还有一些操作 `ArrayList` 的增删也会快于 `LinkedList` ，在 `ArrayList` 预先分配空间充足（一定要注意是空间充足）的情况下，尾插法也是要优于 `LinkedList` 的尾插法的。

那么查询呢？

二者的查询速度，`ArrayList` 的速度是毋庸置疑要快于 `LinkedList` ，原因还是二者的数据结构导致，`ArrayList` 底层动态数组，本就是一片连续内存空间，这很方便数据的查询，而 `LinkedList` 则需要每个前后节点的内存地址寻找目标数据，这会很慢。

#### 总结

`ArrayList` 的综合性能是大于 `LinkedList` 的，那么开发者会有疑问，为什么是综合性能，通过以上文字描述，`LinkedList` 根本找不到一丁点胜算，完全被 `ArrayList` 压着打。

Java 当然也知道 `LinkedList` 的增删操作时，要进行遍历查询操作这会浪费很多时间，所以 Java 给出了迭代器 `Iterator` ，Java 为了优化 `LinkedList` 的增删改查操作，书写了 `ListIterator` 接口（下节讲述）。

这也就要求了开发者对于 Java 的理解要一定程度，开发者合理使用 `LinkedList` 才能完全发挥出其真正的效率。

总之，开发者需要灵活使用 `ArrayList、LinkedList` 。无论是哪个 `List` 子接口，都有各自的优势，且存在即合理，强扭的瓜不甜，合适才是绝配。 开发者需要根据对应的场景使用合适的接口，这也就是为什么，经验重要的原因。

### 线程问题

#### `ConcurrentModificationException` 快速失败机制

`ArrayList、LinkedList` 都不是线程安全的，这也就表明当有多个线程对 `ArrayLsit、LinkedList` 进行修改时，导致集合中存储的数据为脏数据。

存储的数据变为脏数据，这是程序错误，不再是程序异常，在 *异常* 一节中讲述过，程序错误和程序异常并不相同，程序错误在整个系统同运行时会导致存储、传输的数据为错误数据，这很严重。

Java 为了避免这种错误在 `AbstractList` 接口中增加了属性 `modCount`（对于 `AbstractList` 开发者可以自行去看源码，`List` 及其大多数的子类、子接口都继承了，当然也包括间接继承），而 `modCount` 属性记录了一个集合的修改次数，当并发程序同时修改某一集合时，`modCount` 会 +1，当开发者通过 `iterator()` 或 `listIterator()` 获取迭代器时，迭代器会记录当前的 `modCount` ，当开发者每次通过迭代器进行操作时，会检查二者的 `modCount` 是否相同，如果不同，则会抛出`ConcurrentModificationException` ，使得当前程序快速失败。

如下示例：

```java
public class Test05 {
      public static void main(String[] args) {
          List<String> list = new ArrayList<>();
          list.add("A");
          list.add("B");
          list.add("C");

          Iterator<String> iterator = list.iterator();

          while (iterator.hasNext()) {
              String item = iterator.next(); // 触发检查，如果不一致会抛出 ConcurrentModificationException

              if ("B".equals(item)) {
                  // 在迭代时，通过集合的方法修改结构，会导致 modCount 不一致
                  list.remove(item);
              }
              System.out.println(item);
          }

          System.out.println(list);
      }
}
```

以上代码如果开发者去运行了一边的话，结果是并不是理想中的结果，Java 并不会报出程序异常结果，这是一个巧合，开发者需要从根本分析错误。

##### 分析问题

错误发生的位置，在以上代码中已经标记出来，在 `while` 循环体中，不再分析其前面的代码。

当我们在获取集合的迭代器时，会将当前集合的 `modCount` 记录为`expectedModCount` 。

每次调用迭代器的 `next()` 方法时，都会检测一次集合的 `modCount` 是否和 `expectedModCount` 是否相等。相等则继续遍历，不相等则直接抛出异常 `ConcurrentModificationException`  。

运行结果如下：

```cmd
A
B
[A, C]
```

根据运行结果来看，并没有理想的那样抛出异常，哪里出现了问题呢？

其实这是一个偶然的结果，十分巧合。下面再给出一段代码，Java 会抛出异常，如下：

```java
public class Test05 {
      public static void main(String[] args) {
          List<String> list = new ArrayList<>();
          list.add("A");
          list.add("B");
          list.add("C");
          list.add("D");

          Iterator<String> iterator = list.iterator();

          while (iterator.hasNext()) {
              String item = iterator.next(); // 触发检查，如果不一致会抛出 ConcurrentModificationException
              if ("B".equals(item)) {
                  // 在迭代时，通过集合的方法修改结构，会导致 modCount 不一致
                  list.remove(item);
              }
              System.out.println(item);
          }

          System.out.println(list);
      }
}
```

运行结果如下：

```cmd
A
B
Exception in thread "main" java.util.ConcurrentModificationException
	at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:1095)
	at java.base/java.util.ArrayList$Itr.next(ArrayList.java:1049)
	at Mastering_the_API.Collection.Test05.main(Test05.java:23)
```

##### 猜想

根据两段代码的差异，开发者不难发现，仅是对集合进行了一次添加元素，这是为什么？是否跟集合中元素多少有关呢？开发者可以自行尝试做实验，当开发者进行多次实验后，会发现确实和集合中含有多少元素有关，简单来说集合的长度影响了程序是否抛出 `ConcurrentModificationException` （确实和集合长度有关，但也和开发者所要进行操作的元素位置有关，下面会详细讲述）。

可以肯定的是集合添加元素的程序是没有错误，获取迭代对象也没有错误（这是编写 Java 集合框架前辈留下的成就，经过时间及后人的验证毋庸置疑），有错误的地方还是在 `while` 循环体中，可是在 `while` 循环体中调用的方法，除了集合的删除操作外，其他的的函数调用均符合使用规范，这是为什么呢？

前面说过了 `next()` 会检查 `modCount` 和 `expectedModCount` 是否一致，不一致会抛出异常。既然没有抛出异常也就说明没有触发 `next()` ，不然肯定会抛出异常的。当让开发者也可以假定其他错误（这就需要开发者自行去验证了）。

##### 分析代码

以第一段代码为例

首先，获取迭代对象记录当前 `modCount` （前面有3次）= 3，`expectedModCount` = 3。

第一次循环，`hasNext()`判断当前遍历的指针位置是否等于集合长度，指针现在在 A 元素前面，不等于集合长度，进入循环体。 `next()` 调用，判断`modCount、excepectModCount` （此时相等，二者均为3），指针移动至 A 元素，则此时 `item` 值为 A ，不匹配后面的 `if` 判断，没有对集合进行结构性修改，`modCount、expectedModCount` 不变，输出 `item.toString()` 。

第二次循环，`hasNext()` 判断当前遍历的指针位置是否等于集合长度，指针现在在 B 元素前面，不等于集合长度，进入循环体。`next()` 调用，判断`modCount、excepectModCount` （此时相等，二者均为3），指针移动至 B 元素，则此时 `item` 值为 B ，匹配后面的 `if` 判断，有对集合进行结构性修改，`modCount` + 1，注意我们在修改集合时集合的长度同样也进行了变化，此时集合的 `size` = 2 ，且 C 元素需要前移，输出 `item.toString()` 。

第三次循环，`hasNext()` 判断当前遍历的指针位置是否等于集合长度，在第二次循环中进行了删除 B 元素的操作，这也就导致 C 元素前移，此时指针指向 C 元素，其物理位置为 2，也就等集合的 `size` ，循环直接结束。

通过以上分析开发者不难发现，第三次的 `next()` 并没有成功调用，而是直接跳过，这就说明没有第三次对 `modCount、expectedModCount` 进行检查，也就不会抛出 `ConcurrentModificationException` 。

第二段代码也成功解释了以上内容的正确性。

##### 总结

上面的猜想对了，但不完全正确，正如小括号中所说，也和开发者所要进行操作的元素位置有关。

开发者可以多次试验，实际的确如此。

这也照应了上面所说，这是一个十分偶然的巧合，Java 官方在教程中始终强调了迭代器的使用要遵循规范，所以开发者要遵循开发规范，不要随意使用不合理合法的程序。（那怎么知道合理合法呢？=> 作者表示多犯点错不就知道了。。。）

 正确的使用代码，如下：

```java
public class Test05 {
      public static void main(String[] args) {
          List<String> list = new ArrayList<>();
          list.add("A");
          list.add("B");
          list.add("C");

          Iterator<String> iterator = list.iterator();

          while (iterator.hasNext()) {
              String item = iterator.next(); // 触发检查，如果不一致会抛出 ConcurrentModificationException

              if ("B".equals(item)) {
                  // 记住要使用迭代器的修改方法！！！
                  iterator.remove();
              }
              System.out.println(item);
          }

          System.out.println(list);
      }
}
```

