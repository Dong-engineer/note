# Number

## Number 类型介绍

在 Google 和 MDN 的文档中说到，开发者几乎没有任何可能性去使用 `Number` 关键字去用作构造函数，因为这样做会创建一个数字对象而并非字面的数字值。

```js
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
```

## Number 的运算

| 运算符 | 介绍 |
| ------ | ---- |
| +      | 加   |
| -      | 减   |
| *      | 乘   |
| /      | 除   |

```js
// 开发者可以对 Number 类型进行数学计算
// + - * /
console.log(a + b);
console.log(b - a);
console.log(a * b);
console.log(b / e); // Infinity -> 无穷大，因为分母为 0 所以结果为 Infinity（前面讲述过 JS 是区分大小写的，所以没有 infinity 等同于 Ininity 这一说，除非开发者自定义 infinity 变量然后将 Infinity 赋值给其变量）
```

| 运算符 | 介绍 |
| ------ | ---- |
| ++     | 自增 |
| --     | 自减 |
| **     | 幂   |
| %      | 取余 |

```js
// ++ -- ** %
console.log(a++); // 自增（先赋值后运算）
console.log(b--); // 自减
console.log(a**2); // 2 次幂
console.log(b % 3); // 取余
console.log(++a); // 自增（先运算后赋值）
console.log(--b); // 自减
```

| 运算符 | 介绍     |
| ------ | -------- |
| +=     | 加法分配 |
| -=     | 减法分配 |
| *=     | 乘法分配 |
| /=     | 除法分配 |

```js
// +=  -=  *=  /=  **=  %=
console.log(a += 1); // a = a + 1
console.log(b -= 1); // b = b - 1
console.log(a *= 2); // a = a * 2
console.log(b /= 1); // b = b / 1
console.log(a **= 1); // a = a ** 1
console.log(a %= 2); // a = a % 2
```

## 常用函数

| 函数 / 方法           | 核心用途                                  | 语法示例                                                 | 输出 / 说明                                                  |
| --------------------- | ----------------------------------------- | -------------------------------------------------------- | ------------------------------------------------------------ |
| `Number()`            | 类型转换（转数字原始类型）                | `Number("123")` / `Number(false)`                        | `123` / `0`（推荐替代 `new Number()`，返回原始类型而非对象） |
| `Number.parseInt()`   | 字符串转整数（支持进制）                  | `Number.parseInt("123.99")` / `Number.parseInt("10", 2)` | `123` / `2`（第二个参数是进制，默认 10；忽略小数部分）       |
| `Number.parseFloat()` | 字符串转浮点数                            | `Number.parseFloat("123.99abc")`                         | `123.99`（解析到非数字字符停止，仅识别第一个小数点）         |
| `Number.isInteger()`  | 判断是否为整数（原始类型）                | `Number.isInteger(123)` / `Number.isInteger(123.0)`      | `true` / `true`（123.0 本质是整数）                          |
| `Number.isNaN()`      | 精准判断是否为 NaN（避免 NaN !== NaN 坑） | `Number.isNaN(NaN)` / `Number.isNaN("123")`              | `true` / `false`（优于全局 `isNaN()`，不会先转换类型）       |
| `Number.isFinite()`   | 判断是否为有限数字                        | `Number.isFinite(123)` / `Number.isFinite(Infinity)`     | `true` / `false`（排除 NaN/Infinity/-Infinity）              |
| `Math.abs()`          | 取绝对值                                  | `Math.abs(-123)`                                         | `123`                                                        |
| `Math.round()`        | 四舍五入取整                              | `Math.round(123.4)` / `Math.round(123.5)`                | `123` / `124`                                                |
| `Math.ceil()`         | 向上取整（天花板）                        | `Math.ceil(123.1)` / `Math.ceil(-123.1)`                 | `124` / `-123`                                               |
| `Math.floor()`        | 向下取整（地板）                          | `Math.floor(123.9)` / `Math.floor(-123.9)`               | `123` / `-124`                                               |
| `Math.random()`       | 生成 0~1 随机数（不包含 1）               | `Math.random()`                                          | 如 `0.4567`（常用：`Math.floor(Math.random()*100)` 生成 0~99 随机整数） |
| `Math.max()`          | 取多个数的最大值                          | `Math.max(1, 2, 3)`                                      | `3`（支持任意多个参数）                                      |
| `Math.min()`          | 取多个数的最小值                          | `Math.min(1, 2, 3)`                                      | `1`                                                          |
| `Math.pow()`          | 幂运算（等同于 `**`）                     | `Math.pow(2, 3)`                                         | `8`（2 的 3 次方，和 `2**3` 效果一致）                       |
| `num.toFixed(n)`      | 保留 n 位小数（返回字符串）               | `(123.456).toFixed(2)`                                   | `"123.46"`（会四舍五入；n 范围 0~20，超出报错）              |
| `num.toPrecision(n)`  | 保留 n 位有效数字（返回字符串）           | `(123.456).toPrecision(3)`                               | `"123"`（四舍五入，n 范围 1~21）                             |

## notice

正如本节开头所说，开发者在开发时可能很少回去使用 Number 去构建对象，大多数的时候，开发者需要使用的是原始类型 `number`。

而 `Number` 类型的计算实际上也就是 `number` 类型的计算。

到这里其实一直留有一个疑问（对于新手来讲），如果开发者直接将字面值数字赋值给变量那么其类型是什么样的呢？（开发者可以自行尝试一下）