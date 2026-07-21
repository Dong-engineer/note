# Boolean

## Boolean 类型介绍

```js
// 布尔类型分为字面量布尔值与 Boolean 包装对象两种
// 构造函数创建布尔对象，使用 new Boolean()
var a = new Boolean(true);
var b = new Boolean(false);
console.log(a);
console.log(b);
console.log(typeof(a)); // object
console.log(typeof(b)); // object
// 想要获取布尔原始字面值，需要调用 valueOf()
console.log(typeof(a.valueOf())); // boolean
console.log(a.valueOf());
console.log(b.valueOf());

// 日常开发直接使用字面量 true / false（原始boolean，推荐）
let flag1 = true;
let flag2 = false;
console.log(typeof(flag1)); // boolean
console.log(typeof(flag2)); // boolean
```

## 布尔类型转换规则

任意值都可以转为布尔值，分为**真值 (truthy)** 和**假值 (falsy)**

```js
// 使用 Boolean() 函数可将任意值转为原始布尔类型（推荐，不生成对象）
console.log(Boolean(1)); // true
console.log(Boolean(0)); // false
console.log(Boolean(-100)); // true
console.log(Boolean("hello")); // true
console.log(Boolean("")); // false
console.log(Boolean(null)); // false
console.log(Boolean(undefined)); // false
console.log(Boolean(NaN)); // false
console.log(Boolean([])); // true
console.log(Boolean({})); // true

// new Boolean 构造函数转换，返回对象，不推荐业务使用
var c = new Boolean("0");
console.log(c.valueOf()); // true
var d = new Boolean("");
console.log(d.valueOf()); // false
```

### 六大假值（转为布尔均为 false）

`0`、`""`、`null`、`undefined`、`NaN`、`0n` 其余所有值全部为真值。

```js
// 快速判断是否为假值
function isFalsy(val) {
  return !Boolean(val);
}

console.log(isFalsy(NaN)); // true
console.log(isFalsy("hello")); // false
```

## 布尔逻辑运算

| 运算符 | 介绍                       |      |                              |
| ------ | -------------------------- | ---- | ---------------------------- |
| `&&`   | 逻辑与，两边全为真才返回真 |      |                              |
| `||`    | 逻辑或，任意一边为真则返回真 |||
| `!`    | 逻辑非，取反               |      |                              |

```js
// 逻辑与 &&
console.log(true && true); // true
console.log(true && false); // false
console.log(false && true); // false

// 逻辑或 ||
console.log(true || false); // true
console.log(false || false); // false

// 逻辑非 !
console.log(!true); // false
console.log(!false); // true
console.log(!0); // true
console.log(!""); // true

// 双 !! 快速转布尔（常用简写）
console.log(!!"abc"); // true
console.log(!!0); // false
```

### 短路特性

```js
// && 短路：左侧为假，直接返回左侧，不执行右侧
let x = 0;
console.log(x && x++); // 0，x不会自增
console.log(x); // 0

// || 短路：左侧为真，直接返回左侧，不执行右侧
let y = 10;
console.log(y || y++); // 10，y不会自增
console.log(y); // 10
```

## 常用函数 / 方法

| 函数 / 方法      | 核心用途                        | 语法示例                       | 输出 / 关键说明                                              |
| ---------------- | ------------------------------- | ------------------------------ | ------------------------------------------------------------ |
| `Boolean()`      | 类型转换（转原始布尔值，推荐）  | `Boolean(123)` / `Boolean("")` | `true` / `false`；替代 `new Boolean()`，返回基础类型而非对象 |
| `bool.valueOf()` | 获取 Boolean 包装对象原始布尔值 | `new Boolean(1).valueOf()`     | `true`；`typeof` 结果为 `boolean`                            |
| `!val`           | 单次取反，得到相反布尔          | `!123` / `!""`                 | `false` / `true`                                             |
| `!!val`          | 强制转为标准布尔值（简写）      | `!!null` / `!!"test"`          | `false` / `true`                                             |

## notice

实际业务开发中几乎不会使用 `new Boolean()` 创建布尔包装对象，该方式生成的是 object 类型，容易引发判断异常。 日常操作布尔值优先使用字面量 `true` / `false`，类型转换统一使用 `Boolean()` 或 `!!` 简写。 逻辑运算短路特性在判断、赋值、函数调用场景高频使用，需要重点理解。