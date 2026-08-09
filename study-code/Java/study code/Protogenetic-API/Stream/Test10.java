package Java_Protogenetic_API.Stream;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 流的中间操作
 * @Author Dong
 * @Date 2026/8/4 21:29
 */
public class Test10 {

    static class MenuItem implements Comparable<MenuItem> {
        String name;    // 菜单名称
        Number price;    // 价格

        public MenuItem() {}

        public MenuItem(String name, Number price) {
            this.name = name;
            this.price = price;
        }

        @Override
        public String toString() {
            return  "名称：" + this.name + "|" + "价格：" + this.price;
        }

        @Override
        public int compareTo(MenuItem other) {
            return Double.compare(this.price.doubleValue(), other.price.doubleValue());
        }
    }

    static class Order {
        Long number;    // 订单编号
        String good;    // 商品
        String payer;   // 付款人
        String payee;   // 收款人

        public Order() {}

        public Order(Long number, String payer, String good, String payee) {
            this.number = number;
            this.good = good;
            this.payer = payer;
            this.payee = payee;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Order order = (Order) o;

            if (!this.number.equals(order.number)) return false;
            if (!this.good.equals(order.good)) return false;
            return this.payee.equals(order.payee);
        }

        @Override
        public int hashCode() {
            int result = this.number.hashCode();
            result = 31 * result + this.good.hashCode();
            result = 31 * result + this.payee.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "=== 咖啡订单 ===" + "\n" +
                    "订单编号：" + this.number + "\n" +
                    "商品名称：" + this.good + "\n" +
                    "付款人：" + this.payer + "\n" +
                    "收款人：" + this.payee;
        }
    }

    /**
     * 通过流的映射操作，返回一个更加规范且清晰的菜单表
     * @param menuItems 菜单项集合
     * @return 纯字符菜单表
     */
    public static List<String> showMenu(List<MenuItem> menuItems) {
        return menuItems
                .stream()
                .map(menuItem -> "名称：" + menuItem.name + "|" + "价格：" + menuItem.price)
                .collect(Collectors.toList());
    }

    /**
     * 去除重复的订单
     * @param orders 订单列表
     * @return 无重复的订单列表
     */
    public static List<Order> remDuplication(List<Order> orders) {
        return orders
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 筛选菜单，筛选出 <= price 的咖啡商品
     * @param price 价格阈值
     * @return 符合条件的菜单项
     */
    public static List<String> filterMenu(List<MenuItem> menuItems, Number price) {
        return menuItems
                .stream()
                .filter(menuItem -> menuItem.price.doubleValue() <= price.doubleValue())
                .map(MenuItem::toString)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        /* 各种咖啡选项 */
        MenuItem americano = new MenuItem("Caffè Americano", 23.00);
        MenuItem latte = new MenuItem("Caffè Latte", 28.00);
        MenuItem cappuccino = new MenuItem("Cappuccino", 28.00);
        MenuItem mocha = new MenuItem("Caffè Mocha", 32.00);
        MenuItem macchiato = new MenuItem("Caramel Macchiato", 33.00);
        MenuItem espresso = new MenuItem("Espresso", 18.00);
        // 咖啡菜单
        List<MenuItem> menuItems = List.of(americano, latte, cappuccino, mocha, macchiato, espresso);

        // 展现菜单
        showMenu(menuItems).forEach(System.out::println);

        // 筛选菜单
        System.out.println(">>> 过滤后的菜单 <<<");
        filterMenu(menuItems, 30).forEach(System.out::println);

        // 菜单排序，按价格由低到高进行排序
        System.out.println(">>> 排序后的菜单 <<<");
        menuItems.stream().sorted().toList().forEach(System.out::println);

        /* 顾客过滤菜单后，开始进行下单操作 */
        Order order1 = new Order(202608092112L, "customer1","Caffè Latte", "Dong Coffee");
        Order order2 = new Order(202608092122L, "customer2","Espresso", "Dong Coffee");
        // 顾客在同一时间重复下单（这里假设是失误操作，那么后续开发者需要对这一类型的订单进行筛选操作）
        Order order3 = new Order(202608092122L, "customer2","Espresso", "Dong Coffee");

        // 订单列表
        List<Order> orders = List.of(order1, order2, order3);
        System.out.println(">>> 订单列表 <<<");
        orders.forEach(System.out::println);

        // 去重处理（将重复订单去除）
        System.out.println(">>> 清洗后的订单 <<<");
        remDuplication(orders).forEach(System.out::println);

        //
    }
}
