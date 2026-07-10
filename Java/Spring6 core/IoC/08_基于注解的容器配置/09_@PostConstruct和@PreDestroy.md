# @PostConstruct和@PreDestroy

`@PostConstruct、@PreDestroy` 想必大家并不陌生，在初始化回调、销毁回调的两节简单讲述过， 

对于 Bean 的声明周期开发者有一定的了解，这里补充一下注解的初始化回调是通过哪个类进行完成的。

`@PostConstruct、@PreDestroy` 二者属于 JSR - 250 里面的注解，在 `@Resource` 的学习中，讲述过一个 `BeanPostProcessor` 专门来处理 JSR - 250 的注解——`CommonAnnotationBeanPostProcessor` 。

所以 `@PostConstruct、@PreDestroy` 二者所注释的方法，本质上是通过 `CommonAnnotationBeanPostProcessor` 来完成的。

**这也就是为什么注解声明的初始化回调优先于 XML 文件的初始化回调的原因**。