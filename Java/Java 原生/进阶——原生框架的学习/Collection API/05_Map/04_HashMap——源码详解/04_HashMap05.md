# HashMap

本文的内容从 `Node` 又回到 `HashMap` ，在 *04_HashMap03* 中了解到了 `Node` 作为 `HashMap` 的存储元素的单个节点，在本文中，开发者将会学习到 `HashMap` 是如何管理 `Node` 。

`HashMap` 对于 `Node` 节点管理的方法很值得开发者学习，比如：哈希寻址、联表与红黑树的转化、扩容机制。

也许在大部分的实际业务中很少会使用到这些方法，但总有一瞬间开发者会获得灵感，使得枯燥无味的业务流程变得更加有趣味。

## 添加 Node 节点——putVal()

```java
/**
 * Map.put() 的依赖方法
 * @param hash key 的哈希值
 * @param key 指定 key 值
 * @param value 指定的 value 值
 * @param onlyIfAbsenet: true —— 禁止修改存在的值**
 * @param evict: 驱逐标记（仅对 LinkedHashMap 有效）| true - 正常插入/扩容场景（允许淘汰旧元素）| false - 初始化导入场景（当前方法调用时传入，不允许淘汰元素）
 */
final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
    
    // tab 临时存储 table
    Node<K,V>[] tab;
    
    // p 目标索引的节点
    Node<K,V> p;
    
    // 临时存储长度变量
    int n;
    
    // 临时存储目标数组索引（通过 (n-1) & hash 计算得出）
    int i;
    
    // 判断 table 初始化，table 并未初始化或者长度为 0，这里就需要通过 resize() 去初始化 table 或者说调整 table 的容量
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    
    // 计算目标索引是否存在元素，目标索引没有元素存在，创建新的节点
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    
    // 目标索引存在元素
    else {
        // 临时存储元素的节点（这里开发者可以当做一个临时交换站）
        Node<K,V> e;
        // 临时存储键的变量（具体存储情况请看上下文信息）
        K k;
        
        // 目标索引下元素和插入元素 hash 值和 key 相同
        if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        
        // 旧元素是树节点，交给树节点函数处理
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);

        // 不为树节点且目标索引元素和添加的新元素 key 不相同
        else {
            for (int binCount = 0; ; ++binCount) {
                // 当前节点的下一元素为空，链表末尾，插入新节点（尾插法）
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    
                    // 当前循环达到了红黑树阈值，交给红黑树处理
                    if (binCount >= TREEIFY_THRESHOLD - 1)
                        treeifyBin(tab, hash);
                    
                    break;
                }
                // 如果 p.next 的 hash 值和添加的新元素的 key 相等
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;

                // 更新 p 指针，进入下一元素的判断
                p = e;
            }
        }
        // 匹配到了节点（下面称为旧节点）
        if (e != null) {
            // 取出旧值
            V oldValue = e.value;
            
            // 判断就只是否允许修改且是否为空
            if (!onlyIfAbsent || oldValue == null)
                // 允许修改，则覆盖旧值
                e.value = value;
            
            // 钩子方法，供 LinkedHashMap 重写（用于维护访问顺序，如 LRU 缓存）
            afterNodeAccess(e);
            
            // 返回旧值
            return oldValue;
        }
    }
    // 修改次数 +1
    ++modCount;
    // 长度超出扩容阈值
    if (++size > threshold)
        // 扩容
        resize();
    // 钩子方法，供 LinkedHashMap 重写（用于插入后淘汰旧元素，如 LRU 缓存）
    afterNodeInsertion(evict);
    // 插入新节点，返回 null
    return null;
}
```

`putVal()` 展示了 `HashMap` 存储结构的全貌，关于 `putVal()` 的流程图开发者可以自行搜索或绘制。

### 哈希寻址

```java
if ((p = tab[i = (n - 1) & hash]) == null) 
    tab[i] = newNode(hash, key, value, null);
```

![该图来自于 CSDN 博主：家乡的落日](../img/HashMap/哈希寻址.gif)

`tab[i = (n - 1) & hash]` 会计算出元素在 `table` 数组的哪一索引位置下，`if ((p = tab[i = (n - 1) & hash]) == null) ` 该索引下的位置如果为 null（即该位置没有元素）则会将元素放置该位置，如上图所示。

如果该位置下有元素，如果该索引下是链表则会进行尾插，如果是红黑树则会交给红黑树的插入方法进行处理。

## 批量添加 Map 集合元素——putMapEntries()

