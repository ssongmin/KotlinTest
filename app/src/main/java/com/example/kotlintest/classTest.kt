package com.example.kotlintest

fun main() {
    var class1 = test1(1)
    println(class1.a1);

    var class2 = test1()
    println(class2.a1)

    var dataClass = test2(21, "tt")
    println(dataClass.a1)
    println(dataClass.a2)
}

class  test1{
    var a1: Int = 0
    init {
        a1 = 11
    }

    constructor()

    constructor(age: Int) {
        this.a1 = age;
    }
}

data class test2(var a1: Int, var a2: String)