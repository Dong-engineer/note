/**
 * 变量的声明，变量的声明有三种方式：var、let、const
 * 这是 Java Script 中特有的，也可以说是独有的
 * 当开发者想要创建变量时，就需要用到这三个关键字
 * 在 Google 的官方文当中还提到了 "令牌" ，关于令牌，这里作者也是第一次听到这种说法，
 * 文档所描述的作者个人认为，只要是存在在文件中的代码都可以称为令牌，var、let、const 其实就可以称为关键字令牌
 * 而下面代码中的 a、b、c 则可以被称为标识符令牌等等 
 */
/**
 * var：
 * 声明全局动态变量
 * let:
 * 声明块级动态变量
 * const:
 * 声明块级静态变量（即不可以被修改的变量）
 * 
 * 所谓的块级变量实际就是一对 "{}" ，这里也可也称作作用域
 */

// 开启严格模式
// 'use strict';
var a = "hello word";

let b = "hello";

const c = "word";

function firstFun(params) {
    console.log(params);
}

firstFun(a);
firstFun(b);
firstFun(c);

function secondFun(params) {
    params = "这是第二个函数，这里我被重新赋值";
    console.log(params);
}

secondFun(a);
secondFun(b);
secondFun(c);

function thirdFun() {
    var d = "我是 var 类型变量，我在 thirdFun() 中";
    let e = "我是 let 类型变量，我在 thirdFun() 中";
    const f = "我是 const 类型变量，我在 thirdFun() 中";
    console.log(d);
    console.log(e);
    console.log(f);
}

thirdFun();

// 非严格模式下这里并不会报错，这里 JS 会默认将其识别为新的变量，只不过并没有使用 var、let、const 去声明
d = "我是 var 类型变量，在 thirdFun() 外重新被赋值";
e = "我是 let 类型变量，在 thirdFun() 外重新被赋值";
f = "我是 const 类型变量，在 thirdFun() 外重新被赋值";
console.log(d);
console.log(e);
console.log(f);

function fourthFun() {
    // 字符串本身就是一个常量值不允许被修改
    // 这里进行了拼接运算，其底层实际更换了 g 变量指针的指向，g 原本指向 "我是一个块级变量"，现在指向了 "我是一个块级变量，我允许被赋值"
    let g = "我是一个块级变量";
    g += "，我允许被赋值";
    console.log(g);

    // 这里开发者可以拿 h 做字符串实验
    const h = [5,2,3,4];
    // h = [1,2,3,4]; // 这里会报错，h 是一个 const 变量
    h[0] = 1; // const 实际并非不让修改内容，而是不允许开发者修改其变量的指针指向
    console.log(h); // [1,2,3,4]
}

fourthFun();