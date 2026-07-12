# switch控制语句

## switch语句

`switch`语句允许任意数量的执行路径。语句将选择器变量作为参数，并使用此变量的值来选择将要执行的路径。但在Java 13后，`switch`有了更多的用法。

先来了解最初的`switch`语句，当我们需要判断某个元素符合某个条件的时候，使用`if`，如果有多种情况就需要写很多个`if`、`else if`。这会大大降低代码可读性和运行效率。这种情况就应使用`switch`语句。

如下：

```java
int quarter = ...; // any value

String quarterLabel = null;

// 接受被判断的参数
switch (quarter) {
    // quarter=0
    case 0: quarterLabel = "Q1 - Winter"; 
            break;
    // quarter=1
    case 1: quarterLabel = "Q2 - Spring"; 
            break;
    // quarter=2
    case 2: quarterLabel = "Q3 - Summer"; 
            break;
    // quarter=3
    case 3: quarterLabel = "Q3 - Summer"; 
            break;
    // quarter未预料的默认情况
    default: quarterLabel = "Unknown quarter";
};
```

以上代码，注意到，每个`case`后都跟了一个`break`，这是为了保证`switch`在进行判断后，一旦匹配成功后直接跳出语句，不再进行后面的判断。

`case`后也可以调用函数，如下：

```java
int month = 8;
List<String> futureMonths = new ArrayList<>();

switch (month) {
    case 1:  futureMonths.add("January");
    case 2:  futureMonths.add("February");
    case 3:  futureMonths.add("March");
    case 4:  futureMonths.add("April");
    case 5:  futureMonths.add("May");
    case 6:  futureMonths.add("June");
    case 7:  futureMonths.add("July");
    case 8:  futureMonths.add("August");
    case 9:  futureMonths.add("September");
    case 10: futureMonths.add("October");
    case 11: futureMonths.add("November");
    case 12: futureMonths.add("December");
             break;
    default: break;
}
```

这里没有使用`break`跳出`switch`的判断，所以程序会在`case 8`的时候，执行`futureMonths.add("August")`，且后续的`case`和其方法体也会执行，到`case 12`执行后`break`。这种现象叫做**穿透现象**。

**在实际开发中，我们要考虑业务是否需要穿透现象。**

## 多值判断

如果你希望多个不同的`case`值执行同一组代码，可以将多个`case`标签堆叠在一起，中间不用加`break`，直到遇到需要执行的代码块和`break`为止。

```java
int month = 2;
int year = 2021;
int numDays = 0;

switch (month) {
    case 1: case 3: case 5:   // January March May
    case 7: case 8: case 10:  // July August October
    case 12:
        numDays = 31;
        break;
    case 4: case 6:   // April June
    case 9: case 11:  // September November
        numDays = 30;
        break;
    case 2: // February
        if (((year % 4 == 0) && 
             !(year % 100 == 0))
             || (year % 400 == 0))
            numDays = 29;
        else
            numDays = 28;
        break;
    default:
        System.out.println("Invalid month.");
        break;
}
```

## 总结

`switch`语句的判断不仅限域数值，也可以是字符，对象等等，如果是对象的话要保证其不为null，否则`switch` 语句将抛出`switchNullPointerException`。

# switch表达式

## switch表达式

在上一节我们了解了switch表达式，这里详细解释。

正常写`switch`语句是无法作为表达式的，如下：

```java
Day day = ...; // any day
int len = 0;
switch (day) {
    case MONDAY:
    case FRIDAY:
    case SUNDAY:
        len = 6;
        break;
    case TUESDAY:
        len = 7;
        break;
    case THURSDAY:
    case SATURDAY:
        len = 8;
        break;
    case WEDNESDAY:
        len = 9;
        break;
}
System.out.println("len = " + len);
```

使用 `switch` 表达式语法，如下：

```java
Day day = ...; // any day
int len =
    switch (day) {
        case MONDAY, FRIDAY, SUNDAY -> 6;
        case TUESDAY                -> 7;
        case THURSDAY, SATURDAY     -> 8;
        case WEDNESDAY              -> 9;
    };
System.out.println("len = " + len);
```

仔细观察，`switch`表达式和正常的`switch`表达语句有很大的区别，当我们要匹配多值的时候，不用写多个`case`了，直接再后面写多个变量用`,`分割即可，同时后面的方法体也简化成箭头函数（匿名函数的一种）。

## yield和switch表达式

接着是`yield`关键字和`switch`的搭配使用。

在写函数时，传统意义上我们使用`return`来返回某个值，所以当我们在函数中使用`switch`表达式的时候会有**歧义**，如下：

```java
public String convertToLabel(int quarter) {
    String quarterLabel =
        switch (quarter) {
            case 0  -> {
                System.out.println("Q1 - Winter");
                return "Q1 - Winter";
            }
            default -> "Unknown quarter";
        };
    return quarterLabel;
}
```

以上代码我们会发现，当`case 0`匹配成功时候，`return`返回了`Q1 - Winter`，这时开始思考，此时返回是整个函数结束返回的值，还是结束了`quarterLabel`的声明，这种歧义会导致可读性差和容易出错的代码。

于是便有了`yield`关键字，如下：

```java
public String convertToLabel(int quarter) {
    String quarterLabel =
        switch (quarter) {
            case 0  -> {
                System.out.println("Q1 - Winter");
                yield "Q1 - Winter";
            }
            default -> "Unknown quarter";
        };
    }
    return quarterLabel;
}
```

在**控制流程语句**一节中讲述了`yield`关键字的使用。如有忘记请回头重温。

