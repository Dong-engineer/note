# Symbol

## Symbol 介绍

Symbol（符号）代表一个**永远不会与其他任何值冲突的唯一值**，包括其他 Symbol 原始值。

两个内容完全相同的字符串原始值，严格相等判定结果为 true：

```js
String() === String()	// true
String( "My string." ) === String( "My string." );	// true
```

但通过 `Symbol()` 函数创建的任意两个 Symbol，**永远不可能严格相等**：

```js
Symbol() === Symbol()	// false
Symbol( "My symbol." ) === Symbol( "My symbol." );	// false
```

这一特性让 Symbol 非常适合作为对象的**唯一属性键**，可以彻底避免和其他代码新增的对象键名发生冲突。

```js
const mySymbol = Symbol( "Desc" );

const myObject = {};
myObject[mySymbol] = "propSymbol"; // 将 mySymbol 作为 key, "propSymbol" 作为 value

myObject	// Object { Symbol("Desc"): "propSymbol" }
```

## Symbol 的三种类型

- 通过 `Symbol()` 自定义创建的普通 Symbol
- 通过 `Symbol.for()` 从全局 Symbol 注册表中设置和获取的**共享 Symbol**
- 定义在 Symbol 对象上的静态属性——**知名 Symbol（Well-known symbols）**，内置原生方法，不会被意外覆盖

`Symbol()` 支持传入一个可选参数作为**描述名**（Symbol 名称）。该描述仅用于开发者调试阅读，**不会影响 Symbol 的唯一性**。即便两个 Symbol 的描述文字完全一致，它们依然是两个完全独立、互不相等的值。

```js
Symbol( "My symbol." ) === Symbol( "My symbol." );	// false
```

和其他原始数据类型一致，Symbol 会从自身原型上继承方法和属性。例如可以直接读取 Symbol 的描述属性：

```js
let mySymbol = Symbol( "My symbol." );

mySymbol.description	// "My symbol."
```

**禁止使用 new 关键字创建 Symbol**，Symbol 不是构造函数，调用会直接报错：

```js
let mySymbol = new Symbol();
// Uncaught TypeError: Symbol is not a constructor
```

Symbol 属性**不可枚举**，常规的对象遍历方法无法获取到 Symbol 类型的键名。可以通过 `Object.getOwnPropertySymbols()` 方法，专门获取对象上所有的 Symbol 属性。

## 共享 Symbol

`Symbol.for()` 方法会在**全局唯一的 Symbol 注册表**中，根据传入的字符串键名查找已存在的 Symbol：

- 找到对应 Symbol：直接返回已有值
- 未找到对应 Symbol：以该字符串为键，创建新的 Symbol 并存入全局注册表

```js
let sharedSymbol = Symbol.for( "My key." );

sharedSymbol === Symbol.for( "My key." )
// true
```

**重点区分**：全局注册表的键名，和普通 Symbol 的描述名完全无关、互不冲突。通过 `Symbol()` 创建的同名 Symbol，永远不等于 `Symbol.for()` 创建的 Symbol。

```js
Symbol( "String" ) === Symbol( "String" );	// false

Symbol( "String" ) === Symbol.for( "String" );	// false

Symbol.for( "String" ) === Symbol.for( "String" );	// true
```

通过 `Symbol.keyFor()` 方法，可以从全局注册表中，获取共享 Symbol 对应的键名字符串：

```js
let mySymbol = Symbol.for( "Key." );

Symbol.keyFor( mySymbol );	// "Key."
```
