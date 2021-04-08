package com.hr.words.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.hr.words.R
import com.hr.words.Word2
import com.hr.words.WordViewModel
import kotlinx.android.synthetic.main.fragment_add.*


class AddFragment : Fragment() {

    lateinit var inputMethodManager: InputMethodManager
    lateinit var wordViewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        wordViewModel = ViewModelProvider(requireActivity(),ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(
            WordViewModel::class.java)
        buttonSubmit.isEnabled = false
        inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //获取焦点
        editTextTextEnglish.requestFocus()
        //弹出键盘
        inputMethodManager.showSoftInput(editTextTextEnglish,0)

        val textWatcher = object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val english = editTextTextEnglish.text.toString().trim()
                val chinese = editTextTextChinese.text.toString().trim()
                buttonSubmit.isEnabled = english.isNotEmpty() && chinese.isNotEmpty()
            }
        }
        editTextTextEnglish.addTextChangedListener(textWatcher)
        editTextTextChinese.addTextChangedListener(textWatcher)

        buttonSubmit.setOnClickListener {
            var english = editTextTextEnglish.text.toString().trim()
            var chinese = editTextTextChinese.text.toString().trim()
            val word2 = Word2(english, chinese)
            //添加数据
            wordViewModel.insertWords(word2)
            //呼叫导航 返回上一页
            val navController = Navigation.findNavController(it)
            navController.navigateUp()
        }
    }
}