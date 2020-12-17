package com.example.detailed

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.example.adapter.CartoonImgRvAdapter
import com.example.base.*
import com.example.hwq_cartoon.R
import com.example.repository.model.FavouriteInfor
import com.example.repository.model.HistoryInfor
import com.example.viewModel.CartoonViewModel
import com.example.viewModel.FavouriteViewModel
import kotlinx.android.synthetic.main.fragment_detailed.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

/***
 * 页面 漫画详细
 * 逻辑 从数据库读取漫画名字并判断是否已经追漫
 *     对点击的漫画保存到历史数据库，追漫的保存到追漫数据库
 * **/
class DetailedFragment : BaseFragment(R.layout.fragment_detailed) {

    private lateinit var cartoonImgRvAdapter: CartoonImgRvAdapter
    private val dateformat = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA)
    private var favouriteInfor: FavouriteInfor? = null
    private var historyInfor: HistoryInfor? = null
    private var historyMark = 0//记录在list位置
    private var favouriteMark = 0
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[CartoonViewModel::class.java]
        val favouriteViewModel =
            ViewModelProvider(requireActivity())[FavouriteViewModel::class.java]
        //返回
        btnDetailBack.setOnClickListener {
//            Navigation.findNavController(requireView()).navigateUp()
            viewModel.bottomLiveData.value = false
            favouriteViewModel.tabLayLiveData.value = false
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out).remove(this).commit()
        }
        CoroutineScope(Dispatchers.Main).launch {
            //跳转所传递的数据
            val name = arguments?.getString("name")
            val img = arguments?.getString("img")
            val mark = arguments?.getInt("mark")//判断fragment
            val position = arguments?.getInt("position") ?: 0//list

            withContext(Dispatchers.Default) {
                //历史部分,修改上次观看时间
                val time = Date(System.currentTimeMillis())
                for ((index, info) in favouriteViewModel.historyList.withIndex()) {
                    if (info.title == name) {
                        info.time = dateformat.format(time)
                        historyInfor = info
                        favouriteViewModel.historyUpdate(info)
                        historyMark = index
                        break
                    }
                }
                //如果历史数据库里没有就添加
                if (historyInfor == null) {
                    historyInfor = HistoryInfor(
                        name,
                        img,
                        viewModel.cartoonInfors[position].href, 0,
                        dateformat.format(time)
                    )
                    Log.i(TAG, "onActivityCreated: ${historyInfor?.title}")
                    favouriteViewModel.historyList.add(historyInfor!!)
                    historyMark = favouriteViewModel.historyList.size - 1
                    favouriteViewModel.historyInsert(historyInfor!!)
                }
                //判断是否已经追漫
                for ((index, info) in favouriteViewModel.favouriteList.withIndex()) {
                    if (info.title == name) {
                        withContext(Dispatchers.Main) { btnDetailAdd.text = "已追漫" }
                        favouriteInfor = info
                        favouriteMark = index
                        break
                    }
                }
            }

            //漫画名与图片
            tvDetailName.text = name
            tvDetailContent.text = viewModel.content
            tvDetailContent.movementMethod = ScrollingMovementMethod()

            Glide.with(requireContext()).asDrawable()
                .load(GlideUrl(img, headers))
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgDetail)
            //集数Rv
            val favouriteDialogRvAdapter = if (favouriteInfor != null)
                FavouriteDialogRvAdapter(
                    context,
                    viewModel.mgs3List,
                    favouriteInfor?.mark ?: 0
                )
            else
                FavouriteDialogRvAdapter(context, viewModel.mgs3List, historyInfor?.mark ?: 0)

            rvDetail.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            rvDetail.setUpWithGrid(favouriteDialogRvAdapter, 4)
            //点击集数
            favouriteDialogRvAdapter.setOnClick { p: Int ->
                Log.i(TAG, "his:$historyMark ")
                viewModel.msg3Send(p)
                favouriteDialogRvAdapter.itemChange(p)
                historyInfor?.mark = p//当前页面
                favouriteViewModel.historyList[historyMark].mark = p//历史list
                favouriteViewModel.historyUpdate(historyInfor!!)//历史数据库
                favouriteViewModel.historyLivaData.value = historyMark
                if (btnDetailAdd.text.toString() == "已追漫") {
                    favouriteInfor?.mark = p//当前页面
                    favouriteViewModel.updateFavourite(favouriteInfor)//喜爱数据库
                }
            }
            //添加到喜爱,从喜爱中删除 添加到数据库,添加到list，添加到livedata
            btnDetailAdd.setOnClickListener {
                if (btnDetailAdd.text.toString() == "追漫") {
                    when (mark) {
                        R.id.homeFragment -> {
                            favouriteInfor =
                                favouriteViewModel.setFavouriteFromHome(
                                    favouriteViewModel.historyList[historyMark]
                                )
                        }
                        R.id.favoriteFragment -> {
                            favouriteViewModel.setFavourite(favouriteInfor)
                        }
                    }
                    favouriteMark = favouriteViewModel.favouriteListAdd(favouriteInfor!!)
                    Log.i(TAG, "onActivityCreated: $favouriteMark")
                    btnDetailAdd.text = "已追漫"
                    shortToast("追漫成功")
                } else {
                    Log.i(TAG, "onActivityCreated: $favouriteMark")
                    favouriteViewModel.favouriteDel(favouriteInfor,favouriteMark)
                    btnDetailAdd.text = "追漫"
                    shortToast("已取消追漫")
                }
            }
            //显示漫画
            viewModel.liveDataMsg4.observe(viewLifecycleOwner, { msg4: List<ByteArray?> ->
                if (msg4.size == 1) {
                    val builder = AlertDialog.Builder(requireContext())
                    val alertDialog = builder.create()
                    val view4 =
                        LayoutInflater.from(context).inflate(R.layout.dialog_cartoon, null, false)
                    val recyclerView4: RecyclerView = view4.findViewById(R.id.rvCartoon)
                    val btnBack = view4.findViewById<ImageButton>(R.id.btnCartoondialogBack)
                    val layTop = view4.findViewById<LinearLayout>(R.id.layCartoonDialog)
                    viewModel.msg4List.clear()
                    cartoonImgRvAdapter = CartoonImgRvAdapter(context, msg4)
                    alertDialog.setOnDismissListener {
                        viewModel.onMsg4Dismiss()
                        Runtime.getRuntime().gc()
                    }
                    //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
                    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.BLACK))
                    //一定要在setContentView之后调用，否则无效
                    alertDialog.window!!.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    recyclerView4.setUpWithLinear(cartoonImgRvAdapter)
                    cartoonImgRvAdapter.setOnClick {
                        if (layTop.visibility == View.VISIBLE)
                            layTop.visibility = View.GONE
                        else layTop.visibility = View.VISIBLE
                    }
                    btnBack.setOnClickListener { alertDialog.dismiss() }
                    alertDialog.setView(view4)
                    alertDialog.show()
                }
                if (msg4.isNotEmpty()) cartoonImgRvAdapter.notifyItemChanged(msg4.size - 1)
            })
        }
    }

}