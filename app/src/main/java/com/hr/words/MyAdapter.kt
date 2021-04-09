package com.hr.words

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(var useCardView: Boolean, var wordViewModel: WordViewModel) : ListAdapter<Word2, MyAdapter.MyViewHolder>(Word2DiffCallback()) {



    // ListAdapter中不需要对数据进行处理
//    var allWords:List<Word2> = ArrayList()
//        get()  = field
//        set(value) {
//            field = value
//        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //kotlin没有三元表达式
        val itemView = if(useCardView) LayoutInflater.from(parent.context).inflate(R.layout.cell_card_2,parent,false) else LayoutInflater.from(parent.context).inflate(R.layout.cell_normal_2,parent,false)
        val holder = MyViewHolder(itemView)
        holder.itemView.setOnClickListener {
            val uri = Uri.parse("https://m.youdao.com/dict?le=eng&q=${holder.textViewEnglish.text}")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            holder.itemView.context.startActivity(intent)
        }
        holder.switchChineseInvisible.setOnCheckedChangeListener { compoundButton, b ->
            val word2 = holder.itemView.getTag(R.string.word_for_view_holder) as Word2
            if(b) {
                holder.textViewChinese.visibility = View.GONE
                word2.chineseInvisible = true
                wordViewModel.updatetWords(word2)
            }else {
                holder.textViewChinese.visibility = View.VISIBLE
                word2.chineseInvisible = false
                wordViewModel.updatetWords(word2)
            }
        }
        return holder
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val word2 = getItem(position)
        //设置数据
        holder.itemView.setTag(R.string.word_for_view_holder, word2)
        holder.textViewNumber.text = position.toString()
        holder.textViewEnglish.text = word2.word
        holder.textViewChinese.text = word2.chineseMeaning
        //先设置监听器为null
//        holder.switchChineseInvisible.setOnCheckedChangeListener(null)
        if(word2.chineseInvisible) {
            holder.textViewChinese.visibility = View.GONE
            holder.switchChineseInvisible.isChecked = true
        }else {
            holder.textViewChinese.visibility = View.VISIBLE
            holder.switchChineseInvisible.isChecked = false
        }
    }

    /**
     * viewholder出现在屏幕上的回调
     */
    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.textViewNumber.text = (holder.adapterPosition + 1).toString()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewNumber:TextView = itemView.findViewById(R.id.textViewNumber);
        var textViewEnglish:TextView = itemView.findViewById(R.id.textViewEnglish);
        var textViewChinese:TextView = itemView.findViewById(R.id.textViewChinese);
        var switchChineseInvisible:Switch = itemView.findViewById(R.id.switchChineseInvisible)
    }
}

class Word2DiffCallback:DiffUtil.ItemCallback<Word2>() {
    override fun areItemsTheSame(oldItem: Word2, newItem: Word2): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Word2, newItem: Word2): Boolean {
        return oldItem.word.equals(newItem.word) && oldItem.chineseMeaning.equals(newItem.chineseMeaning) && oldItem.chineseInvisible == newItem.chineseInvisible
    }

}