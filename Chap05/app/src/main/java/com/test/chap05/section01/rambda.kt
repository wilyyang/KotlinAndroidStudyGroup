package com.test.chap05.section01

typealias MyFunType = (Int, Int) -> Boolean

fun hofFun(arg: (Int) -> Boolean) : () -> String{
    val result = if(arg(10)){
        "valid"
    }else{"invalid"}
    return { println("TEMPTEMP"); "hofFun result : $result"}
}

fun main() {
    val a = { no1:Int, no2:Int -> println("yellow"); val no3 = no1+1; no1+no2+no3 }(10,20)
    println(a)

    val some: (Int) -> Unit =  {println(it)}
    some(10)

    val some2 = {no1: Int, no2: Int ->  println("in lambda function")
    no1*no2}

    println("result : ${some2(10,20)}")

    val someFun: MyFunType = {no1, no2 -> no1 > no2 }
    println(someFun(10,20))
    println(someFun(20,10))

    val someFun2: (Int, Int) -> Boolean = { no1, no2 -> no1 > no2 }
    val someFun3 = {no1: Int, no2: Int -> no1 > no2 }

    val result = hofFun { no -> no > 0 }
    println(result())
}