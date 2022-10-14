package com.example.testworknode.presentation.Main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.testworknode.R
import com.example.testworknode.databinding.ListItemBinding
import com.example.testworknode.domain.Note
import com.example.testworknode.presentation.APP_ACTIVITY
import com.squareup.picasso.Picasso
import java.text.NumberFormat

class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteViewHolder>(diffCallbacks){


    var onItemsClicListener: OnItemsClickListener? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            with(item) {
                val formattedPrice = NumberFormat.getCurrencyInstance().format(item.price)
                if (item.imgUrl.isEmpty()|| item.imgUrl == "null") {
                    Picasso.get().load(R.drawable.img).into(imageView)
                } else {
                    Picasso.get().load(imgUrl)
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                        .into(imageView)
                }
                txtName.text = item.name
                txtCount.text = APP_ACTIVITY.getString(R.string.count_amount, item.count.toString())
                tvPrice.text = APP_ACTIVITY.getString(R.string.price_amount, formattedPrice)
                root.setOnClickListener {
                    onItemsClicListener?.onItemsClick(this)
                }
            }
        }
    }

    inner class NoteViewHolder(val binding: ListItemBinding) : ViewHolder(binding.root)

    companion object {


        object diffCallbacks : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.price == newItem.price
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

        }
    }
    interface OnItemsClickListener {
        fun onItemsClick(item: Note)
    }
}


