# HashMap 键值对丢失

**Java 官方不建议开发者使用非常量类型作为 `HashMap` 的 key**，但是在本节内容中，作者**表示一定不要使用非常量类型作为 `HashMap` 的 key**。

假设开发者有一段如下代码：

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description HashMap 键值对丢失
 * @Author Dong
 * @Date 2026/7/11 21:59
 */
public class One {

    public static class Key {
        private String key;

        public Key() {}

        public Key(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key1 = (Key) o;

            return Objects.equals(key, key1.key);
        }

        @Override
        public int hashCode() {
            return key != null ? key.hashCode() : 0;
        }
    }

    public static void main(String[] args) {
        Map<Key, Integer> map = new HashMap<>();

        Key one = new Key("one");
        Key two = new Key("two");
        Key three = new Key("three");
        Key four = new Key("four");

        map.put(one, 1);
        map.put(two, 2);
        map.put(three, 3);
        map.put(four, 4);

        System.out.println("map" + map);
        System.out.println("map.get(one):" + map.get(one));

        one.setKey("five");

        System.out.println("map.get(one):" + map.get(one));
        System.out.println("map.get(two):" + map.get(two));
        System.out.println("map.get(three):" + map.get(three));
        System.out.println("map.get(four):" + map.get(four));

        System.out.println("map" + map);

        System.out.println(new Key("one"));
        System.out.println("map.get(one): " + map.get(new Key("one")));
        System.out.println("map.get(one): " + map.get(new Key("two")));
        System.out.println(new Key("five"));
        System.out.println("map.get(one): " + map.get(new Key("five")));

    }
}
```

这段代码是根据 Java 官方的示例来书写的，输出结果如下：

```cmd
map{One$Key@300d26=4, One$Key@1ae66=1, One$Key@1c24c=2, One$Key@693a59e=3}
map.get(one):1

map.get(one):null
map.get(two):2
map.get(three):3
map.get(four):4

map{One$Key@300d26=4, One$Key@2ff6b2=1, One$Key@1c24c=2, One$Key@693a59e=3}

One$Key@1ae66
map.get(one): null
map.get(one): 2
One$Key@2ff6b2
map.get(one): null
```

第一次打印 `map` 开发者可以看到每一个 `key` 的 hash 值以及所对应的 `value` ，正常情况下去获取也是可以获取到的，如：`map.get(one)`

开发者可以注意到，`one.setKey("five")` 更改了对象的值，再次获取 `map.get(one)` 会发现键对应的值为 `null` 。

在前面的学习中开发者知道 `HashMap` 存储元素时，会调用 key 本身的 `hashCode()` 并将其与 `value` 绑定在一起，修改了 `one` 的属性（且该属性参与到 `hashCode()`）的计算之中），所以再次打印 `map` 会发现 `One$Key@1ae66=1 => One$Key@2ff6b2=1` 。

## 键值对丢失——桶错位

开发者肯定会有疑惑，不是有去尝试 `map.get(new Key("one"))` 和 `map.get(new Key("two"))` 获取元素对象吗？在终端输出的对象地址也能对的上，可是获取的结果还是 `null` 。
首先，开发者要明白，`map.put(one, 1)` 所存储的元素真的丢失了吗？实际并没有，通过打印信息开发者依然可以看的见 `map` 中依然存储这该元素的信息。

其次，开发者尝试修改 `one` 的属性值，实际是导致 `map` 在取出元素时，**对桶的索引位置计算发生了错误——桶错位**。

先看第一次添加元素 `map.put(one, 1)` ，假设 `map` 通过计算该元素的 `hashCode()` 应该被放置在 `Node<K, V> tab` 数组索引为 1 的位置，那么该元素会被插入到  `Node<K, V> tab` 数组索引为 1 的位置下或者是该位置下索引所挂在的数据结构中（链表或红黑树）。

`one.setKey("five")` 这会导致 `hashCode()` 的计算结果不等于原来的旧对象，`map` 在获取桶的位置时会导致桶的位置找错，没有匹配的目标，所以 `map.get(one) = null`。

`map.get(new Key("five"))` 原理也是如此。

`map.get(new Key("one"))` ，既然知道了 `map` 寻找桶的位置是根据元素的 `hashCode()` 的，那么开发者不妨尝试去创建一个和旧 `one` 一样 `hashCode()` 的值，如：`new Key("one")` 。但是结果依然为 `map.get(new Key("one")) = null` 。这是因为 `HashMap` 找到目标元素后还会进行一次 `equals()` 对比，对比失败返回结果 `null` 。

旧的 `one` 通过 setter 将属性值修改为了 `five` ，而 `new Key("one")` 的属性值为 `one` ，所以返回结果 `null` 。

为了印证以上的猜想，开发者不妨再次修改 `one` ，代码如下：

```java
one.setKey("one");
System.out.println("map.get(one): " + map.get(new Key("one")));
```

这样依然会得到正确的结果：

```cmd
map.get(one): 1
```

**尽管有修正的手段，作者表示一定不要使用可变类型作为 `HashMap` 的 key。在生产环境开发者会丢失很多的数据。**
