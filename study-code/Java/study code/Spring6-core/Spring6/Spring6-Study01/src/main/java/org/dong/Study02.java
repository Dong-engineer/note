package org.dong;

import org.dong.Beans.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @ClassName Study02
 * @Description TODO
 * @Author Dong
 * @Date 2026/2/15 22:56
 * @Version 1.0
 */
public class Study02 {
    public static void main(String[] args) {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("Study02.xml");

//        User JQK = applicationContext.getBean("JQK", User.class);
//        User JQK = applicationContext.getBean("JQD", User.class);
//        User JQK = applicationContext.getBean("Dong", User.class);
        User JQK = applicationContext.getBean("JQDong", User.class);
        System.out.println(JQK);
    }
}
