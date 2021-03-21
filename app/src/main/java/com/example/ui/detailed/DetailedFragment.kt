package com.example.ui.detailed

import android.annotation.SuppressLint
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
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.model.GlideUrl
import com.example.adapter.DetailImgRvAdapter
import com.example.adapter.DetailRvAdapter
import com.example.base.*
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentDetailedBinding
import com.example.repository.model.FavouriteInfor
import com.example.repository.model.HistoryInfor
import com.example.viewModel.CartoonViewModel
import com.example.viewModel.FavouriteViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


/***
 * 页面 漫画详细
 * 逻辑 从数据库读取漫画名字并判断是否已经追漫
 *     对点击的漫画保存到历史数据库，追漫的保存到追漫数据库
 * **/
class DetailedFragment : BaseFragment<FragmentDetailedBinding>(R.layout.fragment_detailed) {

    private lateinit var detailImgRvAdapter: DetailImgRvAdapter
    private val dateformat = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA)
    private var favouriteInfor: FavouriteInfor? = null
    private var historyInfor: HistoryInfor? = null
    private var historyMark = 0//记录在list位置
    private var favouriteMark = 0
    private lateinit var viewModel: CartoonViewModel
    private lateinit var favouriteViewModel: FavouriteViewModel
    private var mark: Int? = null
    private lateinit var favouriteDialogRvAdapter: DetailRvAdapter

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[CartoonViewModel::class.java]
        favouriteViewModel =
            ViewModelProvider(requireActivity())[FavouriteViewModel::class.java]
        b.frameLayout.setOnClickListener { }//避免点击到下一层的视图
        //返回
        b.btnDetailBack.setOnClickListener {
//            Navigation.findNavController(requireView()).navigateUp()
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out).remove(this).commit()
        }
        CoroutineScope(Dispatchers.Main).launch {
            //跳转所传递的数据
            val name = arguments?.getString("name")
            val img = arguments?.getString("img")
            mark = arguments?.getInt("mark")//判断fragment
            val href = arguments?.getString("href")
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
                        href,
                        0,
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
                        withContext(Dispatchers.Main) { b.btnDetailAdd.text = "已追漫" }
                        favouriteInfor = info
                        favouriteMark = index
                        break
                    }
                }
            }

            //漫画名与图片
            b.tvDetailName.text = name
            b.tvDetailContent.text = viewModel.content
            b.tvDetailUpdate.text = "最后更新时间:${viewModel.update}"
            b.tvDetailContent.movementMethod = ScrollingMovementMethod()
            if (img!!.contains("wuqimh"))
                setImg(b.imgDetail, img)
            else
                setImg(b.imgDetail, GlideUrl(img, headers))
            //集数Rv,判断是否在喜爱中
            favouriteDialogRvAdapter = if (favouriteInfor != null)
                DetailRvAdapter(
                    viewModel.msg3List,
                    favouriteInfor?.mark ?: 0
                )
            else
                DetailRvAdapter(viewModel.msg3List, historyInfor?.mark ?: 0)

            b.rvDetail.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            b.rvDetail.setUpWithGrid(favouriteDialogRvAdapter, 4)
            //点击集数
            favouriteDialogRvAdapter.setOnClick { p: Int ->
                update(p)
            }
            //添加到喜爱,从喜爱中删除 添加到数据库,添加到list，添加到livedata
            b.btnDetailAdd.setOnClickListener {
                if (b.btnDetailAdd.text.toString() == "追漫") {
                    when (mark) {
                        R.id.favoriteFragment -> {
                            favouriteViewModel.setFavourite(favouriteInfor)
                        }
                        else -> {
                            favouriteInfor =
                                favouriteViewModel.setFavouriteFromHome(
                                    favouriteViewModel.historyList[historyMark]
                                )
                        }
                    }
                    favouriteMark = favouriteViewModel.favouriteListAdd(favouriteInfor!!)
                    Log.i(TAG, "onActivityCreated: $favouriteMark")
                    b.btnDetailAdd.text = "已追漫"
                    shortToast("追漫成功")
                } else {
                    Log.i(TAG, "onActivityCreated: $favouriteMark")
                    favouriteViewModel.favouriteDel(favouriteMark)
                    b.btnDetailAdd.text = "追漫"
                    shortToast("已取消追漫")
                }
                //判断现存追漫数
                favouriteViewModel.likesIsZero()
            }
            //显示漫画
            viewModel.msg4LiveData.observe(viewLifecycleOwner, { msg4: List<ByteArray> ->
                if (msg4.size == 1) {
                    val builder = AlertDialog.Builder(requireContext())
                    val alertDialog = builder.create()
                    val constraintLayout = b.root.findViewById<ConstraintLayout>(R.id.linearLayout3)
                    val view4 =
                        LayoutInflater.from(context)
                            .inflate(R.layout.dialog_cartoon, constraintLayout, false)
                    val recyclerView4: RecyclerView = view4.findViewById(R.id.rvCartoon)
                    val btnBack = view4.findViewById<ImageButton>(R.id.btnCartoondialogBack)
                    val layTop = view4.findViewById<LinearLayout>(R.id.layCartoonDialog)
                    val tvNum = view4.findViewById<TextView>(R.id.tvCartoonNum)
                    val num = viewModel.imgUrlList.size
                    var lastPosition = -1//记录上一个itemview
                    //判断能否加载下一话
                    recyclerView4.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            val position =
                                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            if (lastPosition == position) return
                            lastPosition = position
                            tvNum.text = "${position + 1}/$num"
                            if (position + 1 == num && viewModel.msg3List.size - 1 > historyInfor!!.mark) {
                                AlertDialog.Builder(requireContext()).setMessage("是否加载下一话")
                                    .setNegativeButton("否") { d, _ ->
                                        d.dismiss()
                                    }.setPositiveButton("是") { d, _ ->
                                        alertDialog.setOnDismissListener { }
                                        viewModel.onMsg4Dismiss()
                                        alertDialog.dismiss()
                                        shortToast("正在加载下一页")
                                        d.dismiss()
                                        update(historyInfor!!.mark + 1)
                                    }.show()
                            }
                        }
                    })
                    viewModel.msg4List.clear()
                    detailImgRvAdapter =
                        DetailImgRvAdapter(msg4)
                    alertDialog.setOnDismissListener {
                        viewModel.onMsg4Dismiss()
                        Runtime.getRuntime().gc()
                    }
                    with(alertDialog) {
                        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
                        window!!.setBackgroundDrawable(ColorDrawable(Color.BLACK))
//                        一定要在setContentView之后调用，否则无效
                        window!!.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        recyclerView4.setUpWithLinear(detailImgRvAdapter)
                        detailImgRvAdapter.setOnClick {
                            if (layTop.visibility == View.VISIBLE)
                                layTop.visibility = View.GONE
                            else layTop.visibility = View.VISIBLE
                        }
                        btnBack.setOnClickListener { dismiss() }
                        setView(view4)
                        show()
                    }
                    viewModel.pgLiveData.value = true
                    return@observe
                }
                if (msg4.isNotEmpty()) detailImgRvAdapter.notifyItemChanged(msg4.size - 1)
            })
        }
    }

    /**更新观看集数并加载漫画**/
    private fun update(p: Int) {
        if (viewModel.pgLiveData.value == false) return
        Log.i(TAG, "his:$historyMark ")
        viewModel.msg3Send(p)
        favouriteDialogRvAdapter.itemChange(p)
        historyInfor?.mark = p//当前页面
        with(favouriteViewModel) {
            historyList[historyMark].mark = p//历史list
            historyUpdate(historyInfor!!)//历史数据库
            historyLivaData.value = historyMark
            if (b.btnDetailAdd.text.toString() == "已追漫") {
                favouriteInfor?.mark = p//当前页面
                updateFavourite(favouriteInfor)//喜爱数据库
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onMsg3Dismiss()
        if (mark != R.id.searchFragment)
            viewModel.bottomLiveData.value = false

    }
}