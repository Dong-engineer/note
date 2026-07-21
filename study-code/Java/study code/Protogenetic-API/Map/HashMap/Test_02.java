package Java_Protogenetic_API.Map.HashMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Test_02
 * @Description TODO
 * @Author Dong
 * @Date 2026/3/30 16:19
 * @Version 1.0
 */
public class Test_02 {
    public static void main(String[] args) {
        Map<String, Integer> a = new HashMap<>();
        Map<String, String> b = new HashMap<>();
        a.put("one", 1);
        a.put("two", 2);
        b.put("one", "1");
        b.put("two", "2");
        b.put("t", "3");

        System.out.println(b.keySet());
    }
}
