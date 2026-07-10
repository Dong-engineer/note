# ApplicationContextAware、BeanNameAware、BeanPostProcessor三者区别

`ApplicationContextAware` 和 `BeanNameAware` 两者都属于 Spring 的 `Aware` 接口，`Aware` 接口属于 Spring 中的标记接口，一旦 Bean 实现了 `Aware` 及其子类接口，那么 Bean 会感知到其自身所在的容器，并通过容器完成对其它 Bean 的操作。

Spring 对于 `Aware` 的接口本身没有任何定义，但是其子类实现不同的功能，如 `ApplicationContextAware` 可以让 Bean 在其自身内获取容器对象，`BeanNameAware` 则可以让 Bean 获取其自身在容器中的名称，除此之外还有很多的 `Aware` 接口，详情不在一一叙述。

`BeanPostProcessor` 不同于 `Aware` 接口，实现 `BeanPostProcessor` 的接口不会违反 IOC 原则，Bean 也不会感知到容器的存在，且具体触发时机不一样，`BeanPostProcessor` 触发时机在初始化回调前后，这可以更好的让开发者自定义 Bean 在该时间段内需要进行什么操作。

下面从以下几个方面讲述：

1. 设计初衷
2. 执行时机
3. 代码耦合
4. 使用范围

## 设计初衷

`Aware` 体系接口的设计初衷，是为了解决 Bean 对容器的主动依赖，让 Bean 能通过容器进行更加灵活的操作。

`BeanPostProceeos` 的设计初衷，是为了对 Bean 进行增强，`BeanPostProceeos` 接口两个方法发生的时间刚好在 Bean 的初始化回调前后，这是为了 Bean 在使用前后有充足的准备。

## 执行时机

`ApplicationContextAware` 和 `BeanNameAware` 接口触发时间在第一节已经明确说明，发生在 `BeanPostProcessor` 前，Bean 的依赖注入后。

`BeanPostProcessor` 发生时机在 Bean 的初始化回调前后。

## 代码耦合

`Aware` 体系接口的设计初衷就已经决定了 Bean 会与 Spring 代码产生耦合。

`BeanPostProcessor` 并不会与 Spring 代码产生耦合。

## 使用范围

`Aware` 接口只对实现其的 Bean 有效。

`BeanPostProcessor` 对全局 Bean 有效（可以过滤 Bean，即让 `BeanPostProcessor` 只对部分 Bean 有效）。

