package com.example.ui.me

import android.os.Bundle
import com.example.base.ViewBindingBaseFragment
import com.example.hwq_cartoon.databinding.FragmentMeBinding


class MeFragment : ViewBindingBaseFragment<FragmentMeBinding>() {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        b.tv.text = "AAA"
    }

    override fun rootView(): FragmentMeBinding {
        return FragmentMeBinding.inflate(layoutInflater)
    }


}