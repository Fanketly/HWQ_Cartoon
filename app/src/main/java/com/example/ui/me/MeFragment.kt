package com.example.ui.me

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.BaseFragment
import com.example.base.PAGER_ORIENTATION
import com.example.base.ViewBindingRvAdapter
import com.example.hwq_cartoon.App
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.DialogAutoBinding
import com.example.hwq_cartoon.databinding.DialogMePagerBinding
import com.example.hwq_cartoon.databinding.FragmentMeBinding
import com.example.hwq_cartoon.databinding.RvSettingBinding
import com.example.repository.model.SettingInfo
import com.example.viewModel.FavouriteViewModel
import com.example.viewModel.MeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MeFragment : BaseFragment<FragmentMeBinding>() {

    private val meViewModel: MeViewModel by activityViewModels()
    private val favouriteViewModel: FavouriteViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.rvSetting.layoutManager = LinearLayoutManager(context)
        favouriteViewModel.sizeLiveData.observe(viewLifecycleOwner) {
            b.tvMeHistorySize.text = "累计观看过的漫画数量：${favouriteViewModel.historySize}"
            b.tvMeFavouriteSize.text = "追漫数量：${favouriteViewModel.favouriteSize}"
        }
        b.chipTheme.setChecked(App.blackTheme)
        b.chipTheme.setOnCheckedChangeListener {
            lifecycleScope.launch {
                delay(200)
                meViewModel.selectTheme()
                requireActivity().recreate()
            }
        }
        val adapter =
            object : ViewBindingRvAdapter<SettingInfo, RvSettingBinding>(meViewModel.settingList) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): VH<RvSettingBinding> {
                    return viewBinding(parent)
                }

                override fun onBind(b: RvSettingBinding, d: SettingInfo, p: Int) {
                    b.tvSetting.text = d.name
                    b.tvSetting.setOnClickListener {
                        when (p) {
                            0 -> {//漫画自动滚动速度设置
                                with(AlertDialog.Builder(context).create()) {
                                    val dialogAutoBinding =
                                        DialogAutoBinding.inflate(layoutInflater, null, false)
                                    dialogAutoBinding.btn.setOnClickListener {
                                        lifecycleScope.launch {
                                            meViewModel.saveAuto(
                                                dialogAutoBinding.edt.text.toString().toInt()
                                            )
                                            Toast.makeText(
                                                requireContext(),
                                                "保存成功",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dismiss()
                                        }
                                    }
                                    dialogAutoBinding.btn2.setOnClickListener {
                                        dismiss()
                                    }
                                    setView(dialogAutoBinding.root)
                                    show()
                                }
                            }
                            1 -> {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://gitee.com/fanketly/HWQ_Cartoon")
                                    )
                                )
                            }
                            2 -> {
                                val dialogMePagerBinding =
                                    DialogMePagerBinding.inflate(layoutInflater, null, false)
                                when (App.pagerOrientation) {
                                    LinearLayout.VERTICAL -> dialogMePagerBinding.swPagerV.setChecked(
                                        true
                                    )
                                    LinearLayout.HORIZONTAL -> dialogMePagerBinding.swPagerH.setChecked(
                                        true
                                    )
                                    3 -> dialogMePagerBinding.swPagerH2.setChecked(true)
                                }
                                dialogMePagerBinding.swPagerV.setOnClickListener {
                                    pager(LinearLayout.VERTICAL, dialogMePagerBinding)
                                }
                                dialogMePagerBinding.swPagerH.setOnClickListener {
                                    pager(LinearLayout.HORIZONTAL, dialogMePagerBinding)
                                }
                                dialogMePagerBinding.swPagerH2.setOnClickListener {
                                    pager(3, dialogMePagerBinding)
                                }
                                val dialog = AlertDialog.Builder(requireContext())
                                    .setView(dialogMePagerBinding.root).show()
                                dialog.setOnDismissListener {
                                    App.kv.encode(PAGER_ORIENTATION, App.pagerOrientation!!)
                                }
                            }
                            3 -> {
                                meViewModel.bottomLiveData.postValue(true)
                                add(AboutFragment())
                            }
                        }
                    }
                    b.tvSetting.setCompoundDrawablesWithIntrinsicBounds(
                        d.img,
                        0,
                        R.drawable.ic_baseline_keyboard_arrow_right_24,
                        0
                    )
                }
            }
        b.rvSetting.adapter = adapter
    }

    private fun pager(orientation: Int, binding: DialogMePagerBinding) {
        App.pagerOrientation?.let {
            if (orientation == it) return
            when (it) {
                LinearLayout.VERTICAL -> binding.swPagerV.setChecked(false)
                LinearLayout.HORIZONTAL -> binding.swPagerH.setChecked(false)
                3 -> binding.swPagerH2.setChecked(false)
            }
            when (orientation) {
                LinearLayout.VERTICAL -> binding.swPagerV.setChecked(true)
                LinearLayout.HORIZONTAL -> binding.swPagerH.setChecked(true)
                3 -> binding.swPagerH2.setChecked(true)
            }
            App.pagerOrientation = orientation
        }
    }

    override fun viewBinding(container: ViewGroup?): FragmentMeBinding {
        return FragmentMeBinding.inflate(layoutInflater, container, false)
    }


}