```java
/**
 * 将另一个 Map 集合 m 中的所有键值对批量导入到当前 HashMap 实例中,该方法被 HashMap(Map<? extends K, ? extends V> m) 调用
 * @param m 指定的映射集合
 * @param evict 驱逐标记（仅对 LinkedHashMap 有效）| true - 正常插入/扩容场景（允许淘汰旧元素）| false - 初始化导入场景（当前方法调用时传入，不允许淘汰元素）
 * 下面以默认的初始化容量 16、0.75f 负载因子、实际容量为 10 为例
 */
final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
    // 获取参数 m 集合长度
    // s = 10
    int s = m.size();
    // 10 > 0
    if (s > 0) {
        // 判断当前调用该函数的底层 table 属性是否实例化（上面的构造器开发者如果认真阅读了的话，会发现 HashMap 在 new 完对象后，其底层的 table 属性是没有被实例化的，至于其会在什么时候被初始化请继续往下看）
        if (table == null) {
            // 计算导入 m 中所有元素所需的最小容量（避免后续插入频繁扩容）
            // s/loadFactor => 10/0.75 = 13.33333
            /**
             * 这里计算的含义是：当 Map 集合有 10 个元素时（此时容量已经占了 75%）
             * 那么开发者需要思考 Map 逻辑上的总容量是多少呢？（这很简单，现有数量/占比=总数）
             * 而后面我们只需要判断逻辑总容量是否超出最大物理容量即可（这里无需判断逻辑总容量是否超出 m 的实际物理容量，至于为何请保持疑问？下面阈值出会讲述）
             */
            // 对 ft 进行赋值, ft = 14.33333（+ 1.0f，是因为 int 的强转会向下取整）
            float ft = ((float)s / loadFactor) + 1.0F;
            // 判断 ft 是否超出 HashMap 的最大容量
            // ft = 14.3333 < MAXIMUM_CAPACITY
            // 直接强转 ft ，ft = 14，则 t = 14
            int t = ((ft < (float)MAXIMUM_CAPACITY) ?
                     (int)ft : MAXIMUM_CAPACITY);
            // 若计算出的最小容量 t 大于当前阈值，调整阈值为 t 对应的最小 2 的幂次
            // 这里依然会有疑问 threshole 为多少呢？（开发者这里先知道 thresold = 总容量 * 负载因子）
            // threshold = 16 * 0.75 = 12
            // t = 14 > 12，所以需要重新计算扩容阈值
            if (t > threshold)
                // threshold = tableSizeFor(14) = 16
                threshold = tableSizeFor(t);
        } 
        // table 已经被初始化
        else {
            // 若 m 的元素个数 s 大于当前扩容阈值，且数组长度未达最大值，循环扩容
            // 目的：提前扩容到足够容纳 m 的所有元素，避免插入过程中多次扩容
            while (s > threshold && table.length < MAXIMUM_CAPACITY)
                // 以上提出的两个疑问（一个是 threshole 的计算，一个是 table 会在合适实例化），将会在 resize() 方法中得到答案
                resize();
        }
        
		// 插入元素
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            K key = e.getKey();
            V value = e.getValue();
            putVal(hash(key), key, value, false, evict);
        }
    }
}
```

##  table 初始化及扩容方法——resize()

