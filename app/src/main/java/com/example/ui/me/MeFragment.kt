package com.example.ui.me

import android.os.Bundle
import android.view.View
import com.example.base.ViewBindingBaseFragment
import com.example.hwq_cartoon.databinding.FragmentMeBinding


class MeFragment : ViewBindingBaseFragment<FragmentMeBinding>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun rootView(): FragmentMeBinding {
        return FragmentMeBinding.inflate(layoutInflater)
    }


}