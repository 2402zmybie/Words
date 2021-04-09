package com.hr.words.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.hr.words.MyAdapter
import com.hr.words.R
import com.hr.words.Word2
import com.hr.words.WordViewModel
import com.hr.words.const.ConstVarage
import kotlinx.android.synthetic.main.fragment_words.*
import org.jetbrains.anko.AlertDialogBuilder


class WordsFragment : Fragment() {

    lateinit var wordViewModel: WordViewModel
    lateinit var adapter: MyAdapter
    lateinit var adapterCard: MyAdapter
    lateinit var fliterWords: LiveData<List<Word2>>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        wordViewModel = ViewModelProvider(requireActivity(),ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(
            WordViewModel::class.java)
        adapter = MyAdapter(false, wordViewModel)
        adapterCard = MyAdapter(true, wordViewModel)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val viewType =
            SPUtils.getInstance().getBoolean(ConstVarage.IS_USING_CARD_VIEW, false)
        if(viewType) {
            recyclerView.adapter = adapterCard
        }else {
            recyclerView.adapter = adapter
        }
        //
        recyclerView.itemAnimator = object :DefaultItemAnimator() {
            override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                super.onAnimationFinished(viewHolder)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                val findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
                //遍历findFirstVisibleItemPosition到findLastVisibleItemPosition
                for(i in findFirstVisibleItemPosition..findLastVisibleItemPosition) {
                    val holder = recyclerView.findViewHolderForAdapterPosition(i) as MyAdapter.MyViewHolder
                    holder.textViewNumber.text = (i + 1).toString()
                }
            }
        }

        fliterWords = wordViewModel.allWordsLive
        //观察数据
        fliterWords.observe(requireActivity(),
            Observer<List<Word2>> {
                val temp = adapter.itemCount
                if(temp != it.size) {
                    //数据长度发生变化
                    //插入数据之后 滚动
                    recyclerView.smoothScrollBy(0, -200)
                    //提交的数据列表,会在后台进行差异化比较,根据比对结果,来刷新界面
                    adapter.submitList(it)
                    adapterCard.submitList(it)
                }
            })

        floatingActionButton.setOnClickListener {
            val controller = Navigation.findNavController(it)
            controller.navigate(R.id.action_wordsFragment_to_addFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.mmain_menu, menu)
        //search
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.maxWidth = 700
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val pattern = p0?.trim()
                //先移除观察 避免观察碰撞
                fliterWords.removeObservers(requireActivity())
                fliterWords = wordViewModel.findWordsWithPattern(pattern.toString())
                //删选的liveData的集合数据 观察数据变化
                fliterWords.observe(requireActivity(),Observer<List<Word2>> {
                    val temp = adapter.itemCount
                    if(temp != it.size) {
                        //数据长度发生变化
                        adapter.submitList(it)
                        adapterCard.submitList(it)
                    }
                })
                //消费事件
                return true
            }

        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.clearData -> {
                AlertDialog.Builder(requireActivity())
                    .setTitle("清空数据")
                    .setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i ->
                        wordViewModel.deletetAllWords()
                    })
                    .setNegativeButton("取消",null )
                    .create()
                    .show()
            }
            R.id.switchViewType -> {
                val viewType =
                    SPUtils.getInstance().getBoolean(ConstVarage.IS_USING_CARD_VIEW, false)
                if(viewType) {
                    recyclerView.adapter = adapter
                    SPUtils.getInstance().put(ConstVarage.IS_USING_CARD_VIEW, false)
                }else {
                    recyclerView.adapter = adapterCard
                    SPUtils.getInstance().put(ConstVarage.IS_USING_CARD_VIEW, true)
                }
            }
        }
        return true
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //显示导航条menu, 默认是false
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_words, container, false)
    }





}