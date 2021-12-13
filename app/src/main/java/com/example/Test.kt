package com.example

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.abs
import kotlin.random.Random
import kotlin.streams.asSequence
import kotlin.system.measureTimeMillis

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/2/8
 * Time: 0:21
 */
class Test {


}

sealed class Figure {
    abstract fun area(): Double

    class Rectangle(val length: Double, val width: Double) : Figure() {
        override fun area(): Double = length * width
    }

    class Circle(val radius: Double) : Figure() {
        override fun area(): Double = Math.PI * (radius * radius)
    }
}

enum class E(val value: Int) {
    Red(1),
    Blue(2)
}

sealed interface I {
    fun add()
    fun reduce()
}

@RequiresApi(Build.VERSION_CODES.N)
fun main(figure: Figure) {
    val random = java.util.Random()
    Random.nextInt()
    abs(random.nextInt())
    //选择的随机数生成器现在【首选】是 ThreadLocalRandom 。它产生更高质量的随机数，而且速度非常快。在我的机器上，它比 Random 快 3.6 倍。对于 fork 连接池和并行流，请使用 SplittableRandom。
//    ThreadLocalRandom.current()
//    SplittableRandom
//    val mutableLiveData = MutableLiveData<Figure>()
//    mutableLiveData.postValue(Figure.Rectangle(1.0,1.0))
//    mutableLiveData.observe(){
//        when(it){
//            is Figure.Circle -> TODO()
//            is Figure.Rectangle -> TODO()
//        }
//    }
//    when (figure) {
//        is Figure.Circle -> {
//            figure.area()
//        }
//        is Figure.Rectangle -> {
//
//        }
//    }


//    val n = arrayOf(1, 23, 4, 5)
//    print("a:${n.sortedBy { it }}")
//    val list = listOf(1, 2, 3, 4, 5, 6)
//    val mapN=n.associateBy {  }
//    val mapL=list.associateBy { it }
//    for (entry in mapL) {
//        println(entry.value)
//    }
//    val r = Random()
//    val listOf = listOf(100_000, 1_000_000, 10_000_000)
//
//    listOf.asSequence()
//        .map { (1..it).map { r.nextInt(1_000_000_000) } }
//        .forEach { list: List<Int> ->
//            println(
//                "Java stdlib sorting of ${list.size} elements took ${
//                    measureTimeMillis {
//                        list.sorted()
//                    }
//                }"
//            )
////            println("quickSort sorting of ${list.size} elements took ${measureTimeMillis { list.quickSort() }}")
//        }
//    listOf.stream().asSequence()
//        .map { (1..it).map { r.nextInt(1_000_000_000) } }
//        .forEach { list ->
//            println(
//                "Java stdlib sorting of ${list.size} elements took ${
//                    measureTimeMillis {
//                        list.sorted()
//                    }
//                }"
//            )
//        }


}

fun <T : Comparable<T>> List<T>.quickSort(): List<T> =
    if (size < 2) this
    else {
        val pivot = first()
        val (smaller, greater) = drop(1).partition { it <= pivot }
        smaller.quickSort() + pivot + greater.quickSort()
    }

class WorkSingleton private constructor(val url: String) {
    init {
        // Init using context argument

    }

    fun get() {
        print(url)
    }

    companion object :
        SingletonHolder<WorkSingleton, String>(::WorkSingleton)

//    companion object : SingletonHolder<WorkSingleton, Context>(::WorkSingleton)
}

open class SingletonHolder<out T : Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}

