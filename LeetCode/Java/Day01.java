import java.util.Arrays;

/**
 * @Description 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。
 * @Author Dong
 * @Date 2026/6/28 16:06
 */
public class Day01 {

    /**
     * 暴力破解
     * 开发者可以认为 x+y=C，C 是常量，x、y 均为 nums 数组中的元素
     *
     * @param nums 整数数组
     * @param target 整数目标值
     */
    public static void bruteForce(int[] nums, int target) {
        int x;
        int y;

        // 先固定 x
        for (int i = 0; i < nums.length; i++) {
            x = nums[i];
            // 遍历数组寻找 y
            for (int j = i+1; j < nums.length; j++) {
                y = nums[j];
                int[] ij = new int[]{i, j};
                if (x +y == target)
                    System.out.println(Arrays.toString(ij));
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[]{11, 2, 34, 21, 48, 7, 0, 72, 90, 9};
        int target = 18;

        bruteForce(nums, target);
    }
}
