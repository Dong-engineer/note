# 注解和 XML 的区别

## 注解

基于注解的开发，其元数据是与 “类定义” 绑定在一起的。

具体如何解释呢，下面给个示例：

```java
@Service
@Qualifier("userService")
@Scope("prototype")
public class UserService {
	
}
```

当开发者开启扫描后，并对类 `UserService` 实例化时，那么所有的 `UserService` Bean 的限定符都是 `userService` 。

这可以理解为注解的局限性，当开发者将其作为候选对象时，且其 `scope` 为 `prototype` 多例 Bean 。在进行 Bean 的示例化时，开发者每次实例化的对象都是不同的，但其限定符是不会变的，依然是 `userService`。

这种情况下，即使是多例 Bean，也无法通过注解为同一类的不同实例设置不同的限定符 —— 这正是注解元数据 "按类绑定" 的特性导致的。如果需要为同一类的不同 prototype 实例设置不同的限定符，就必须借助 XML 配置（为每个实例单独定义元数据），或者在 `@Bean` 方法上单独标注（每个方法对应一个实例，通过方法级注解区分）。

## XML

基于 XML 文件的配置则没有上述情况，除非开发者没有明确使用 Bean 的属性或者 XML 标签。

开发者完全可以通过手动配置，为不同 Bean 使用不同的限定符或名称（id/name）。