package com.hr.words

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordDao {
    //vararg可变参数
    @Insert
    fun insertWords(vararg words:Word2)

    @Update
    fun updateWords(vararg words:Word2)

    @Delete
    fun deleteWords(vararg words:Word2)

    @Query("delete from word2")
    fun deleteAllWords();

    @Query("select * from word2 order by id desc")
//    fun getAllWords(): List<Word2>
    fun getAllWordsLive(): LiveData<List<Word2>>

    @Query("select * from word2 where english_word like :pattern order by id desc")
    fun findWordsWithPattern(pattern:String): LiveData<List<Word2>>
}