```java
/**
 * HashMap 扩容/初始化核心方法（HashMap 很重要的一个方法，很多的疑问都会在这里得到答案）：
 * 1. 无参构造第一次put元素：初始化默认容量16+阈值12
 * 2. 指定容量构造第一次put元素：初始化tableSizeFor计算的容量+对应阈值
 * 3. 已有元素时put：数组容量翻倍，阈值同步调整
 */
final Node<K,V>[] resize() {
    // 临时存储旧的底层数组（table）
    Node<K,V>[] oldTab = table;
    // 旧数组的容量（长度）：未初始化则为0，已初始化则为数组长度
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    // 旧的扩容阈值（初始化阶段可能临时存储容量值）
    int oldThr = threshold;
    // 新数组的容量
    int newCap = 0;
    // 新的扩容阈值
    int newThr = 0;

    // 分支1：旧数组已初始化（已有元素，本次是扩容操作）
    if (oldCap > 0) {
        // 子分支1：旧容量达到最大值（1<<30），无法再扩容
        if (oldCap >= MAXIMUM_CAPACITY) {
            // 阈值设为整型最大值，后续不会再触发扩容
            threshold = Integer.MAX_VALUE;
            // 返回旧数组，扩容失败
            return oldTab;
        }
        // 子分支2：旧容量翻倍后 < 最大值，且旧容量 >= 默认初始容量（16）
        // 仅满足此条件时，阈值才同步翻倍，否则newThr后续兜底计算
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY) {
            newThr = oldThr << 1; // 阈值同步翻倍（左移1位=×2）
        }
    }
    // 分支2：旧数组未初始化，但旧阈值>0（指定初始容量构造器，第一次put元素）
    else if (oldThr > 0) {
        // oldThr是tableSizeFor计算的合规容量，直接作为新数组容量
        newCap = oldThr;
    }
    // 分支3：旧数组未初始化，旧阈值=0（无参构造器，第一次put元素）
    else {
        // 新容量设为默认初始容量16
        newCap = DEFAULT_INITIAL_CAPACITY;
        // 初始阈值=默认容量×默认负载因子=16×0.75=12
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    // 兜底计算newThr：前面分支未给newThr赋值时（如oldCap<16扩容、指定容量初始化）
    if (newThr == 0) {
        // 计算新阈值=新容量×负载因子
        float ft = (float)newCap * loadFactor;
        // 避免阈值超过最大值：容量<最大值且计算值<最大值则取计算值，否则取整型最大值
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    // 更新全局扩容阈值为计算好的新阈值
    threshold = newThr;

    // 初始化新的底层数组（哈希桶），容量为newCap
    @SuppressWarnings({"rawtypes","unchecked"})
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    // 将全局table指向新数组
    table = newTab;

    // 旧数组不为空（扩容场景）：迁移旧数组节点到新数组
    if (oldTab != null) {
        // 遍历旧数组的每个索引位置
        for (int j = 0; j < oldCap; ++j) {
            // 临时存储当前索引的节点（交换站）
            Node<K,V> e;
            // 旧数组当前索引有节点
            if ((e = oldTab[j]) != null) {
                // 置空旧节点，便于GC回收
                oldTab[j] = null;
                // 场景1：当前索引只有单个节点，无链表/红黑树
                if (e.next == null) {
                    // 计算新索引，直接存入新数组（熟悉的(n-1)&hash）
                    newTab[e.hash & (newCap - 1)] = e;
                }
                // 场景2：当前节点是红黑树节点
                else if (e instanceof TreeNode) {
                    // 调用红黑树的拆分迁移方法
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                }
                // 场景3：当前节点是链表节点，需要拆分迁移
                else {
                    // lo组：扩容后索引不变（j），hi组：扩容后索引=j+oldCap
                    Node<K,V> loHead = null, loTail = null; // 低位组（原索引）
                    Node<K,V> hiHead = null, hiTail = null; // 高位组（新索引）
                    Node<K,V> next; // 临时存储下一个节点

                    // 遍历链表所有节点
                    do {
                        next = e.next;
                        // 核心判断：hash & 旧容量 = 0 → 低位组，否则高位组（性能优化，无需重新计算索引）
                        if ((e.hash & oldCap) == 0) {
                            // 低位组链表尾插法
                            if (loTail == null) loHead = e;
                            else loTail.next = e;
                            loTail = e;
                        } else {
                            // 高位组链表尾插法
                            if (hiTail == null) hiHead = e;
                            else hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);

                    // 低位组存入新数组原索引j
                    if (loTail != null) {
                        loTail.next = null; // 断开链表尾，避免环引用
                        newTab[j] = loHead;
                    }
                    // 高位组存入新数组索引j+oldCap
                    if (hiTail != null) {
                        hiTail.next = null; // 断开链表尾
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    // 返回初始化/扩容后的新底层数组
    return newTab;
}
```

## 删除 Node 节点——removeNode()

```java
final Node<K,V> removeNode(int hash, Object key, Object value,
                           boolean matchValue, boolean movable) {
    Node<K,V>[] tab; Node<K,V> p; int n, index;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (p = tab[index = (n - 1) & hash]) != null) {
        Node<K,V> node = null, e; K k; V v;
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            node = p;
        else if ((e = p.next) != null) {
            if (p instanceof TreeNode)
                node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
            else {
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key ||
                         (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);
            }
        }
        if (node != null && (!matchValue || (v = node.value) == value ||
                             (value != null && value.equals(v)))) {
            if (node instanceof TreeNode)
                ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
            else if (node == p)
                tab[index] = node.next;
            else
                p.next = node.next;
            ++modCount;
            --size;
            afterNodeRemoval(node);
            return node;
        }
    }
    return null;
}
```

## 获取指定 Node 节点——getNode()

