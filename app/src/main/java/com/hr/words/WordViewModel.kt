package com.hr.words

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private var wordRepository:WordRepository = WordRepository(application)
    var allWordsLive: LiveData<List<Word2>>

    init {
        allWordsLive = wordRepository.allWordsLive
    }


    //外界操作数据的方法
    fun insertWords(vararg words:Word2) {
        wordRepository.insertWords(*words)
    }
    fun updatetWords(vararg words:Word2) {
        wordRepository.updatetWords(*words)
    }
    fun deletetWords(vararg words:Word2) {
        wordRepository.deletetWords(*words)
    }
    fun deletetAllWords() {
        wordRepository.deletetAllWords()
    }
    fun  findWordsWithPattern(pattern:String):LiveData<List<Word2>> {
        return wordRepository.findWordsWithPattern(pattern)
    }






}