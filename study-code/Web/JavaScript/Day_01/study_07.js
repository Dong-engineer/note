// Symbol 符号
// var a = new Symbol("hello"); // 抛出异常，Symbo 无法通过 new 来创建对象

// 什么是 Symbol ？
// Symbol => 符号类型，symbol => 符号基元，每一个符号都不会和另一个符号发生冲突，也就是说每一个符号都具有唯一性
console.log(String("My string.") === String("My string.")); // true
console.log(Symbol("My string.") === Symbol("My string.")); // false

// 如何使用 Symbol ?
// Symbol 最大的特性就是唯一性，所以开发者要思考如何利用好其唯一性？
// 对于小白来讲 Symbol 的上手有点难度
// 对于由一定知识储备量的开发者来说，如果你的键值对类型的数据结构需要确保数据的唯一性，那么 Symbol 是一个不错的选择
const a = Symbol("properites");
const object = {};
object[a] = "value";
console.log(object);
console.log(object[a]);

// Symbol 符号在编译运行后会被存储在当前程序的注册表中
// 开发者可以通过 Symbol.for() 来存储或共享注册表中的指定符号
// 如果注册表中并没有 String 字符，其会自动创建至字符中
var b = Symbol.for("String");
var c = Symbol.for("String");
console.log(b === c); // true

var d = Symbol("Stirng"); // 开发者注意只有 Symbol.for() 才会从注册表中共享字符
console.log(b == d); // false
console.log(b === d); // false

// 在 JS 中开发者会用到很多的关键字
// JS 并不允许开发者命名变量时将其命名一致
// JS 中的关键字大多数都是唯一性的，其都有通过 Symbol() 声明，使其具有唯一性
var console = "你好";
// console.log(console); // 抛出异常