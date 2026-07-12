import java.util.LinkedList;
import java.util.NavigableMap;

/**
 * @Description 两数之和：给你两个非空的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，并且每个节点只能存储一位数。
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * @Author Dong
 * @Date 2026/6/29 20:53
 */
public class Day02 {

    /**
     * 该函数仅并不符合 LeetCode 原题题意
     * @param one 第一个整数
     * @param tow 第二个整数
     * @return one + tow 的结果
     */
    public static LinkedList<Integer> addTwoNumbers2(LinkedList<Integer> one, LinkedList<Integer> tow) {
        // 计算次数
        int count = Math.min(one.size(), tow.size());
        // 进位
        int carry = 0;
        // 结果集合
        LinkedList<Integer> result = one.size() <= tow.size() ? one : tow;

        for (int i = 0; i < count; i++) {
            int lastResult = one.get(i) + tow.get(i) + carry;
            result.set(i, lastResult % 10);
            carry = lastResult / 10 >= 1 ? lastResult / 10 : 0;
        }

        return result;
    }

    public static void main(String[] args) {
        LinkedList<Integer> one = new LinkedList<>();
        one.add(2);
        one.add(4);
        one.add(3);

        LinkedList<Integer> tow = new LinkedList<>();
        tow.add(5);
        tow.add(6);
        tow.add(4);

        addTwoNumbers2(one, tow).forEach(System.out::println);
    }
}
