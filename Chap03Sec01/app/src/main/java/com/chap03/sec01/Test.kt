package com.chap03.sec01

val data1:Int = 10
var data2 = 10

fun someFun(){
    val data3: Int
//    println("data3 : $data3")
    data3 = 10
    println("data3 : $data3")
}

fun someFun2(){
    var data1: Int = 10
    var data2: Int? = null

    data1 = data1 + 10
    data1 = data1.plus(10)
}

fun someFun3(): Unit{
    println(10+20)
}

fun someFun4(): Nothing{
    throw Exception()
}

class User{
//    lateinit var data1: Int // Int, Long, Short ... not allowed
//    lateinit val data2: String
    lateinit var data3: String
    val data4: Int by lazy{
        println("in lazy......")
        10
    }
    val data5: Int = 10
}

fun main(){
    println("hello world");

//    data1 = 20
    data2 = 20

    val str1 = "Hello \n World"
    val str2 = """
        Hello
        World
    """.trimIndent()
    println("str1 : $str1")
    println("str2 : $str2")

    fun sum(no: Int): Int{
        var sum = 0
        for (i in 1..no){
            sum+=i
        }
        return sum
    }

    val name: String = "kkang"
    println("name : $name, sum : ${sum(10)}, plus : ${10+20}")

    someFun3()
//    someFun4()

    fun some(data1: Int, data2: Int = 10): Int{
        return data1*data2
    }
    println(some(10))
    println(some(10,20))
    println(some(data2=20,data1=10))
}