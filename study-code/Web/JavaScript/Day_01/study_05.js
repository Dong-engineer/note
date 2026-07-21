// null 和 undefined

// null
var a = null;
console.log(a);
console.log(typeof(a)); // object
// console.log(a.valueof()); // null 没有 valueof()

// undefined
var b;
console.log(b); // undefined
console.log(typeof(b)); // undefined

console.log(null == undefined); // true
console.log(null === undefined); // false