```java
/**
* 根据 key 获取指定节点，没有返回 null，get() 的底层依赖函数
* param:
*		key: 指定key
* return:
*		Node<K, V> 内部节点类型
*/
final Node<K,V> getNode(Object key) {
    // 临时存储 this.table
    Node<K,V>[] tab;
    // first => 临时存储目标索引所存储的头指针
    // e => 临时存储 first 的下一节点
    Node<K,V> first, e; 
    // n => this.table.length
    int n;
    // 临时存储参数 key 的哈希值
    int hash; 
    // 临时存储节点的 key（包括 first 节点和链表遍历中的 e 节点，用于 key 相等性判断）
    K k;
    
    // (tab = table) != null => 判断 table 是否实例化
    // (n = tab.length) > 0	 => table 实例化且容量不为 0
    /*
    * (first = tab[(n - 1) & (hash = hash(key))]) != null)
    * 一步一步来看
    * (hash = hash(hey))	=>	hash(key) 在上一节有讲述
    * (n-1) & (hash = hash(key)) => 这段代码的目的是通过 hash(key) % this.table.length 来获取 this 的下标（其真正的目的是通过对哈希值进行取余处理，找到所要存储 key 的目标索引）
    * 
    * 依然以默认的初始化容量 16、0.75f 负载因子、实际容量为 10 （即有 10 个元素占了前 10 个索引）、参数 key = "Java" 为例
    * tab = table
    * n = 16，则 n - 1 =15
    * hashCode("Java") = 2301506（开发者可以自己写代码计算）
    * 则 hash = hash("Java") = ("Java".hashCode()) ^ ("Java".hashCode() >>> 16) = 2301537
    * (n - 1) & hash = 1 （(n-1) & hash 的核心作用是快速计算合法数组索引（0~n-1），等价于 hash % n 但效率更高，这里开发者可以实验一下，一定一定要注意俩个变量的顺序，前后顺序不同结果也不一样）
    * first = tab[1] != null，也就是说 this 的第1个索引所存储的头指针信息不为空
    */
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & (hash = hash(key))]) != null) {
        // 如果计算出的索引有元素存在，则将其与参数 key 的哈希值对比是否相同
        if (first.hash == hash &&
            ((k = first.key) == key || (key != null && key.equals(k))))
            // 相同则直接返回原来的节点
            return first;
        // 如果下一节点也不为空
        if ((e = first.next) != null) {
            // 判断是否为树节点，如果为树节点
            if (first instanceof TreeNode)
                // 交给树节点的处理方式
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            // 不为树节点则遍历链表找到与 key 匹配的节点并返回
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    // 以下情况返回 null：1. table 未初始化；2. table 容量为 0；3. 目标索引无节点；4. 遍历完链表/红黑树未找到匹配 key
    return null;
}
```

## 树化方法——treeifyBin()

```java
/**
* 把指定索引下的整条链表，从普通 Node 链表转换成 TreeNode 链表，最后再真正树化为红黑树
* param:
*		tab: HashMap 的底层数组 table
*		hash: 目标 key 的哈希值（用于定位数组索引）
* 逻辑流程：
* HashMap 底层初始为 Node<K,V>[] table（数组+链表），当局部某条链表长度≥8 且 数组容量≥64（最小树化容量）时，
* 该索引下的普通 Node 链表会先转换为 TreeNode 双向链表，最后再树化为红黑树（满足红黑树特性）。
* 若数组容量<64，即使链表长度≥8，也会先扩容而非树化。
*/
final void treeifyBin(Node<K,V>[] tab, int hash) {
    // 临时存储数组长度变量
    int n;
    // 临时存储目标索引变量
    int index;
    // 临时存储原普通 Node 链表的节点变量
    Node<K,V> e;
    // 分支1：判断参数 tab 是否初始化（一般是HashMap底层的 table ）和 tab 的长度是否达到最小树化容量（64）
    if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
        // 没有初始化或数组容量不足，先扩容（不执行树化）
        resize();
    // 分支2：tab 已经初始化且数组容量满足树化要求，判断目标索引的节点元素是否为空
    else if ((e = tab[index = (n - 1) & hash]) != null) {
        // hd: TreeNode 双向链表的头节点
        TreeNode<K,V> hd = null;
        // tl: TreeNode 双向链表的尾节点
        TreeNode<K,V> tl = null;
        // 遍历原普通 Node 链表，逐个转换为 TreeNode 节点，构建 TreeNode 双向链表（树化前置步骤）
        do {
            // 把普通 Node 节点 e 转换成 TreeNode 节点 p（换壳不换芯，复制原节点的hash/key/value）
            TreeNode<K,V> p = replacementTreeNode(e, null);
            // 子分支1：尾节点如果为空，说明 TreeNode 链表中无元素，初始化头节点
            if (tl == null)
                // 第一个 TreeNode 节点作为头节点
                hd = p;
            // 子分支2：尾节点不为空，说明 TreeNode 链表中已有元素，采用尾插法构建双向链表
            else {
                // 构建双向链表：新节点的prev指向原尾节点，原尾节点的next指向新节点
                p.prev = tl;
                tl.next = p;
            }
            // 更新尾节点为当前 TreeNode 节点（尾插法核心步骤）
            tl = p;
        } while ((e = e.next) != null);
        
        // 把转换后的 TreeNode 双向链表放回原数组的目标索引位置
        if ((tab[index] = hd) != null)
            // 真正的树化操作：将 TreeNode 双向链表转化为红黑树
            hd.treeify(tab);
    }
}
```

