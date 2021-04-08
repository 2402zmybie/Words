package com.hr.words.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
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
        var viewType =
            SPUtils.getInstance().getBoolean(ConstVarage.IS_USING_CARD_VIEW, false)
        if(viewType) {
            recyclerView.adapter = adapterCard
        }else {
            recyclerView.adapter = adapter
        }

        fliterWords = wordViewModel.allWordsLive
        //观察数据
        fliterWords.observe(requireActivity(),
            Observer<List<Word2>> {
                val temp = adapter.itemCount
                adapter.allWords = it
                adapterCard.allWords = it
                if(temp != it.size) {
                    //数据长度发生变化
                    adapter.notifyDataSetChanged()
                    adapterCard.notifyDataSetChanged()
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
        var searchView = menu.findItem(R.id.search).actionView as SearchView
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
                    adapter.allWords = it
                    adapterCard.allWords = it
                    if(temp != it.size) {
                        //数据长度发生变化
                        adapter.notifyDataSetChanged()
                        adapterCard.notifyDataSetChanged()
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
                var viewType =
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