package com.example

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/2/8
 * Time: 0:21
 */
class Test {


}
enum class E(val value:Int){
    Red(1),
    Blue(2)
}
fun main() {
    val n= arrayOf(1,23,4,5)

    print("a:${n.sortedBy { it }}")
}