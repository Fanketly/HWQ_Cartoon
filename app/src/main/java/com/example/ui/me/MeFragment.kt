package com.example.ui.me

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.example.base.BaseFragment
import com.example.hwq_cartoon.databinding.FragmentMeBinding


class MeFragment : BaseFragment<FragmentMeBinding>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun viewBinding(container: ViewGroup): FragmentMeBinding {
        return FragmentMeBinding.inflate(layoutInflater,container,false)
    }


}