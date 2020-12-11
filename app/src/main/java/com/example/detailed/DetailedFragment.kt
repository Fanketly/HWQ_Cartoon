package com.example.detailed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.base.BaseFragment
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfor
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer

class DetailedFragment : BaseFragment(R.layout.fragment_detailed) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        Navigation.findNavController(requireView()).navigateUp()
    }
}