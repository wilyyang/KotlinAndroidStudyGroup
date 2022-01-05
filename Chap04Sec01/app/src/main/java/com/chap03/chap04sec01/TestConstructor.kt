package com.chap03.chap04sec01

class User(var name: String = "kkang", val count: Int = 0) {
    var age = 13
    var email = "none"

    constructor(name: String, count: Int, age: Int):this(name, count){
        this.age = age
        someFun();
    }

    constructor(name: String, count: Int, age: Int, email: String):this(name, count, age){
        this.age = age
        this.email = email
        println("email : $email")
    }

    fun someFun(){
        println("name : $name, count : $count, age : $age")
    }

    class SomeClass {}
}

fun main() {
    val user1 = User("park", 28)
    val user2 = User("kim", 33, 44)
    val user3 = User("lee", 54, 763, "ef@nf.net")
}