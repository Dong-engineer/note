# HashMap（内部类 TreeNode）

```java
/**
* 静态内部类: TreeNode
* 该类继承了 LinkedHashMap 类中的内部 Entry 类，Entry 类用于封装键值对
* LinkedHashMap.Entry 继承了 HashMap.Node ，所以本质上来说 TreeNode 继承了 Node节点
* 这里也是为下面构造函数——TreeNode() 做铺垫
*/
static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
    // 父节点
    TreeNode<K,V> parent;
    // 左孩子
    TreeNode<K,V> left;
    // 右孩子
    TreeNode<K,V> right;
    // 前驱节点
    TreeNode<K,V> prev;
    // 红色标记
    boolean red;
    
    /**
    * 构造函数
    * 继承 Node()
    */
    TreeNode(int hash, K key, V val, Node<K,V> next) {
        super(hash, key, val, next);
    }

    /**
    * 返回根节点
    */
    final TreeNode<K,V> root() {
        // 遍历节点
        for (TreeNode<K,V> r = this, p;;) {
            if ((p = r.parent) == null)
                return r;
            r = p;
        }
    }

    /**
     * 前移 root 节点（这里后面会有图解）
     * @param tab 指定的 Node[]
     * @param root 指定的节点
     */
    static <K,V> void moveRootToFront(Node<K,V>[] tab, TreeNode<K,V> root) {
        // 临时存储长度变量
        int n;
        if (root != null && tab != null && (n = tab.length) > 0) {
            // 计算 root 所对应的索引
            int index = (n - 1) & root.hash;
            // first 索引所存储的头指针
            TreeNode<K,V> first = (TreeNode<K,V>)tab[index];
            // 判断 root 和 first （即头节点）是否相等
            if (root != first) {
                // 临时存储 root 后驱节点
                Node<K,V> rn;
                // 索引指向 root，为 root 成为头节点做铺垫，后面会调整 first 和 root 的位置
                tab[index] = root;
                // 临时存储 root 前驱节点
                TreeNode<K,V> rp = root.prev;
                // 需要将原来的 root 的前后两节点连接
                // 判断旧 root 的前后驱节是否为空，不为空则重新构建连接
                if ((rn = root.next) != null)
                    ((TreeNode<K,V>)rn).prev = rp;
                if (rp != null)
                    rp.next = rn;
                
                // 这里是判断旧 tab[index] 是否为空
                if (first != null)
                    // 不为空，将元素的前驱设为 root
                    first.prev = root;
                // 更新 root 前后驱节点
                root.next = first;
                root.prev = null;
                // 此时 root 已经为头节点了
            }
            assert checkInvariants(root);
        }
    }

    /**
    * 寻找目标节点
    * param:
    *		h: 参数 k 的哈希值，一般接受 hash(k)
    * 		k: 指定参数的 key
    * 		kc: 待查找键 k 的类对象（提前缓存 comparableClassFor(k) 的结果，避免重复反射，优化性能；可为 null）
    */
    final TreeNode<K,V> find(int h, Object k, Class<?> kc) {
        // 获取调用该函数的节点地址，p 在这里作用实际是一个指针，后面遍历会用
        TreeNode<K,V> p = this;
        // 遍历红黑树
        do {
            // 存储节点 this 的哈希值
            int ph;
            // 存储 key 比较结果（用于确定遍历方向：<0 向左，>0 向右）
            int dir;
            // 存储当前遍历节点 p 的键（p.key），用于后续与目标键 k 比较
			K pk;
            // 临时存储当前节点的左孩子
            TreeNode<K,V> pl = p.left;
            // 临时存储当前节点的右孩子
            TreeNode<K,V> pr = p.right;
            // 临时存储递归的结果
            TreeNode<K,V> q;
            // 分支1：this.hash > hash(k)，即this 的 key的哈希值 > 目标元素 key 的哈希值
            if ((ph = p.hash) > h)
                // 更新 p 指向的地址，p 指向左孩子节点（红黑树中，哈希值小的节点通常在左侧）
                p = pl;
            // 分支2：this.hash < hash(k)，即this 的 key的哈希值 < 目标元素 key 的哈希值
            else if (ph < h)
                // 更新 p 指向的地址，p 指向右孩子节点（红黑树中，哈希值大的节点通常在右侧）
                p = pr;
            // 分支3：树中存在与参数 k 相同的键（匹配成功）
            else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                // 结束遍历，返回匹配成功的节点
                return p;
            // 分支4：左孩子为 null
            else if (pl == null)
                // 更新指针到右孩子，跳过左孩子
                p = pr;
            // 分支5：右孩子为 null
            else if (pr == null)
                // 更新指针到左孩子，跳过右孩子
                p = pl;
            /**
            * 分支6：这里使用了前面 HashMap01 讲述的红黑树类型校验和安全比较，
            * 其目的通过这两种方法获取红黑树的比较方式，然后更新指针的指向。
            */
            else if ((kc != null ||
                      (kc = comparableClassFor(k)) != null) &&
                     (dir = compareComparables(kc, k, pk)) != 0)
                p = (dir < 0) ? pl : pr;
            // 分支7：到这里基本就是以上情况都经过判断，即哈希值相等、左右孩子均不为 null，且 k 未实现 Comparable 接口（无法通过自然排序确定方向）
            else if ((q = pr.find(h, k, kc)) != null)
                // 通过递归遍历右孩子下的所有节点
                return q;
            // 分支8：右孩子没有找到匹配结果，继续左孩子的遍历
            else
                p = pl;
        } while (p != null);
        // 找不到任何结果
        return null;
    }


    /**
    * 寻找指定参数目标
    * param:
    *		h: 参数 k 的哈希值，一般接受 hash(k)
    * 		k: 指定参数的 key
    * return:
    * 		TreeNode 类型
    */
    final TreeNode<K,V> getTreeNode(int h, Object k) {
        // 这里会判断节点的父节点是否为空（即判断其是否为 root 根节点），然后回调 find() 遍历整棵树寻找目标值 
        return ((parent != null) ? root() : this).find(h, k, null);
    }
    
    /**
    * 红黑树节点比较平局时的决胜方法（又称 HashMap 的兜底排序方法）
    * 该方法主要用于红黑树节点两者之间哈希值相同，但 equals() = false，且二者都没有实现 Comparable 比较器（或者说经过 Comparable 的比较结果为 0），就需要该方法进行兜底排序
    * param:
 	*      a: 待比较的第一个对象（红黑树中的节点 key）
 	*      b: 待比较的第二个对象（红黑树中的节点 key/目标查找 key）
    */
    static int tieBreakOrder(Object a, Object b) {
        // 最终比较结果
        int d;
        /**
        * a == null || b == null => 判断二者是否为 null，为 null 就意味着无法直接排序（实际 HashMap 是不允许 null 存在的）
        * (d = a.getClass().getName().compareTo(b.getClass().getName())) == 0) => 这里获取 a、b 类的全限定名进行字符串比较（其目的是分辨 a、b 是否为相同的类型 key，直接比较类名会报错，所以比较的是类名的字符串）
        */
        if (a == null || b == null ||
            (d = a.getClass().getName().
             compareTo(b.getClass().getName())) == 0)
            // 以上条件有一个满足直接返回 a、b 二者原生内存地址对应的哈希值（即按系统身份哈希值进行比较）
            // System.identityHashCode() 的实现和 Obejct.hashCoed() 的默认实现一样，但是存在重写 hashCode() 的隐患，所以这里直接使用 System.identityHashCode() 来比较
            d = (System.identityHashCode(a) <= System.identityHashCode(b) ?
                 -1 : 1);
        // 这里的 d 不会为 0 ，哪怕 a、b 两者真的存在哈希冲突，也会按照比较结果为 -1 （a<b）去进行排序
        return d;
    }

	/**
    * 红黑树的构建核心方法：将 TreeNode 双向链表转换为符合红黑树规则的自平衡红黑树
    * 注意：此方法执行前，普通 Node 单向链表已被 treeifyBin() 转为 TreeNode 双向链表
    * param:
    *		tab：HashMap 的底层数组（哈希桶），非双向链表！用于最终将红黑树根节点挂载到对应数组索引
    * return:
    *       无（构建完成后通过 moveRootToFront 维护根节点与数组的关联）
    */
    final void treeify(Node<K,V>[] tab) {
        // 定义红黑树的根节点，用于承载最终构建完成的红黑树根节点
        TreeNode<K,V> root = null;
        // 遍历当前 TreeNode 双向链表的所有节点（this 是双向链表的头节点），逐个插入红黑树
        // x：当前遍历的 TreeNode 节点；next：缓存下一个遍历节点（避免插入时链表引用丢失）
        for (TreeNode<K,V> x = this, next; x != null; x = next) {
            next = (TreeNode<K,V>)x.next; // 提前缓存下一个节点，防止后续操作破坏链表引用
            // 将当前节点的左右孩子先置为空，清除原有链表残留引用，为作为红黑树节点做准备
            x.left = x.right = null;
            // 分支1：红黑树尚未初始化（根节点为空），将当前节点作为红黑树的根节点
            if (root == null) {
                x.parent = null; // 根节点没有父节点，符合红黑树特性
                x.red = false;    // 红黑树规则2：根节点必须为黑色（false 代表黑色）
                root = x;         // 赋值根节点，完成红黑树初始化
            }
            // 分支2：红黑树已初始化（根节点不为空），将当前节点插入红黑树对应位置
            else {
                K k = x.key;      // 临时存储当前节点 x 的 key，用于后续节点比较
                int h = x.hash;   // 临时存储当前节点 x 的哈希值，用于优先比较（提升效率）
                Class<?> kc = null; // 缓存 key 的 Class 对象，避免重复反射，优化性能

                // 从红黑树根节点开始遍历，寻找当前节点 x 的插入位置（循环直至找到空位置）
                // p：遍历指针，初始指向红黑树根节点，用于遍历树结构确定插入方向
                for (TreeNode<K,V> p = root;;) {
                    int dir;        // 存储节点比较结果，确定插入方向（<0 左孩子，>0 右孩子）
                    int ph;         // 临时存储当前遍历节点 p 的哈希值
                    K pk = p.key;   // 临时存储当前遍历节点 p 的 key

                    // 子分支1：当前遍历节点 p 的哈希值 > 当前节点 x 的哈希值
                    if ((ph = p.hash) > h)
                        dir = -1; // 标记插入方向为左孩子
                    // 子分支2：当前遍历节点 p 的哈希值 < 当前节点 x 的哈希值
                    else if (ph < h)
                        dir = 1;  // 标记插入方向为右孩子
                    // 子分支3：哈希值相等（哈希冲突），通过 Comparable 接口进行安全比较
                    else if ((kc == null &&
                              (kc = comparableClassFor(k)) == null) || // 校验 key 是否实现 Comparable 接口
                             (dir = compareComparables(kc, k, pk)) == 0) // 安全比较两个 key，获取比较结果
                        // 兜底方案：若未实现 Comparable 或比较结果为 0，调用 tieBreakOrder 确定唯一插入方向
                        dir = tieBreakOrder(k, pk);

                    // 缓存当前遍历节点 p（即将成为当前节点 x 的父节点）
                    TreeNode<K,V> xp = p;
                    // 根据 dir 确定插入位置（左/右孩子），并判断该位置是否为空
                    // 若 p（左/右孩子）为 null，说明找到插入位置，终止内层循环
                    if ((p = (dir <= 0) ? p.left : p.right) == null) {
                        x.parent = xp; // 将当前节点 x 的父节点设置为 xp（确定父子关系）
                        // 根据 dir 方向，将 x 挂载为 xp 的左孩子或右孩子
                        if (dir <= 0)
                            xp.left = x;
                        else
                            xp.right = x;
                        // 核心：插入节点后，红黑树规则可能被破坏，调用 balanceInsertion 修复平衡
                        // 修复后返回新的根节点（可能因旋转导致根节点变化）
                        root = balanceInsertion(root, x);
                        break; // 插入完成，跳出内层循环，继续遍历链表下一个节点
                    }
                    // 若插入位置不为 null，继续内层循环，遍历下一层节点寻找插入位置
                }
            }
        }
        // 核心：将红黑树的根节点移动到 HashMap 数组对应索引的链表头部，确保数组索引指向红黑树根节点
        moveRootToFront(tab, root);
    }
/***** 请看下一节（主要是因为 Typora 的渲染问题，这里分开了） *****/
```

