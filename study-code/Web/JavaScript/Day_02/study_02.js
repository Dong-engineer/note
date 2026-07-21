// 控制流
// 条件语句，当开发者想要判断某个条件是否成立是，可以使用条件语句，如下：
if(10 > 9) {
    console.log("10 大于 9");
}
// else 则表示非 10 > 9 的情况 
else {
    console.log("10 小于 9"); // 逻辑上来讲这里永远不可能执行
}

// 如果开发者需要判断多种情况，则可以使用 else if
if(10 > 9) {
    console.log("10 大于 9");
} else if(10 > 8) {
    console.log("10 大于 8");
} else {
    console.log("程序运行不到这里");
}

// 三目运算符
// 条件语句 ? 真值 : 假值
var a = 10 > 9 ? "10 大于 9" : "10 小于 9";
console.log(a);

// switch case ，如果开发者只需要一个判断条件，但是需要多种情况的赋值，可以使用

switch(10 > 9) {
    default:
        console.log("这里是默认情况"); // 所有 case 情况都不匹配，这里会默认执行
        break;
    case true:
        console.log("10 大于 9");
        break; // 使用 break 关键字直接中断匹配，否则程序会匹配完所有的 case 情况，除非开发者有需要可以不加 break
    case false:
        console.log("10 小于 9");
        break;
}

// 循环，当开发者想要重复某一段程序时，那么可以使用循环语句
// 加入现在我希望某个数字，每次增加 1，一共增加 10 次，代码如下：
var b = 10;
var b_count = 0; // 记录次数
// while 循环，循环条件只要不为 false，则一直循环
while(b_count < 10) {
    b += 1;
    console.log(b);
    b_count++;
}
// do...while 循环，与 while 循环不同的是，do...while 循环的循环条件在语句结尾，这也就是说循环体至少被执行 1 次
var c = 20;
var c_count = 0;
do {
    c += 1;
    console.log(c);
    c_count++;
} while(c_count < 10);
// for 循环与以上两种都不同，for 循环有着指定的循环次数以及步长（即每次循环一次 i 增加多少）
// for (循环起点; 循环终点判断条件; 循环步长)
var d = 30;
for(let i = 0; i < 10; i++) {
    d += 1;
    console.log(d);
}
