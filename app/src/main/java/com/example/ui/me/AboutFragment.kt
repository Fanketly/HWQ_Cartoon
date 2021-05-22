package com.example.ui.me

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.base.BaseFragment
import com.example.hwq_cartoon.databinding.FragmentAboutBinding
import com.example.viewModel.MeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : BaseFragment<FragmentAboutBinding>() {
    private val meViewModel: MeViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.root.setOnClickListener {  }
        b.tvAboutContent.text = "Fun漫是提供所有动漫爱好者聚集阅读漫画的平台。 自建成并发布以来，逐步完善系统功能和改善用户体验，" +
                "致力于漫画阅读服务，提供海量更新最快的高清在线漫画欣赏，受到用户的好评。以后还会继续接入更多的频道内容，" +
                "欢迎有动漫资源的组织或机构与我们一起为用户共同打造这个平台，我们将不断为了成为向广大动漫爱好者提供优质的漫画阅读空间的优秀漫画App而努力。" +
                "\n\n\n免责声明\n" +
                "1. 除注明之服务条款外，其它因使用而导致任何意外、疏忽、合约毁坏、诽谤、版权或知识产权侵犯 及其所造成的各种损失（包括因下载而感染电脑病毒），" +
                "概不负责，亦不承担任何法律责任。\n" +
                "2. 任何透过Fun漫而链接及得到之资讯、产品及服务，Fun漫概不负责，亦不负任何法律责任。\n" +
                "3. 所有内容并不反映任何Fun漫的意见及观点。\n" +
                "4. Fun漫认为，一切网民在进入Fun漫主页及各层页面时已经仔细看过本条款并完全同意。敬请谅解。"
        b.btnAboutBack.setOnClickListener {
            remove()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        meViewModel.bottomLiveData.postValue(false)
    }
    override fun viewBinding(container: ViewGroup?): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(layoutInflater,container,false)
    }
}