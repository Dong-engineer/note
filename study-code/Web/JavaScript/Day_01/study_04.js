// Boolean
var a = new Boolean(true);
var b = new Boolean(false);
console.log(a);
console.log(b);
console.log(typeof(a));
console.log(typeof(b));

// 开发者想要获取布尔类型的字面值依然需要使用 valueOf()
console.log(a.valueOf());
console.log(b.valueOf());
console.log(typeof(a.valueOf()));
console.log(typeof(b.valueOf()));

// 这里主要说一下布尔类型的判断逻辑
// 在 JS 中所有的值（注意哈，不是对象），都具有隐式的 true 和 false
// 在 JS 中 0、null、undefined、NaN、"" 都是隐式的 false
var c = new Boolean("hello"); // true
var d = new Boolean("false"); // true
var e = new Boolean(10); // true
console.log(c.valueOf());
console.log(d.valueOf());
console.log(e.valueOf());

var f = new Boolean(0); // false
var g = new Boolean(NaN); // false
var h = new Boolean(null); // false
var i = new Boolean(undefined); // false
var j = new Boolean(""); // false
console.log(f.valueOf());
console.log(g.valueOf());
console.log(h.valueOf());
console.log(i.valueOf());
console.log(j.valueOf());

// 在 JS 中所有的对象本质上其实都是真值，因为每个对象都被付了值，所以即使该对象包含 false 布尔基元
// 但是对其对象进行布尔运算时，依然会被判为真
// ! 表示取相反值，!f = false ，所以 !!f = true
console.log(!!f); // true
