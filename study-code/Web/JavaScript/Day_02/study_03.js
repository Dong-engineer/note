// continue、break
// continue，当开发者想要跳过某个循环中的某一轮循环可以使用，如下：
// 当结果为 10 时跳过
var a = 0;
for(let i = 0; i < 15; i++) {
    a++;
    if(a == 10) {
        // continue 会跳过本次循环所剩余的代码，进入下一次新的循环
        continue;
    }
    console.log(a);
}

// break，终止本轮循环
var b = 20;
for(let i = 0; i < 15; i++) {
    b++;
    if(b == 30) {
        break;
    }
    console.log(b);
}