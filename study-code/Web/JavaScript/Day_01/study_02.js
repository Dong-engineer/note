/**
 * 在 JS 中，一共有 7 中基本数据类型变量，
 * 1.Boolean    布尔类型
 * 2.null       空值类型（null 指的是变量被明确声明定义为 null）
 * 3.undefind   未定义值类型（undefind 并不和 null 相同，undefind 是定义了变量，但是并未被赋值）
 * 4.Number     数字类型（包括整数或浮点数）
 * 5.BigInt     任意精度的整数
 * 6.String     字符串类型
 * 7.Symbol     实例是唯一且不可变的数据类型
 */

// 这里注意，不可以使用基本数据类型关键字直接声明变量，这样是错误的
// Number a = new Number();

// 这里先介绍 Number 的构造函数
var a = new Number(5);
// 开发者可以从控制台看到，打印出的信息实际上并不是字面数值 5 ，而是一个结构对象
console.log(a);
console.log(typeof(a)); // object

// 如果开发者想要打印输出 Number 对象的字面数值，则可以如下
console.log(a.valueOf());
// 这里开发者可以在控制台看见 number 
// 这是因为在 JS 中数字字面类型变量值类型为 number 类型并非 Number
console.log(typeof(a.valueOf()));

// Number 类型与其他类型的转换
// 开发者可以在 Number 的构造函数中传递非 number 类型的参数，如下：
var b = new Number("6"); // String 类型
console.log(b);
console.log(b.valueOf());
var c = new Number("NaN"); // 这是 Number 类型中的一个特殊的值，所以这里可以转化成功
console.log(c);
console.log(c.valueOf()); 
// NaN ，Number 类型的 NaN 值表示创建 Number 结构体时的错误值，
// 即开发者试图将 undefind、null 或非数字字符串进行了 Number 类型的计算或转化，且结果是 Number 类型的
// 那么结果就会使 NaN 或者抛出异常
var d = new Number("haha123");
console.log(d); // NaN

console.log(NaN === NaN); // false
console.log(Number.isNaN(NaN)); // true（正确判断 NaN 的方式）


var e = new Number(false); // Boolean 类型，false 为 0，true 为 1
console.log(e);
console.log(e.valueOf());

// 浮点数
// JS 在存储数字时会将任何数字都去转化为浮点数，然后存储至内存中
var f = new Number(3.1415926);
console.log(f);
console.log(typeof(f.valueOf()));   // number

/* ============================== 计算 ============================ */
console.log("Number 计算");
// 开发者可以对 Number 类型进行数学计算
// + - * /
console.log(a + b);
console.log(b - a);
console.log(a * b);
// Infinity -> 无穷大，因为分母为 0 所以结果为 Infinity（前面讲述过 JS 是区分大小写的，
// 所以没有 infinity 等同于 Ininity 这一说，除非开发者自定义 infinity 变量然后将 Infinity 赋值给其变量）
console.log(b / e); // Infinity

// ++ -- ** %
console.log(a++); // 自增
console.log(b--); // 自减
console.log(a**2); // 2 次幂
console.log(b % 3); // 取余

// += -= *= /= **= %=
console.log(a += 1); // a = a + 1
console.log(b -= 1); // b = b - 1
console.log(a *= 2); // a = a * 2
console.log(b /= 1); // b = b / 1
console.log(a **= 1); // a = a ** 1
console.log(a %= 2); // a = a % 2
