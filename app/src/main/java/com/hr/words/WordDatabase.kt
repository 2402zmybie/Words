package com.hr.words

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Word2::class], version = 5, exportSchema = false)
abstract class  WordDatabase: RoomDatabase() {

    companion object {
        private var instance:WordDatabase? = null


        private val MIGRATION_4_5:Migration = object :Migration(4,5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE word2 ADD COLUMN chinese_invisible INTEGER NOT NULL DEFAULT 0")
            }

        }

        //数据库新增字段的更新
        private val MIGRATION_2_3:Migration = object :Migration(2,3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE word2 ADD COLUMN bar_data INTEGER NOT NULL DEFAULT 0")
            }

        }

        //数据库删除字段的更新
        private val MIGRATION_3_4:Migration = object :Migration(3,4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //创建临时表
                database.execSQL("CREATE TABLE `word2_temp` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `english_word` TEXT, `chinese_meaning` TEXT)")
                //拷贝数据
                database.execSQL("INSERT INTO `word2_temp`(`id`, `english_word`, `chinese_meaning`) SELECT id,english_word,chinese_meaning FROM word2")
                //删除原表
                database.execSQL("DROP TABLE word2")
                //临时表更名
                database.execSQL("ALTER TABLE word2_temp RENAME TO word2")
            }

        }

        fun getInstance(context: Context):WordDatabase {
            if(instance == null) {
                synchronized(WordDatabase::class) {
                    if(instance == null) {
                        instance = Room.databaseBuilder(context.applicationContext, WordDatabase::class.java, "word_database")
                                //强制在主线程执行
//                            .allowMainThreadQueries()
                                //破坏式地更新数据库 不推荐,会导致数据的丢失
//                            .fallbackToDestructiveMigration()
                                //增量更新,添加字段
//                            .addMigrations(MIGRATION_2_3)
                                //删除列,并保留数据
//                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
                            .build()
                    }
                }
            }
            return instance!!
        }
    }
    abstract fun getWordDao():WordDao
}