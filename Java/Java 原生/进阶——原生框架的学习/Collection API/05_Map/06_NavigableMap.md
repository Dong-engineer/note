# NavigableMap

`NavigableMap` 继承 `SortedMap` ，`NaivgableMap` 在 `SortedMap` 上增加了大量区间、邻近、逆序、弹出操作。

本节学习内容：

1. 熟悉并掌握 `NavigableMap` 方法即可。

## 源码

```java
public interface NavigableMap<K,V> extends SortedMap<K,V> {

    /**
	 * 获取小于等于指定key的键值对
	 * @param key 参照键
	 * @return 存在则返回对应Entry，不存在返回null
	 */
    Map.Entry<K,V> floorEntry(K key);

    /**
	 * 获取小于等于指定key的键
	 * @param key 参照键
	 * @return 存在则返回对应key，不存在返回null
	 */
    K floorKey(K key);

    /**
	 * 获取大于等于指定key的键值对
	 * @param key 参照键
	 * @return 存在则返回对应Entry，不存在返回null
	 */
    Map.Entry<K,V> ceilingEntry(K key);

    /**
	 * 获取大于等于指定key的键
	 * @param key 参照键
	 * @return 存在则返回对应key，不存在返回null
	 */
    K ceilingKey(K key);

    /**
	 * 获取严格大于指定key的键值对（不包含等于）
	 * @param key 参照键
	 * @return 存在则返回对应Entry，不存在返回null
	 */
    Map.Entry<K,V> higherEntry(K key);

    /**
	 * 获取严格大于指定key的键（不包含等于）
	 * @param key 参照键
	 * @return 存在则返回对应key，不存在返回null
	 */
    K higherKey(K key);

    /**
	 * 获取Map中最小key对应的键值对（第一个元素）
	 * @return 最小key的Entry，空Map返回null
	 */
    Map.Entry<K,V> firstEntry();

    /**
	 * 获取Map中最大key对应的键值对（最后一个元素）
	 * @return 最大key的Entry，空Map返回null
	 */
    Map.Entry<K,V> lastEntry();

    /**
	 * 获取并移除最小key对应的键值对
	 * @return 被移除的最小Entry，空Map返回null
	 */
    Map.Entry<K,V> pollFirstEntry();

    /**
	 * 获取并移除最大key对应的键值对
	 * @return 被移除的最大Entry，空Map返回null
	 */
    Map.Entry<K,V> pollLastEntry();

    /**
	 * 返回逆序视图的NavigableMap，key排序与原Map相反
	 * @return 倒序可导航Map视图，原Map修改会同步反映到此视图
	 */
    NavigableMap<K,V> descendingMap();

    /**
	 * 返回可导航的key集合视图，支持上下限、截取、逆序等操作
	 * @return NavigableSet格式的key集合，绑定原Map
	 */
    NavigableSet<K> navigableKeySet();

    /**
	 * 返回逆序的可导航key集合视图
	 * @return 倒序key集合，绑定原Map
	 */
    NavigableSet<K> descendingKeySet();

    /**
	 * 截取区间子Map，可自由控制左右边界是否包含
	 * @param fromKey 区间起始键
	 * @param fromInclusive 是否包含起始键 true包含/false不包含
 	 * @param toKey 区间结束键
 	 * @param toInclusive 是否包含结束键 true包含/false不包含
	 * @return 指定区间的NavigableMap视图
	 */
    NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive);

    /**
	 * 截取头部子Map：所有小于（或小于等于）toKey的元素
	 * @param toKey 截断边界键
	 * @param inclusive 是否包含toKey true包含/false不包含
	 * @return 头部区间NavigableMap视图
	 */
    NavigableMap<K,V> headMap(K toKey, boolean inclusive);

    /**
	 * 截取尾部子Map：所有大于（或大于等于）fromKey的元素
 	 * @param fromKey 截断边界键
 	 * @param inclusive 是否包含fromKey true包含/false不包含
	 * @return 尾部区间NavigableMap视图
	 */
    NavigableMap<K,V> tailMap(K fromKey, boolean inclusive);

    /**
	 * 截取区间子Map，默认左闭右开 [fromKey, toKey)
	 * @param fromKey 起始键（包含）
	 * @param toKey 结束键（不包含）
 	 * @return 标准SortedMap区间视图
 	 */
    SortedMap<K,V> subMap(K fromKey, K toKey);

    /**
  	 * 截取头部子Map，默认不包含toKey，所有key < toKey
	 * @param toKey 截断边界键（不包含）
	 * @return 头部SortedMap视图
	 */
    SortedMap<K,V> headMap(K toKey);

    /**
	 * 截取尾部子Map，默认包含fromKey，所有key >= fromKey
	 * @param fromKey 截断边界键（包含）
	 * @return 尾部SortedMap视图
	 */
    SortedMap<K,V> tailMap(K fromKey);
}
```

## 注意事项

请参见 *05_SortedMap* 一节。
