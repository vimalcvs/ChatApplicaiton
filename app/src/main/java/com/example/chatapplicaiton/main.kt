package com.example.chatapplicaiton

fun main() {
    println("hello world")

    println(reversString("hello"))
}


fun reversString(str: String): String {
    return str.reversed()
}