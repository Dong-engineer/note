package org.dong;

import org.dong.Beans.User;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @ClassName Study01
 * @Description 第一个 Spring 程序
 * @Author Dong
 * @Date 2026/2/14 12:45
 * @Version 1.0
 */
public class Study01 {
    public static void main(String[] args) {

        // 容器初始化时所有的 Bean 都会被 Spring 完成加工，而开发者只需要通过 getBean() 来获取即可
        // 这就是 Spring 的工厂思想的体现
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("Study01.xml");

        User user = applicationContext.getBean("user", User.class);
        User user1 = applicationContext.getBean("user-1", User.class);
        User user2 = applicationContext.getBean("user-2", User.class);

        System.out.println(user);
        System.out.println(user1);
        System.out.println(user2);
    }
}
