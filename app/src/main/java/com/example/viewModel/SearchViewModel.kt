package com.example.viewModel

import com.example.repository.model.CartoonInfor

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/5
 * Time: 19:58
 */
class SearchViewModel(private val cartoonInfor: CartoonInfor) {
    val title: String?
        get() = cartoonInfor.titile
    val img: String?
        get() = cartoonInfor.img
}