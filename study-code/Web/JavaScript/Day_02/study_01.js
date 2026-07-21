// 比较运算符 "==" 和 "==="
// "=="
var a = 123;
var b = "123";
console.log(a == b); // true
console.log(a === b); // false

// 逻辑运算符
// && => 逻辑与  || => 逻辑或  ! => 非

// && => 逻辑与，只有当所有条件都为真时最终结果才为真
var c = true;
var d = false;
// 当开发者需要判断多个条件是否成立，就可以使用 &&
console.log(c && d); // false
console.log(c && true); // true
// 也可以是表达式
// 当开发者熟练以后，开发者可能需要在其里调用函数
var e = 10;
console.log(e > 0 && e < 11); // true

function fristFun(params) {
    return params < 0; // 这里是小于 10
}

function secondFun(params) {
    console.log("secondFun() 执行了");
    return params < 11;
}

console.log(fristFun(e) && secondFun(e)); // 在 fristFun() 函数中比较了 10 < 0，结果为 false
// && 只有当两个条件为真才为真，否则为假，此时第一个条件为假，所以结果必定为假，第二个函数不再执行
// 如果开发者希望两个表达式都要被执行，那么可以使用 &

// || => 逻辑或，两个条件有一个为 true ，结果就为 true
console.log(e > 0 || e < 10); // true，同理 e < 10 改表达式不再执行

// ! => 非，结果取反
console.log(!( e > 0 )); // false