// BigInt
// BigInt 的出现是为了解决 Number 类型在计算时超出其范围的问题
var a = Number.MAX_SAFE_INTEGER; // Number 还有一个 MAX_VALUE 最大值，但这个值并非安全，其是一个溢出值
console.log(a); // 9007199254740991

var b = new Number(9007199254740992);
console.log(b);
console.log(typeof(b)); // 这里的 b 依然是 Number 类型，这是因为 9007199254740992 并未超出 Number 的 MAX_VALUE

var c = Number.MIN_SAFE_INTEGER;
console.log(c); // -9007199254740991
console.log(typeof(c));

var d = 9999999999999999;
console.log(d); // 10000000000000000

// 在实际开发中如果数据的值真的很大，超出了 MAX_SAFE_VALUE 和 MIN_SAFE_INTEGER 那么开发者一定要做一个判断
// 因为 9007199254740991 是一个安全值，也就说在该范围之内的数字进行任何计算都是安全的
// 超出该范围的数值，在计算时很有可能会出现错误！！！

// 于是 Google 设计了 BigInt 类型
// 开发者只需要在数值后面加个 n 即可，如下：
var e = 9999999999999999n;
console.log(e);
console.log(typeof(e));

// BigInt 的运算
// var f = e + 10; // 抛出异常，JS 中不允许 BigInt 和 Number 类型进行混合算术运算

var f = e + 10n;
console.log(f);
console.log(typeof(f));
