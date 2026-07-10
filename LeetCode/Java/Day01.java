import java.util.Arrays;
import java.util.HashMap;

/**
 * @Description 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。
 * @Author Dong
 * @Date 2026/6/28 16:06
 */
public class Day01 {

    /**
     * 暴力破解
     * 开发者可以认为 x+y=C，C 是常量，x、y 均为 nums 数组中的元素
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(1)
     *
     * @param nums 整数数组
     * @param target 整数目标值
     * @return 索引数组
     */
    public static int[] towSum1(int[] nums, int target) {
        int[] result;

        // 先固定 x
        for (int i = 0; i < nums.length; i++) {
            // 遍历数组寻找 y
            for (int j = i+1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target)
                    return new int[]{i, j};
            }
        }

        return new int[0];
    }

    /**
     * 利用 HashMap 查找元素时间复杂度为 O(1) 的特性，减少计算时间复杂度，但相对的空间复杂度会增加变为 O(n)
     *
     * @param nums 整数数组
     * @param target 整数目标值
     * @return 符合条件的索引数组
     */
    public static int[] towSum2(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i]))
                return new int[]{i, map.get(target - nums[i])};

            map.put(nums[i], i);
        }

        return new int[0];
    }

    public static void main(String[] args) {
        int[] nums = new int[]{11, 2, 34, 21, 48, 7, 0, 72, 90, 9};
        int target = 18;

        System.out.println(Arrays.toString(towSum1(nums, target)));

        System.out.println(Arrays.toString(towSum2(nums, target)));
    }
}
