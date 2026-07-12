# @Inject 和 @Named

## @Inject

`@Inject` 自动装配，和 `@Autowired` 一样。

`@Inject` 可以使用再方法，类、字段、参数上。

同样地， `@Inject` 适配了 `java.util.Optional` 和 `@Nullable` 。

经过 `@Autowired` 的学习，开发者知道，如果自动注入的依赖为 `null` 的话是会发生 `NullPointException` ，所以这时就需要使用 `java.util.Optional` 或 `@Nullable` 使得注入的依赖值是 `Optionl` 的泛型类型，避免发生 `NullPointExeption` 。

而 `@Inject` 更适合以上的做法因为，`@Inject` 没有 `require` 属性。

对于 `@Autowired` 开发者知道，如果某个自动注入并非是必须的自动注入，那么可以使用 `require=false` 跳过自动注入。

## @Named

`@Named` 和 `@Qualifier` 一样的作用，都是设置限定符，从而达到能够更加精确的依赖注入。

## 注意事项

以上注解的注意事项均于 Spring 注解注意事项相同。