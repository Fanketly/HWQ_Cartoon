package com.example.detailed

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
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
import com.example.base.BaseFragment
import com.example.base.headers
import com.example.base.setUpWithGrid
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.R
import com.example.repository.model.FavouriteInfor
import com.example.viewModel.CartoonViewModel
import com.example.viewModel.FavouriteViewModel
import kotlinx.android.synthetic.main.fragment_detailed.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/***
 * 页面 漫画详细
 * 逻辑 从数据库读取漫画名字并判断是否已经追漫
 *     已经追漫的对上次观看的集数进行保存，没有则仅在页面保存
 *
 * **/
class DetailedFragment : BaseFragment(R.layout.fragment_detailed) {

    private lateinit var cartoonImgRvAdapter: CartoonImgRvAdapter

    private var favouriteInfor: FavouriteInfor? = null
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
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

        val name = arguments?.getString("name")
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                //判断是否已经追漫
                for (info in viewModel.favourite) {
                    if (info.title == name) {
                        withContext(Dispatchers.Main) { btnDetailAdd.text = "已追漫" }
                        favouriteInfor = info
                        break
                    }
                }
            }

            //漫画名与图片
            tvDetailName.text = name
            tvDetailContent.text = viewModel.content
            tvDetailContent.movementMethod = ScrollingMovementMethod()

            Glide.with(requireContext()).asDrawable()
                .load(GlideUrl(arguments?.getString("img"), headers))
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgDetail)
            //集数Rv
            val favouriteDialogRvAdapter =
                FavouriteDialogRvAdapter(
                    context,
                    viewModel.mgs3List,
                    favouriteInfor?.mark ?: 0
                )
            rvDetail.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            rvDetail.setUpWithGrid(favouriteDialogRvAdapter, 4)
            favouriteDialogRvAdapter.setOnClick { position: Int ->
                viewModel.msg3Send(position)
                favouriteDialogRvAdapter.itemChange(position)
                if (btnDetailAdd.text.toString() == "已追漫") {
                    favouriteInfor?.mark = position
                    viewModel.updateFavourite(favouriteInfor)
                }
            }
            //添加到喜爱,从喜爱中删除
            val mark = arguments?.getInt("mark")
            btnDetailAdd.setOnClickListener {
                val position = arguments?.getInt("position") ?: return@setOnClickListener
                if (btnDetailAdd.text.toString() == "追漫") {
                    when (mark) {
                        R.id.homeFragment -> favouriteInfor = viewModel.setFavouriteHome(position)

                        R.id.favoriteFragment -> viewModel.setFavourite(favouriteInfor)
                    }
                    btnDetailAdd.text = "已追漫"
                    shortToast("追漫成功")
                } else {
                    viewModel.favouriteDel(favouriteInfor)
                    btnDetailAdd.text = "追漫"
                    shortToast("已取消追漫")
                }
            }
            //漫画
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