package com.example.repository.model

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/4/2
 * Time: 13:41
 */
data class CartoonInfo constructor(
    val title: String,
    val href: String,
    val img: String,
    val type: String?
) {

    constructor(title: String, href: String) : this(title, href, "", null)

    constructor(title: String, href: String, img: String) : this(title, href, img, null)
}