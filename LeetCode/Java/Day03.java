/**
 * @Description 无重复字符的最长字符串：给定一个字符串 s ，请你找出其中不含有重复字符的最长子串的长度。
 * @Author Dong
 * @Date 2026/7/13 21:26
 */
public class Day03 {

    /**
     * 无重复字符的最长字符串
     *
     * @param s 目标字符串
     * @return 最长子串的长度
     */
    public static int lengthOfLongestSubstring(String s) {
//        // 字符数组
//        char[] chars = s.toCharArray();
//
//        // 结果
//        int result = 0;
//
//        for (int i = 0; i < s.length() - 1; i++) {
//            int j = i + 1;
//
//            while (j < s.length() - 1) {
//                if (chars[i] == chars[j]) {
//                    result = Math.max(j - i, result);
//                    break;
//                }
//
//                j++;
//
//                if (j == s.length() - 1)
//                    return j - i;
//            }
//        }
//
        return 0;
    }

    public static void main(String[] args) {
        /*
         * 示例 1:
         * 输入: s = "abcabcbb"
         * 输出: 3
         * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。注意 "bca" 和 "cab" 也是正确答案。
         */
        /*
         * 示例 2:
         * 输入: s = "bbbbb"
         * 输出: 1
         * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
         */
        System.out.println(lengthOfLongestSubstring("abcabcbb"));


    }
}
