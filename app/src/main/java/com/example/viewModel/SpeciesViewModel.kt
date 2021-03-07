package com.example.viewModel

import com.example.repository.model.SpeciesInfor

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/5
 * Time: 17:04
 */
class SpeciesViewModel(private val speciesInfor: SpeciesInfor) {
    val title
        get() = speciesInfor.title
    val id
        get() = speciesInfor.id
}