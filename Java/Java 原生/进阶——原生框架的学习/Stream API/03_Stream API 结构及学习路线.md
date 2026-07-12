# Stream API 结构及学习路线

`Stream API` 结构，开发者对 `Stream API` 类的结构有一个基础认识。

```text
java.util.stream
├─ BaseStream<T,S extends BaseStream> 顶层父接口（所有流根接口）
│  ├─ Stream<T> 引用类型流（最常用）
│  ├─ IntStream / LongStream / DoubleStream 基本数值流（避免装箱拆箱）
```

除了以上内容外开发者还将会学习一些：

1. 收集器（将流的的结果重新组装为 `Collention`）

2. 中间操作

   - 过滤类：`filter()`
   - 映射转换：`map()、flatMap()、mapToInt/Long/Double()`

   - 去重排序：`distinct()、sorted()`

   - 截取跳过：`limit()、skip()`

   - 调试：`peek()`

3. 终端操作

   - 遍历：`forEach /forEachOrdered`
   - 归约：`reduce`（自定义聚合逻辑）
   - 匹配查找：`anyMatch`、`allMatch`、`findFirst`、`findAny`
   - 收集转换：`collect()`
   - 转数组：`toArray()`

```
├─ 收集器：Collector、Collectors（终端收集结果）
│  ├─ 并行工具：StreamSupport（创建并行流）
├─ 中间操作（返回新Stream，惰性求值）
│  ├─ 无状态：filter、map、flatMap、peek、unordered
│  └─ 有状态：sorted、distinct、limit、skip
└─ 终端操作（触发计算，关闭流，只能执行一次）
   ├─ 短路终端：anyMatch、allMatch、noneMatch、findFirst、findAny
   └─ 非短路终端：collect、forEach、count、max、min、reduce、toArray
```

