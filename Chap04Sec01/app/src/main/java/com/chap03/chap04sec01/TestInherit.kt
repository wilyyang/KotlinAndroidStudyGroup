package com.chap03.chap04sec01

open class Super(open var name:String = "memo",
                 var publicData:Int = 10,
                 protected var protectedData:Int = 20,
                 private var privateData:Int = 30){

    open fun superFun() {
        println("i am superFun : $name")
    }
}

class Sub(name: String): Super(name){
    override fun superFun() {
        println("override i am superFun : $name")
        publicData++
        protectedData++
//        privateData++

    }
}

fun main() {
    val obj = Sub("yang")
    obj.name = "wily"
    obj.superFun()
//    obj.protectedData++
//    obj.privateData++
}