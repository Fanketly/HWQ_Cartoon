package com.example.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.base.BaseFragment
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.R
import com.example.repository.model.FavouriteInfor
import com.example.viewModel.CartoonViewModel

class FavoriteFragment : BaseFragment(R.layout.fragment_favorite) {
    private lateinit var favouriteInfor: FavouriteInfor
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        val viewModel =ViewModelProvider(requireActivity())[CartoonViewModel::class.java]
        val list = viewModel.favourite
        val rvFavourite: RecyclerView = view.findViewById(R.id.rvFavourite)
        val favouriteRvAdapter = FavouriteRvAdapter(list, R.layout.cartoon_rv_item, context)
        rvFavourite.setUpWithLinear(favouriteRvAdapter)
        rvFavourite.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        favouriteRvAdapter.setOnClick(object : FavouriteRvAdapter.OnClick {
            override fun onClick(position: Int) {
                viewModel.favouriteGet(list[position].url)
                favouriteInfor = list[position]
            }

            override fun longOnClick(position: Int) {
                viewModel.favouriteDel(list[position])//数据库删除
                list.removeAt(position)//list删除
                favouriteRvAdapter.notifyItemRemoved(position)
                favouriteRvAdapter.notifyItemRangeChanged(position, list.size)
                Toast.makeText(context, "已移除", Toast.LENGTH_SHORT).show()
            }
        })
        //msg3集数
//        viewModel.liveDataMsg3.observe(viewLifecycleOwner, { msg3: List<CartoonInfor> ->
//            if (msg3.isNotEmpty()) {
//                val builder = AlertDialog.Builder(context)
//                val alertDialog = builder.create()
//                val view3 = LayoutInflater.from(context).inflate(R.layout.dialog_cartoon, null, false)
//                val rv3: RecyclerView = view3.findViewById(R.id.rvCartoon)
//                rv3.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//                val cartoonRvAdapter1 = FavouriteDialogRvAdapter(context, msg3, favouriteInfor.mark)
//                rv3.adapter = cartoonRvAdapter1
//                rv3.setUpWithLinear(cartoonRvAdapter1)
//                alertDialog.setOnDismissListener {
//                    viewModel.onMsg3Dismiss()
//                }
//                //点击集数
//                cartoonRvAdapter1.setOnClick {
//                    viewModel.msg3Send(it)
//                    favouriteInfor.mark = it
//                    viewModel.update(favouriteInfor)
//                    cartoonRvAdapter1.itemChange(it)
//                }
//                alertDialog.setView(view3)
//                alertDialog.show()
//            }
//        })
//        //msg4显示漫画
//        viewModel.liveDataMsg4.observe(viewLifecycleOwner, { msg4: List<ByteArray?> ->
//            if (msg4.size == 1) {
//                val builder = AlertDialog.Builder(context)
//                val alertDialog = builder.create()
//                val view4 = LayoutInflater.from(context).inflate(R.layout.dialog_cartoon, null, false)
//                val recyclerView4: RecyclerView = view4.findViewById(R.id.rvCartoon)
//                viewModel.stringList.clear()
//                cartoonImgRvAdapter = CartoonImgRvAdapter(context, msg4)
//                alertDialog.setOnDismissListener {
//                    viewModel.onMsg4Dismiss()
//                    Runtime.getRuntime().gc()
//                }
//                //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
//                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.BLACK))
//                //一定要在setContentView之后调用，否则无效
//                alertDialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//                recyclerView4.setUpWithLinear(cartoonImgRvAdapter)
//                alertDialog.setView(view4)
//                alertDialog.show()
//            }
//            if (msg4.isNotEmpty()) cartoonImgRvAdapter.notifyItemChanged(msg4.size - 1)
//        })

        return view
    }

}