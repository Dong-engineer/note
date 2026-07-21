package Java_Protogenetic_API.Collection.Set;

import Java_Protogenetic_API.Collection.User;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName Test08
 * @Description Set 的唯一性由 hashCod()、equals()决定验证
 * @Author 86198
 * @DATE 2025/10/22 16:16
 * @Version 1.0
 */
public class Test02 {
    public static void main(String[] args) {

        User user1 = new User("Dong");
        User user2 = new User("Dong");

        Set<User> set = new HashSet<>();
        set.add(user1);
        set.add(user2);

        System.out.println(set);

    }
}
