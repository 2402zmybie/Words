package com.hr.words

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    @ColumnInfo(name = "english_word")
    var word:String,
    @ColumnInfo(name = "chinese_meaning")
    var chineseMeaning:String
) {

}