# String

## String 类型介绍

```js
// 字符串类型
// 开发者想要声明字符串变量需要用到三种符号 ''、""、``，如下：
var a = new String('hello word');
var b = new String("hello");
var c = new String(`word`);

// String 类型和 Number 类型一样，其都是结构体
console.log(a);
console.log(b);
console.log(c);
console.log(typeof(a)); // object
console.log(typeof(b)); // object
console.log(typeof(c)); // object

// 开发者要想真的获取到其字面值变量依然需要用到 valueOf()
console.log(typeof(a.valueOf())); // string
console.log(b.valueOf());
console.log(c.valueOf());
```

## 字符串的格式化输出

```js
// 这里主要讲述一下关于字符串的格式化以及动态运算
var d = new String("I want to compute 1 add 1");
// + 拼接字符串
console.log(d + "，result: 2");
// ${} 运算
console.log("1+1=${1+1}"); // 这里是错误的，开发者如果想要使用插值运算符（${}）则字符串的声明只能使用 ``
console.log(`1+1=${1+1}`);
// 当开发者想要格式化输出时，如下：
var e = new String("Dong");
var f = new String("中国");
console.log(`我是${e}，来自于${f}`);
console.log("我是" + e + "，来自于" + f);

// 除此之外就是一些开发规范，开发规范每一个团对都有自己的开发规范，是具体情况而定
// 在平常书写代码时，我喜欢使用 ""、`` 这两种，如果需要嵌套，个人喜欢使用 ""/`` 嵌套 ''
console.log("'我在单引号里面'");
```

## 常用函数

| 函数 / 方法                      | 核心用途                                   | 语法示例                                              | 输出 / 关键说明                                              |
| -------------------------------- | ------------------------------------------ | ----------------------------------------------------- | ------------------------------------------------------------ |
| `String()`                       | 类型转换（转字符串原始类型）               | `String(123)` / `String(true)`                        | `"123"` / `"true"`（推荐替代 `new String()`，返回原始类型）  |
| `str.length`                     | 获取字符串长度（属性，非函数）             | `"hello".length`                                      | `5`（空格 / 特殊字符也算长度：`"a b".length → 3`）           |
| `str.charAt(index)`              | 获取指定索引的字符                         | `"hello".charAt(1)`                                   | `"e"`（索引从 0 开始；索引越界返回空字符串 `""`）            |
| `str.indexOf(substr, start)`     | 查找子串首次出现的索引                     | `"hello world".indexOf("o")` / `"hello".indexOf("x")` | `4` / `-1`（start 可选，指定起始查找位置；找不到返回 -1）    |
| `str.lastIndexOf(substr)`        | 查找子串最后一次出现的索引                 | `"hello world".lastIndexOf("o")`                      | `7`（从后往前找，找不到返回 -1）                             |
| `str.includes(substr)`           | 判断是否包含指定子串（返回布尔值）         | `"hello".includes("ell")`                             | `true`（ES6 新增，比 indexOf 更直观）                        |
| `str.startsWith(substr)`         | 判断是否以指定子串开头                     | `"hello".startsWith("he")`                            | `true`（ES6 新增，支持第二个参数指定起始位置：`"hello".startsWith("ll", 2) → true`） |
| `str.endsWith(substr)`           | 判断是否以指定子串结尾                     | `"hello".endsWith("lo")`                              | `true`（ES6 新增，支持第二个参数指定前 n 个字符：`"hello".endsWith("ll", 4) → true`） |
| `str.slice(start, end)`          | 截取字符串（支持负索引）                   | `"hello".slice(1, 4)` / `"hello".slice(-2)`           | `"ell"` / `"lo"`（end 可选，截取到 end 前一位；负索引从末尾算：-1 = 最后一位） |
| `str.substring(start, end)`      | 截取字符串（不支持负索引）                 | `"hello".substring(1, 4)` / `"hello".substring(4,1)`  | `"ell"` / `"ell"`（start/end 顺序无关，自动取小值；负索引视为 0） |
| `str.substr(start, length)`      | 截取指定长度的字符串（慎用，部分环境废弃） | `"hello".substr(1, 3)`                                | `"ell"`（start 可负，length 是截取长度；推荐用 slice 替代）  |
| `str.trim()`                     | 去除首尾空格（不含中间）                   | `"  hello world  ".trim()`                            | `"hello world"`（常用：处理表单输入的空格）                  |
| `str.trimStart()`/`trimLeft()`   | 仅去除开头空格                             | `"  hello  ".trimStart()`                             | `"hello  "`                                                  |
| `str.trimEnd()`/`trimRight()`    | 仅去除结尾空格                             | `"  hello  ".trimEnd()`                               | `"  hello"`                                                  |
| `str.toUpperCase()`              | 转大写                                     | `"hello".toUpperCase()`                               | `"HELLO"`                                                    |
| `str.toLowerCase()`              | 转小写                                     | `"HELLO".toLowerCase()`                               | `"hello"`                                                    |
| `str.replace(substr, newStr)`    | 替换首个匹配的子串                         | `"aaa".replace("a", "b")`                             | `"baa"`（默认只替换第一个；替换所有需用正则：`"aaa".replace(/a/g, "b") → "bbb"`） |
| `str.replaceAll(substr, newStr)` | 替换所有匹配的子串（ES2021+）              | `"aaa".replaceAll("a", "b")`                          | `"bbb"`（无需正则，直接替换所有；兼容低版本需用正则 `/g`）   |
| `str.split(separator, limit)`    | 分割字符串为数组                           | `"a,b,c".split(",")` / `"hello".split("", 3)`         | `["a","b","c"]` / `["h","e","l"]`（separator 为 "" 则拆分为单个字符；limit 限制数组长度） |
| `str.repeat(n)`                  | 重复字符串 n 次（ES6 新增）                | `"ab".repeat(3)`                                      | `"ababab"`（n 为非负整数，0 则返回空字符串）                 |
| `str.padStart(length, padStr)`   | 头部补全字符串到指定长度                   | `"123".padStart(5, "0")`                              | `"00123"`（常用：补零，如时间格式化 `("8").padStart(2, "0") → "08"`） |
| `str.padEnd(length, padStr)`     | 尾部补全字符串到指定长度                   | `"123".padEnd(5, "0")`                                | `"12300"`                                                    |

## notice

在文档中，同样提到了，开发者几乎可能不会使用 `String()` 去创建字符串，因为那样创建的只会是字符串结构体对象，并非字符串字面值。

关于常用函数，建议开发者要动手敲一下，不然很难记住。
