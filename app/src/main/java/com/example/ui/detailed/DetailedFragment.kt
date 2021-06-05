package com.example.ui.detailed

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.adapter.DetailImgRvAdapter
import com.example.adapter.DetailRvAdapter
import com.example.base.*
import com.example.hwq_cartoon.App
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.DialogCartoonBinding
import com.example.hwq_cartoon.databinding.FragmentDetailedBinding
import com.example.repository.model.FavouriteInfor
import com.example.repository.model.HistoryInfor
import com.example.viewModel.DetailViewModel
import com.example.viewModel.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


/***
 * 页面 漫画详细
 * 逻辑 从数据库读取漫画名字并判断是否已经追漫
 *     对点击的漫画保存到历史数据库，追漫的保存到追漫数据库
 * **/

@AndroidEntryPoint
class DetailedFragment : BaseFragment<FragmentDetailedBinding>() {

    private lateinit var detailImgRvAdapter: DetailImgRvAdapter
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA)
    private var favouriteInfor: FavouriteInfor? = null
    private var historyInfor: HistoryInfor? = null
    private var historyMark = 0//记录在list位置
    private var favouriteMark = 0//标记第几个喜爱
    private val viewModel: DetailViewModel by activityViewModels()
    private val favouriteViewModel: FavouriteViewModel by activityViewModels()
    private var fragmentMark: Int? = null
    private lateinit var favouriteDialogRvAdapter: DetailRvAdapter
    private var y2: Float = 0f
    private var tranY: Float = 0f
    private var y: Float = 0f
    private var isLike: Boolean = false
    private lateinit var historyList: MutableList<HistoryInfor>
    private val pagerOrientation = App.pagerOrientation

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility", "InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.frameLayout.setOnClickListener { }//避免点击到下一层的视图
        //返回
        b.btnDetailBack.setOnClickListener {
            remove()
        }
        val displayMetrics = resources.displayMetrics
        val ydpi = displayMetrics.heightPixels
        b.btnDetailAdd.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    //1、event.getRowX（）：触摸点相对于屏幕原点的x坐标
                    //2、event.getX（）：    触摸点相对于其所在组件原点的x坐标
                    y = event.rawY
                    tranY = v.translationY
                    Log.i(TAG, "DOWM: $y tranY: $tranY")
                }
                MotionEvent.ACTION_MOVE -> {
                    y2 = event.rawY
                    if (y2 < (ydpi * 0.39f) || y2 > ydpi - 50)
                        return@setOnTouchListener false
                    b.rvDetail.translationY = tranY + y2 - y
                    b.btnDetailAdd.translationY = tranY + y2 - y
                }
                MotionEvent.ACTION_UP -> {
                    if (tranY == v.translationY)
                        like()
                }
            }
            return@setOnTouchListener false
        }
        CoroutineScope(Dispatchers.Main).launch {
            historyList = favouriteViewModel.historyList
            //跳转所传递的数据
            val name = arguments?.getString("name")
            val img = arguments?.getString("img")
            fragmentMark = arguments?.getInt("mark")//判断fragment
            val href = arguments?.getString("href")
            withContext(Dispatchers.Default) {
                //历史部分,修改上次观看时间
                val time = Date(System.currentTimeMillis())
                for ((index, info) in historyList.withIndex()) {
                    if (info.title == name) {
                        info.time = dateFormat.format(time)
                        historyInfor = info
                        favouriteViewModel.historyUpdate(info)
                        historyMark = index
                        break
                    }
                }
                //判断是否已经追漫
                for ((index, info) in favouriteViewModel.favouriteList.withIndex()) {
                    if (info.title == name) {
                        withContext(Dispatchers.Main) {
                            isLike = true
                            b.btnDetailAdd.setBackgroundResource(R.drawable.like_blue)
                        }
                        favouriteInfor = info
                        favouriteMark = index
                        break
                    }
                }
                //如果历史数据库里没有就添加
                if (historyInfor == null) {
                    //判断喜爱数据库是否有
                    historyInfor = if (favouriteInfor != null) HistoryInfor(
                        name,
                        img,
                        href,
                        favouriteInfor!!.mark,
                        dateFormat.format(time)
                    ) else HistoryInfor(
                        name,
                        img,
                        href,
                        0,
                        dateFormat.format(time)
                    )
                    historyList.add(0, historyInfor!!)
                    favouriteViewModel.historyInsert(historyInfor!!)
                    favouriteViewModel.historyLivaData.postValue(-1)
                }

            }
            //漫画名与图片
            b.tvDetailName.text = name
            b.tvDetailContent.text = viewModel.content
            b.tvDetailUpdate.text = "最后更新时间:${viewModel.update}"
            b.tvDetailContent.movementMethod = ScrollingMovementMethod()
            if (!img.isNullOrEmpty())
                b.imgDetailBackground.load(img)
            //集数Rv,判断是否在喜爱中
            Log.i("TAG", "msg3:${viewModel.msg3List} ")
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
            //显示漫画
            viewModel.msg4LiveData.observe(viewLifecycleOwner, { msg4: List<String> ->
                if (msg4.isNotEmpty()) {
                    val builder = AlertDialog.Builder(requireContext())
                    val alertDialog = builder.create()
                    val view4 = DialogCartoonBinding.inflate(layoutInflater, b.root, false)
                    //图片数量
                    val num = msg4.size
                    view4.tvCartoonNum.text = "1/$num"
                    var lastPosition = -1//记录上一个itemview
                    var job: Job? = null
                    //自动滚动
                    view4.chipAuto.setOnCheckedChangeListener {
                        if (it) {
                            Log.i(TAG, "autoScroll: ")
                            val autoSetting = App.autoSetting
                            job = CoroutineScope(Dispatchers.Main).launch {
                                while (true) {
                                    if (!job!!.isActive) break
                                    delay(1000)
                                    when(pagerOrientation){
                                        LinearLayout.VERTICAL-> view4.rvCartoon.smoothScrollBy(
                                            0,
                                            view4.rvCartoon.scrollY + autoSetting!!
                                        )
                                        LinearLayout.HORIZONTAL-> view4.rvCartoon.smoothScrollBy(
                                            view4.rvCartoon.scrollX + autoSetting!!,
                                            0
                                        )
                                        3-> view4.rvCartoon.smoothScrollBy(
                                            view4.rvCartoon.scrollX - autoSetting!!,
                                            0
                                        )
                                    }

                                }
                            }
                        } else {
                            Log.i(TAG, "autoCancel: ")
                            job!!.cancel()
                        }
                    }
                    //判断能否加载下一话
                    view4.rvCartoon.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            val position =
                                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            if (lastPosition == position) return
                            lastPosition = position
                            view4.tvCartoonNum.text = "${position + 1}/$num"
                            //第一部分判断是否为最后一张图片 第二部分判断集数是否为最后一集
                            if (position + 1 == num && viewModel.msg3List.size - 1 > historyInfor!!.mark) {
                                //取消滚动
                                view4.chipAuto.setChecked(false)
                                AlertDialog.Builder(requireContext()).setMessage("是否加载下一话")
                                    .setNegativeButton("否") { d, _ ->
                                        d.dismiss()
                                    }.setPositiveButton("是") { d, _ ->
                                        shortToast("正在加载下一页")
                                        d.dismiss()
                                        alertDialog.setOnDismissListener(null)
                                        alertDialog.dismiss()
                                        viewModel.onMsg4Dismiss()
                                        update(historyInfor!!.mark + 1)
                                    }.show()
                            }
                        }
                    })

                    detailImgRvAdapter =
                        DetailImgRvAdapter(msg4)
                    detailImgRvAdapter.setOnClick {
                        if (view4.layCartoonDialog.visibility == View.VISIBLE)
                            view4.layCartoonDialog.visibility = View.GONE
                        else view4.layCartoonDialog.visibility = View.VISIBLE
                    }
                    App.pagerOrientation?.let {
                        when (it) {
                            3 -> view4.rvCartoon.setUpWithLinear(
                                detailImgRvAdapter,
                                LinearLayout.HORIZONTAL,
                                true
                            )
                            LinearLayout.HORIZONTAL -> view4.rvCartoon.setUpWithLinear(
                                detailImgRvAdapter,
                                it
                            )
                            else -> view4.rvCartoon.setUpWithLinear(detailImgRvAdapter)
                        }
                    }
                    alertDialog.setOnDismissListener {
                        Log.i(TAG, "Dialog4Dismiss: ")
                        viewModel.onMsg4Dismiss()
                        Runtime.getRuntime().gc()
                    }
                    with(alertDialog) {
                        setCanceledOnTouchOutside(false)
                        with(window!!) {
                            decorView.setPadding(0, 0, 0, 0)
                            //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
                            setBackgroundDrawable(ColorDrawable(Color.BLACK))
                            setLayout(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                        view4.btnCartoondialogBack.setOnClickListener {
                            //取消滚动
                            view4.chipAuto.setChecked(false)
                            dismiss()
                        }
                        setView(view4.root)
                        show()
                    }
                    viewModel.pgLiveData.value = true
                    return@observe
                }
            })
        }
    }

    //判断是否为喜爱；如果当前页面为添加过喜爱的用原数据，否则就从历史List加载数据
    private fun like() {
        if (!isLike) {
            //添加到数据库
            if (favouriteInfor != null) {
                favouriteViewModel.setFavourite(favouriteInfor)
            } else {
                favouriteInfor =
                    favouriteViewModel.setFavouriteFromHome(
                        historyMark
                    )
            }
            //添加到List
            favouriteMark = favouriteViewModel.favouriteListAdd(favouriteInfor!!)
            isLike = true
            b.btnDetailAdd.setBackgroundResource(R.drawable.like_blue)
            Log.i("TAG", "DetailedFragment_like:$favouriteMark ")
            shortToast("追漫成功")
        } else {
            Log.i("TAG", "DetailedFragment_Unlike:$favouriteMark ")
            favouriteViewModel.favouriteDel(favouriteMark)
            isLike = false
            b.btnDetailAdd.setBackgroundResource(R.drawable.unlike)
            shortToast("已取消追漫")
        }
        //判断现存追漫数
        favouriteViewModel.likesIsZero()
    }

    /**更新观看集数并加载漫画**/
    private fun update(p: Int) {
        if (viewModel.pgLiveData.value == false) return
        Log.i(TAG, "his:$historyMark ")
        viewModel.msg3Send(p)
        favouriteDialogRvAdapter.itemChange(p)
        historyInfor?.mark = p//当前页面
        historyList[historyMark].mark = p//历史list
        with(favouriteViewModel) {
            historyUpdate(historyInfor!!)//历史数据库
            historyLivaData.value = historyMark
            if (isLike) {
                favouriteInfor?.mark = p//当前页面
                favouriteUpdate(favouriteInfor)//喜爱数据库
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onMsg3Dismiss()
        if (fragmentMark != R.id.searchFragment)
            viewModel.bottomLiveData.value = false

    }

    override fun viewBinding(container: ViewGroup?): FragmentDetailedBinding {
        return FragmentDetailedBinding.inflate(layoutInflater, container, false)
    }
}