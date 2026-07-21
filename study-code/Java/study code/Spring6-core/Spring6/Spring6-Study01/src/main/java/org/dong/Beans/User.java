package org.dong.Beans;

import java.util.Objects;

/**
 * @ClassName User
 * @Description User 类型
 * @Author Dong
 * @Date 2026/2/14 12:47
 * @Version 1.0
 */
public class User {

    private String name;
    private int age;
    private String address;

    public User() {
        System.out.println("无参构造执行");
    }

    public User(String name, int age, String address) {
        System.out.println("有参构造执行");
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("我是 setter 方法，我被执行了");
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        System.out.println("我是 setter 方法，我被执行了");
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        System.out.println("我是 setter 方法，我被执行了");
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (age != user.age) return false;
        if (!Objects.equals(name, user.name)) return false;
        return Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + age;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}
