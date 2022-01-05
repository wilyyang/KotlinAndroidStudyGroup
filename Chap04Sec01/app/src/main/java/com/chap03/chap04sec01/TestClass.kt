package com.chap03.chap04sec01

class NonDataClass(val name: String, val email: String, val age: Int)
data class DataClass(val name: String, val email: String, val age: Int){
    lateinit var address: String
    constructor(name: String, email: String, age: Int, address: String):
            this(name, email, age){
        this.address = address
    }
}

open class Super2{
    open var data = 10
    open fun some(){
        println("i am super some( : $data")
    }
}

val obj = object : Super2(){
    override var data = 20
    override fun some(){
        println("i am object some() : $data")
    }
}

class MyClass{
    companion object {
        var data = 10
        fun some(){
            println("companion data : $data")
        }
    }
}

fun main() {
    val non1 = NonDataClass("kkang", "a@a.com", 10)
    val non2 = NonDataClass("kkang", "a@a.com", 10)

    val data1 = DataClass("kkang", "a@a.com", 10, "seoul")
    val data2 = DataClass("kkang", "a@a.com", 10, "busan")

    println("non data class equals : ${non1.equals(non2)}")
    println("data class equals : ${data1.equals(data2)}") // true

    println("non data class toString : ${non1.toString()}")
    println("data class toString : ${data1.toString()}")

    obj.data = 30
    obj.some()

    MyClass.data = 20
    MyClass.some()
}