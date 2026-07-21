# BigInt

BigInt 是 JavaScript 较新新增的基础类型，支持对超出 Number 安全范围的数值进行数学运算。 创建 BigInt 有两种方式：数字字面量末尾追加 `n`，或调用 `BigInt()` 并传入整数 / 数字字符串。

```js
const myNumber = 9999999999999999;
const myBigInt = 9999999999999999n;

typeof myNumber;
// "number"

typeof myBigInt;
// "bigint"

myNumber;
// 10000000000000000

myBigInt;
// 9999999999999999n
```

示例中 `9999999999999999` 超出了 Number 可安全存储的数值范围，因此出现精度丢失、四舍五入错误。

BigInt 无法调用 Number 身上的方法与属性，也不能使用内置 Math 对象的各类数学方法。 最关键的规则：**常规算术运算中，不能混用 BigInt 和 Number 原始值**。

```js
9999999999999999n + 5
// 抛出错误：Uncaught TypeError: can't convert BigInt to number
```

若要使用 BigInt 做运算，参与计算的两边都必须是 BigInt 类型：

```js
console.log( 9999999999999999 + 10 );  // 精度出错
// 10000000000000010

console.log( 9999999999999999n + 10n );
// 10000000000000009n
```