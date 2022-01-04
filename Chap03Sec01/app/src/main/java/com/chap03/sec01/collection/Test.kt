package com.chap03.sec01.collection

fun main() {
    val data: Array<Int> = Array(3, { 0 })
    data[0] = 10
    data[1] = 20
    data.set(2,30)

    println(
        """
            array size : ${data.size}
            array data : ${data[0]}, ${data[1]}, ${data[2]}
        """.trimIndent()
    )

    val data1: IntArray = IntArray(3, {0})
    val data2: BooleanArray = BooleanArray(3, {false})

    val data3 = arrayOf<Int>(11,22,33)

    println(
        """
            array size : ${data3.size}
            array data : ${data3[0]} ${data3[1]} ${data3[2]}
        """.trimIndent()
    )

    var list = listOf<Int>(101,202,303)
    println(
        """
            list size : ${list.size}
            list data : ${list[0]}, ${list.get(1)}, ${list.get(2)}
        """.trimIndent()
    )

    var mutableList = mutableListOf<Int>(102, 203, 304)
    mutableList.add(3, 40)
    mutableList.set(0, 50)
    println(
        """
            mlist size : ${mutableList.size}
            mlist data : ${mutableList[0]}, ${mutableList.get(1)},
             ${mutableList.get(2)}, ${mutableList.get(3)},
        """.trimIndent()
    )

    var map = mapOf<String, String>(Pair("one", "hello"), "two" to "world")
    println(
        """
            map size : ${map.size}
            map data : ${map.get("one")}, ${map["two"]}
        """.trimIndent()
    )

    var dataif = 10
    val result = if(dataif > 0){
        println("dataif > 0")
        true
    }else{
        println("dataif <= 0")
        false
    }
    println(result)

    var datawhen = 10
    when(datawhen){
        10 -> println("datawhen is 10")
        20 -> println("datawhen is 20")
        else -> {
            println("datawhen is not valid data")
        }
    }

    var datawhen2 = "hello"
    when(datawhen2){
        "hello" -> println("datawhen2 is hello")
        "world" -> println("datawhen2 is world")
        else -> {
            println("datawhen2 is not valid data")
        }
    }

    var datawhen3: Any = 10
    when(datawhen3){
        is String -> println("datawhen3 is String")
        20, 30 -> println("datawhen3 is 20 or 30")
        in 1..10 -> println("datawhen3 is 1..10")
        else -> println("datawhen3 is not valid")
    }

    var datawhen4 = 10
    when{
        datawhen4 <= 0 -> println("datawhen4 is <= 0")
        datawhen4 > 100-> println("datawhen4 is > 100")
        else -> println("datawhen4 is valid")
    }

    var datawhen5 = -1
    val result2 = when{
        datawhen5 <= 0 -> "datawhen5 is <= 0"
        datawhen5 > 100 -> "datawhen5 is > 100"
        else -> "datawhen5 is valid"
    }
    println(result2)

    var sum: Int = 0
    for (i in 1 until 10 step 2){
        sum += i
    }
    println(sum)

    var datafor = arrayOf<Int>(10,20,30)
    for(i in datafor.indices){
        print(datafor[i])
        if(i != data.size-1)print(",")
    }
    println()
    var datafor2 = arrayOf<Int>(101, 202, 303)
    for((index, value) in datafor2.withIndex()){
        print(value)
        if(index != data.size-1)print(",")
    }
    println()
    var x = 0
    var sum1 = 0
    while(x<10){
        sum1+= ++x
    }
    println(sum1)
}