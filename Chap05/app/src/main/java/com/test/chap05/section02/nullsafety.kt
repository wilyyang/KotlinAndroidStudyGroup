package com.test.chap05.section02

fun some(data: String?): Int{
    return data!!.length
}
fun main() {
    var data: String? = null
    println("data length : ${data?.length ?: 0}")
    var data2: String? = "kkang"
    println("data2 = $data2 : ${data2?.length ?: -1}")
    data2 = null
    println("data2 = $data2 : ${data2?.length ?: -1}")

    println(some("kkang"))
    println(some(null))
}