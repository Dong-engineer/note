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