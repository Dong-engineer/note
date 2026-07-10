# Phased、Ordered的区别

`Phased` 和 `Ordered` 二者虽然都是决定了 Bean 的生命周期先后执行的顺序，但是通过不同方式决定的，在实际开发中，通常二者相互搭配使用。

下面分开讲述。

## Phased

`Phased` 用于划分 Bean 的生命周期，也就是说 `getPhase()` 返回的值决定了不同 Bean 各自生命周期执行的先后顺序。

通俗来讲 `Phased` 更像是对 Bean 进行划分组，`phase` 值相同的一组。

## Ordered

`Ordered` 用于决定同一生命周期阶段内不同 Bean 的执行先后顺序。

通俗来讲 `Ordered` 更像是组内的执行顺序，`order` 值越小越优先执行。

## 具体案例

假设现有：

- Bean A：`phase=1`，`order=2`
- Bean B：`phase=2`，`order=1`

那么执行情况如下：

Bean A 的 `order=2` 只在 `phase=1` 内部生效（假设 `phase=1` 还有其他 Bean，比如 Bean C `order=1`，则 C 会在 A 之前执行初始化）。

Bean B 的 `order=1` 只在 `phase=2` 内部生效（假设 `phase=2` 还有 Bean D `order=2`，则 B 会在 D 之前执行初始化）。

由于 `phase=1 < phase=2`，无论 A 和 B 的 `order` 是多少，A 的初始化一定在 B 的初始化之前（因为分属不同阶段）。

## 总结

在 *09_BeanPostProcessor* 一节中讲述到 `BeanPostProcessor` 只会受到 `Ordered` 的影响，这是因为 `BeanPostProcessor` 本身就是 Bean 生命周期的一个阶段处理器，所以 `BeanPostProcessor` 的执行顺序只受 `Ordered` 影响。

那么根据以上的解释可以猜测 `Ordered` 对其它的生命阶段同样有影响，是的没错，`Ordered` 对其他生命阶段中 Bean 执行的顺序同样有影响。

在实际开发中通常二者搭配使用，能更加灵活的控制 Bean 执行的顺序。且二者都是**值越小越优先，关闭越靠后**。