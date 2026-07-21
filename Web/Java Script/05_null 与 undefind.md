# null

`null` 是基础原始类型，代表**开发者主动定义的空值**，可手动赋值清空变量。 

存在的历史 bug：`typeof null` 返回 `object`。（但是官方文档中写到，这是故意遗留的，以避免破坏整个网络的预期行为）

```js
typeof null // object
```

# undefined

`undefined` 是原始值，代表**变量无赋值、无有效返回**，由 JS 引擎自动生成。 

场景：变量声明未赋值、函数无 return、空 return。

```js
function fn(){}
console.log(fn()); // undefined

(function(){ return; })() // undefined
```

# null 与 undefined 对比

语义区分：

- `null`：主动置空，人为定义缺失值
- `undefined`：未赋值，系统默认空状态

相等判断：宽松相等为 true，严格相等为 false（类型不同）

```js
null == undefined  // true
null === undefined // false
```

注意：`undefined` 不是关键字，局部作用域可被覆盖，**禁止用作变量名**，易引发逻辑异常。