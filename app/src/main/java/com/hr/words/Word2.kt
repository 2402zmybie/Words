package com.hr.words

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Word2{
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
    @ColumnInfo(name = "english_word")
    var word:String? = null
    @ColumnInfo(name = "chinese_meaning")
    var chineseMeaning:String?=null
    //---------------2 删除列 作为数据库的迁移(3-4)
//    @ColumnInfo(name = "foo_data")
//    var foo:Boolean = false
//    //1 新增列 作为数据库的迁移(2-3)
//    @ColumnInfo(name = "bar_data")
//    var bar:Boolean = false
    //---------------2 删除列 作为数据库的迁移(3-4)

    @ColumnInfo(name = "chinese_invisible")
    var chineseInvisible:Boolean = false

    constructor(word: String?, chineseMeaning: String?) {
        this.word = word
        this.chineseMeaning = chineseMeaning
    }



}