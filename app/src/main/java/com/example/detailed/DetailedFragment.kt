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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.example.adapter.CartoonImgRvAdapter
import com.example.base.BaseFragment
import com.example.base.TAG
import com.example.base.setUpWithGrid
import com.example.base.setUpWithLinear
import com.example.home.CartoonDialogRvAdapter
import com.example.hwq_cartoon.R
import com.example.repository.model.FavouriteInfor
import com.example.viewModel.CartoonViewModel
import kotlinx.android.synthetic.main.fragment_detailed.*
import java.util.*

/***
 * 页面 漫画详细
 * 逻辑 从数据库读取漫画名字并判断是否已经追漫
 * **/
class DetailedFragment : BaseFragment(R.layout.fragment_detailed) {
    private lateinit var cartoonImgRvAdapter: CartoonImgRvAdapter
    private lateinit var viewModel: CartoonViewModel
    private var favouriteInfor: FavouriteInfor? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[CartoonViewModel::class.java]
        //返回
        btnDetailBack.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
        //判断是否已经追漫
        val name = arguments?.getString("name")
        for (favouriteInfor in viewModel.favourite) {
            if (favouriteInfor.title == name) {
                btnDetailAdd.text = "已追漫"
                this.favouriteInfor = favouriteInfor
                break
            }
        }
        //漫画名与图片
        tvDetailName.text = name
        tvDetailContent.text = viewModel.content
        tvDetailContent.movementMethod = ScrollingMovementMethod()
        val headers = Headers {
            val map: MutableMap<String, String> =
                HashMap()
            map["Referer"] = "https://manhua.dmzj.com/update_1.shtml"
            map
        }
        Glide.with(requireContext()).asDrawable()
            .load(GlideUrl(arguments?.getString("img"), headers))
            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgDetail)
        //集数
        val cartoonRvAdapter1 =
            CartoonDialogRvAdapter(
                context,
                R.layout.cartoon_dialog_rv_item,
                viewModel.cartoonInforList
            )
        rvDetail.setUpWithGrid(cartoonRvAdapter1, 4)
        cartoonRvAdapter1.setOnClick { position: Int -> viewModel.msg3Send(position) }
        //添加到喜爱,从喜爱中删除
        val mark = arguments?.getInt("mark")
        btnDetailAdd.setOnClickListener {
            val position = arguments?.getInt("position") ?: return@setOnClickListener
            if (btnDetailAdd.text.toString() == "追漫") {
                when (mark) {
                    R.id.homeFragment -> {
                        favouriteInfor = viewModel.setFavouriteCartoon(position)

                    }
                }
                btnDetailAdd.text = "已追漫"
                shortToast("追漫成功")
            } else {
                when (mark) {
                    R.id.homeFragment -> viewModel.favouriteDel(favouriteInfor)
                }
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
                viewModel.stringList.clear()
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